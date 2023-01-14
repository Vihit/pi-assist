package com.digitedgy.piassist.service;

import com.digitedgy.piassist.entity.Feature;
import com.digitedgy.piassist.entity.PI;
import com.digitedgy.piassist.entity.Sprint;
import com.digitedgy.piassist.entity.Story;
import com.digitedgy.piassist.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JiraService {

    @Autowired
    FeatureService featureService;

    @Autowired
    SprintService sprintService;

    @Autowired
    PIService piService;

    @Autowired
    StoryService storyService;

    public ArrayList<Jira> getJirasForPI(String authHeader) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        String url = "https://dbatlas.db.com/jira02/rest/api/latest/search?jql=filter=326544+and+cf[15200]=\"RF.Spartans\"";
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<FilterSearchResponse> response = restTemplate.exchange(url, HttpMethod.GET, request, FilterSearchResponse.class);
        return response.getBody().getIssues();
    }

    public void refreshJiras(String authHeader, String team) {
        Optional<PI> openPI = piService.findOpenPIForTeam(team);
        if(openPI.isPresent()) {
            featureService.deleteAll();
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", authHeader);
            String url = "https://dbatlas.db.com/jira02/rest/api/latest/search?jql=filter="+openPI.get().getFilter()+"+and+cf[15200]=\""+openPI.get().getTeam()+"\"";
            String storyUrl = "https://dbatlas.db.com/jira02/rest/api/latest/issue/";
            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<FilterSearchResponse> response = restTemplate.exchange(url, HttpMethod.GET, request, FilterSearchResponse.class);
            List<Jira> jiras = response.getBody().getIssues();

            List<Feature> features = jiras.stream().filter(jira -> jira.getFields().getIssueType().getName().equals("New Feature")).map(jira -> {
                Set<Story> stories = jira.getFields().getIssueLinks() != null && jira.getFields().getIssueLinks().size() > 0 ?
                        jira.getFields().getIssueLinks().stream().filter(issueLink -> issueLink.getOutwardIssue() != null && issueLink.getOutwardIssue().getFields().getIssueType().getName().equalsIgnoreCase("Story")).map(issueLink -> new Story(issueLink.getOutwardIssue().getKey(), "","","",0,"",""))
                                .collect(Collectors.toSet()) : new HashSet<>();
                Set<Story> updatedStories = stories.stream().map(story -> {
                    ResponseEntity<Jira> jiraResponse = restTemplate.exchange(storyUrl+story.getKey(), HttpMethod.GET, request, Jira.class);
                    Jira storyDetails = jiraResponse.getBody();
                    story.setEstimate((int)storyDetails.getFields().getCustomfield_10002());
                    story.setDescription(storyDetails.getFields().getDescription());
                    story.setAcceptanceCriteria(storyDetails.getFields().getCustomfield_11306());
                    story.setTitle(storyDetails.getFields().getSummary());

                    String sprintObject = storyDetails.getFields().getCustomfield_10005()!=null ? storyDetails.getFields().getCustomfield_10005().get(0) : "[]";
                    story.setSprint(Arrays.asList(sprintObject.substring(sprintObject.indexOf("["),sprintObject.lastIndexOf("]")).split(",")).stream().filter(variable->variable.startsWith("name=")).findFirst().orElseGet(()->(" = ")).split("=")[1]);
                    story.setAssignee(storyDetails.getFields().getAssignee() != null ? storyDetails.getFields().getAssignee().getDisplayName() : null);
                    return story;
                }).collect(Collectors.toSet());
                Feature feature = new Feature(jira.getKey(), jira.getFields().getLabels().stream().collect(Collectors.joining(",")),
                        jira.getFields().getAssignee().getEmailAddress(), jira.getFields().getDescription(), jira.getFields().getSummary(),
                        jira.getFields().getCustomfield_11306(), "","","",null, updatedStories.stream().map(story -> story.getEstimate()).reduce((a,b)->a+b).orElse(0),
                        "N", openPI.get().getTeam(), openPI.get().getId().toString(), stories);
                updatedStories.forEach(story -> story.setFeature(feature));
                return feature;
            }).collect(Collectors.toList());

            featureService.saveAll(features);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No open PI for team "+team);
        }
    }

    public void refreshPISprint(String authHeader, String team) {
        Optional<PI> openPI = piService.findOpenPIForTeam(team);
        if(openPI.isPresent()) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization",authHeader);
            String url = "https://dbatlas.db.com/jira02/rest/agile/1.0/board/"+openPI.get().getBoardId()+"/sprint?state=future&";
            HttpEntity<String> request = new HttpEntity<>(headers);
            SprintSearchResponse sprintSearch = new SprintSearchResponse(0,0,false,null);
            int newStartAt = 0;
            ArrayList<SprintModel> sprints = new ArrayList<>();
            while(!sprintSearch.getIsLast()) {
                ResponseEntity<SprintSearchResponse> response = restTemplate.exchange(url+"startAt="+newStartAt, HttpMethod.GET, request, SprintSearchResponse.class);
                sprintSearch = response.getBody();
                sprints.addAll(sprintSearch.getValues().stream().filter(sprint->sprint.getOriginBoardId() == openPI.get().getBoardId()).collect(Collectors.toList()));
                newStartAt = newStartAt + sprintSearch.getMaxResults();
            }

            sprintService.saveAll(sprints.stream().filter(sprint->sprint.getStateDate().compareTo(openPI.get().getStart()) >= 0 && sprint.getEndDate().compareTo(openPI.get().getEnd()) <= 0)
                    .map(sprint->new Sprint(sprint.getName(), sprint.getStateDate(), sprint.getEndDate(), openPI.get(), sprint.getId())).collect(Collectors.toList()),openPI.get().getId());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No open PI for team "+team);
        }
    }

    public void pushFeatureToJira(String authHeader, Feature feature) {
        feature.getStories().forEach(story -> {
            String key = createStory(authHeader, story);
            story.setKey(key);
            storyService.update(story);
        });
    }

    public String createStory(String authHeader, Story story) {
        Optional<Sprint> sprint = sprintService.getSprintByName(story.getSprint());
        if(sprint.isPresent()) {
            AddIssueLink addIssueLink = new AddIssueLink(new AddLink(new LinkType("Consists","is part of","consists of"), new InwardIssue(story.getFeature().getId())));
            ArrayList<AddIssueLink> issueLinks = new ArrayList<>();
            issueLinks.add(addIssueLink);
            StoryModel storyModel = new StoryModel(new JiraStoryDetails(story.getDescription(), story.getTitle(), new IssueType("Story"),
                    story.getEstimate(), story.getAcceptanceCriteria(), sprint.get().getJiraSprintId(), story.getFeature().getTeam(),
                    new Project("MKRS")),new StoryLinks(new UpdateLinks(issueLinks)));

            if(story.getKey()==null || story.getKey().equalsIgnoreCase("")) {
                return createNewStory(authHeader,storyModel);
            } else {
                return updateStory(authHeader, storyModel, story.getKey());
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No valid sprint attached to the request!");
        }
    }

    public String createNewStory(String authHeader, StoryModel story) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",authHeader);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonStory = objectMapper.writeValueAsString(story);

            String url = "https://dbatlas.db.com/jira02/rest/api/latest/issue/";
            HttpEntity<String> request = new HttpEntity<>(jsonStory, headers);
            CreateStoryResponse createStoryResponse = restTemplate.postForObject(url, request, CreateStoryResponse.class);
            return createStoryResponse.getKey();
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Some error occurred");
        }
    }

    public String updateStory(String authHeader, StoryModel story, String key) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",authHeader);
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonStory = objectMapper.writeValueAsString(story);

            String url = "https://dbatlas.db.com/jira02/rest/api/latest/issue/"+key;
            HttpEntity<String> request = new HttpEntity<>(jsonStory, headers);
            restTemplate.put(url, request);

            return key;
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Some error occurred");
        }
    }
}
