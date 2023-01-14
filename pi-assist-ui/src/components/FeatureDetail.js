import { useEffect, useState } from "react";
import { Link, useHistory, useLocation } from "react-router-dom";
import "./FeatureDetail.css";
import { config } from "./config";

function FeatureDetail(props) {
  const initialPI =
    localStorage.getItem("PI") !== "undefined"
      ? JSON.parse(localStorage.getItem("PI"))
      : {};
  const [pi, setPI] = useState(initialPI);
  const location = useLocation();
  const [feature, setFeature] = useState(location.state);
  const [team, setTeam] = useState([]);
  const [storyDetailsToggle, setStoryDetailsToggle] = useState(false);
  const [descriptionDetailsToggle, setDescriptionDetailsToggle] =
    useState(true);
  const [storiesInFeature, setStoriesInFeature] = useState(feature.stories);
  const [discussionDetailsToggle, setDiscussionDetailsToggle] = useState(true);
  const [storiesToggle, setStoriesToggle] = useState(true);
  const [showStoryEditor, setShowStoryEditor] = useState(false);
  const [acceptanceCriteriaToggle, setAcceptanceCriteriaToggle] =
    useState(true);
  const [storyInEditor, setStoryInEditor] = useState({
    id: null,
    key: null,
    title: null,
    sprint: null,
    estimate: null,
    description: null,
    acceptanceCriteria: null,
    assignee: null,
  });
  const [alert, setAlert] = useState(false);
  const [alertContent, setAlertContent] = useState("");

  useEffect(() => {
    getTeam();
    setPI((prev) => {
      return initialPI;
    });
  }, []);

  let history = useHistory();

  function getTeam() {
    const team = JSON.parse(localStorage.getItem("user")).team;
    fetch(config.apiUrl + "users/" + team, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
        Authorization:
          "Basic " + JSON.parse(localStorage.getItem("user")).authToken,
      },
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        }
      })
      .then((actualData) => {
        setTeam(actualData);
      });
  }

  function setNewValue(e) {
    const id = e.target.id;
    const value = e.target.value;
    const newFeature = { ...feature };
    newFeature[id] = value;
    setFeature(newFeature);
  }

  function toggleStoryDetails() {
    setStoryDetailsToggle(!storyDetailsToggle);
  }

  function toggleDescriptionDetails() {
    setDescriptionDetailsToggle(!descriptionDetailsToggle);
  }

  function toggleDiscussionDetails() {
    setDiscussionDetailsToggle(!discussionDetailsToggle);
  }

  function toggleAcceptanceCriteria() {
    setAcceptanceCriteriaToggle(!acceptanceCriteriaToggle);
  }
  function toggleStories() {
    setStoriesToggle(!storiesToggle);
  }

  function cancelStoryEdit() {
    setShowStoryEditor(false);
    setStoryInEditor((prev) => {
      return {
        id: "",
        key: "",
        title: "",
        sprint: "",
        estimate: "",
        description: "",
        acceptanceCriteria: "",
        assignee: "",
      };
    });
  }

  function openStoryEditor(story) {
    let editStory = { ...storyInEditor };
    editStory = story;
    setStoryInEditor(editStory);
    setShowStoryEditor(true);
  }

  function addStory() {
    if (storiesInFeature.filter((story) => story.id === "").length === 0) {
      let stories = [...storiesInFeature];
      stories.push({
        id: "",
        key: "",
        title: "",
        sprint: "",
        estimate: "",
        description: "",
        acceptanceCriteria: "",
        assignee: "",
      });
      setStoriesInFeature(stories);
    } else {
    }
  }

  function estimateStory() {
    props.onPlanningPokerClick();
    openItemForEstimation();
  }

  function openItemForEstimation() {
    let item = {
      key: storyInEditor.id,
      summary: storyInEditor.title,
      closed: false,
      team: JSON.parse(localStorage.getItem("user")).team,
    };
    fetch(config.apiUrl + "planning-poker/", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
        Authorization:
          "Basic " + JSON.parse(localStorage.getItem("user")).authToken,
      },
      body: JSON.stringify(item),
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        }
        throw new Error("An item is already being estimated!");
      })
      .then((actualData) => {
        console.log("Item ready to be estimated " + actualData);
        history.push(
          "/planning-poker/id=" +
            storyInEditor.id +
            "&name=" +
            storyInEditor.title
        );
      })
      .catch(function (error) {
        console.log("An item is already being estimated!", error);
      });
  }

  function updateStoryInEditor(id, value) {
    const sIE = { ...storyInEditor };
    sIE[id] = value;
    setStoryInEditor(sIE);
  }

  function saveStoryEdit() {
    if (storyInEditor.id === "" && storyInEditor.title !== "") {
      fetch(config.apiUrl + "story/" + feature.id, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
          Authorization:
            "Basic " + JSON.parse(localStorage.getItem("user")).authToken,
        },
        body: JSON.stringify(storyInEditor),
      })
        .then((response) => {
          if (response.ok) {
            setAlert(true);
            setAlertContent("Story added!");
            const timeId = setTimeout(() => {
              setAlert(false);
              setAlertContent("");
            }, 1000);
            return response.json();
          }
          throw new Error("Some error occurred!");
        })
        .then((actualData) => {
          console.log(actualData);
          const staleStories = feature.stories;
          const updatedStories = staleStories.filter(
            (story) => story.id !== actualData.id
          );
          updatedStories.push(actualData);
          setFeature({
            ...feature,
            stories: updatedStories,
            estimate: updatedStories
              .map((story) => Number(story.estimate))
              .reduce((partialSum, a) => partialSum + a, 0),
          });
          setStoriesInFeature(updatedStories);
          setShowStoryEditor(false);
        })
        .catch(function (error) {
          console.log("Some error occurred!", error);
        });
    } else {
      fetch(config.apiUrl + "story/", {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
          Authorization:
            "Basic " + JSON.parse(localStorage.getItem("user")).authToken,
        },
        body: JSON.stringify(storyInEditor),
      })
        .then((response) => {
          if (response.ok) {
            setAlert(true);
            setAlertContent("Story saved!");
            const timeId = setTimeout(() => {
              setAlert(false);
              setAlertContent("");
            }, 1000);
            return response.json();
          }
          throw new Error("Some error occurred!");
        })
        .then((actualData) => {
          const staleStories = feature.stories;
          const updatedStories = staleStories.filter(
            (story) => story.id !== storyInEditor.id
          );
          updatedStories.push(storyInEditor);
          const updatedFeature = {
            ...feature,
            stories: updatedStories,
            estimate: updatedStories
              .map((story) => Number(story.estimate))
              .reduce((partialSum, a) => partialSum + a, 0),
          };
          // setFeature(updatedFeature);
          setStoriesInFeature(updatedStories);
          saveFeature(updatedFeature);
          setShowStoryEditor(false);
        })
        .catch(function (error) {
          console.log("Some error occurred!", error);
        });
    }
  }

  function saveFeature(featureToBeSaved) {
    if (featureToBeSaved === null) {
      featureToBeSaved = feature;
    }
    fetch(config.apiUrl + "feature/", {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
        Authorization:
          "Basic " + JSON.parse(localStorage.getItem("user")).authToken,
      },
      body: JSON.stringify(featureToBeSaved),
    })
      .then((response) => {
        if (response.ok) {
          setAlert(true);
          setAlertContent("Feature updated!");
          const timeId = setTimeout(() => {
            setAlert(false);
            setAlertContent("");
          }, 1000);
          return response.json();
        }
        throw new Error("Some error occurred!");
      })
      .then((actualData) => {
        console.log("Feature updated " + actualData);
        setFeature(featureToBeSaved);
      })
      .catch(function (error) {
        console.log("Some error occurred!", error);
      });
  }

  function togglePlanned(flag) {
    const updatedFeature = { ...feature, isPlanned: flag };
    console.log(updatedFeature);
    setFeature((prevFeature) => {
      return updatedFeature;
    });
    saveFeature(updatedFeature);
  }

  function deleteStory(storyId, storyTitle) {
    const storiesToBeUpdated = [...storiesInFeature];
    if (storyId === "") {
      setStoriesInFeature(
        storiesToBeUpdated.filter((story) => story.id !== "")
      );
      cancelStoryEdit();
    } else {
      fetch(config.apiUrl + "story/" + storyInEditor.id, {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
          Authorization:
            "Basic " + JSON.parse(localStorage.getItem("user")).authToken,
        },
      })
        .then((response) => {
          if (response.ok) {
            setStoriesInFeature(
              storiesToBeUpdated.filter((story) => story.id !== storyId)
            );
            cancelStoryEdit();
            setAlert(true);
            setAlertContent("Story deleted!");
            const timeId = setTimeout(() => {
              setAlert(false);
              setAlertContent("");
            }, 1000);
            return response;
          }
          throw new Error("Story does not exist!");
        })
        .then((actualData) => {
          console.log("Story deleted!");
        })
        .catch(function (error) {
          console.log("Story does not exist!", error);
        });
    }
  }

  return (
    <div>
      <div
        className={
          "team-notification" + (alert ? "" : " team-notification-hidden")
        }
      >
        <div>{alertContent}</div>
      </div>
      <div className={"story-edit " + (showStoryEditor ? "" : "close-flex ")}>
        <div className="story-edit-header">
          <div className="flex-row-title">
            <div className="edit-add-story">
              <i className="fa-solid fa-bookmark edit-add-story-icon"></i>
            </div>
            <div className="story-edit-key">{storyInEditor.key}</div>
          </div>
          <div className="story-edit-input">
            <div className="story-edit-label">Title</div>
            <div className="story-edit-ta">
              <textarea
                value={storyInEditor.title}
                onChange={(e) => updateStoryInEditor("title", e.target.value)}
              ></textarea>
            </div>
          </div>
          <div className="story-edit-input">
            <div className="story-edit-label">Acceptance Criteria</div>
            <div className="story-edit-ta">
              <textarea
                value={storyInEditor.acceptanceCriteria}
                onChange={(e) =>
                  updateStoryInEditor("acceptanceCriteria", e.target.value)
                }
              ></textarea>
            </div>
          </div>
          <div className="story-edit-input">
            <div className="story-edit-label">Sprint</div>
            <div className="story-edit-ta">
              <select
                name="Sprint"
                value={storyInEditor.sprint}
                onChange={(e) => updateStoryInEditor("sprint", e.target.value)}
              >
                <option value="">Unassigned</option>
                {pi.sprints.map((val, idx) => {
                  return (
                    <option value={val.name} key={idx}>
                      {val.name}
                    </option>
                  );
                })}
              </select>
            </div>
          </div>
          <div className="story-edit-input">
            <div className="story-edit-label">Estimate</div>
            <div className="story-edit-ta">
              <input
                type="number"
                value={storyInEditor.estimate}
                disabled={
                  JSON.parse(localStorage.getItem("user"))["role"] ===
                  "ROLE_Scrum-Master"
                    ? false
                    : true
                }
                onChange={(e) =>
                  updateStoryInEditor("estimate", e.target.value)
                }
              ></input>
            </div>
          </div>
          <div className="flex-row-title">
            <div
              className={
                "btn-estimate" +
                ((storyInEditor.estimate === "" ||
                  storyInEditor.estimate === 0) &&
                storyInEditor.id !== ""
                  ? ""
                  : " closeds")
              }
              onClick={estimateStory}
            >
              Estimate
            </div>
            <div className="btn-save" onClick={saveStoryEdit}>
              Save
            </div>
            <div className="btn-cancel" onClick={cancelStoryEdit}>
              Cancel
            </div>
            <div
              className={
                "btn-delete" + (storyInEditor.key === "" ? "" : " closeds")
              }
              onClick={() => deleteStory(storyInEditor.id, storyInEditor.title)}
            >
              <i className="fa-solid fa-trash-can"></i>
            </div>
          </div>
        </div>
      </div>
      <div className="feature-detail-container">
        <div className="head-container">
          <div className="feature-big-icon">
            <i className="fa-solid fa-plus"></i>
          </div>
          <div className="feature-no">
            <Link
              to={{
                pathname: "https://dbatlas.db.com/jira02/browse/" + feature.id,
              }}
              target="_blank"
            >
              {feature.id}
            </Link>
          </div>
          <div className="feature-title">{feature.summary}</div>
        </div>
        <div className="item-header-feature">
          <div className="item-name-feature">Details</div>
          <div className="drawer-feature">
            <div
              className={
                "drawer-icon-feature " +
                (storyDetailsToggle ? "animate-drawer" : "")
              }
            >
              <i
                className="fa-solid fa-circle-chevron-down"
                onClick={toggleStoryDetails}
              ></i>
            </div>
          </div>
        </div>
        <div
          className={
            "plan-detail-container " + (storyDetailsToggle ? "closeds" : "")
          }
        >
          <div className="flex-1">
            <div className="label">Planning Assignee</div>
            <select
              name="Planning Assignee"
              id="planningAssignee"
              value={feature.planningAssignee}
              onChange={(e) => setNewValue(e)}
            >
              <option value="">Unassigned</option>
              {team
                .sort((m1, m2) => {
                  if (m1.name > m2.name) return 1;
                  else if (m1.name < m2.name) return -1;
                  else return 0;
                })
                .filter((mbr) => mbr.role === "ROLE_Team-Member")
                .map((val, idx) => {
                  return (
                    <option value={val.username} key={idx}>
                      {val.name}
                    </option>
                  );
                })}
            </select>
          </div>
          <div className="flex-1">
            <div className="label">Planning Date</div>
            <input
              type="date"
              id="discussionDt"
              onChange={(e) => setNewValue(e)}
              name="Planning Date"
              value={feature.discussionDt}
            />
          </div>
          <div className="flex-1">
            <div className="label">Sprint</div>
            <select
              name="Sprint"
              id="sprint"
              onChange={(e) => setNewValue(e)}
              value={feature.sprint}
            >
              <option value="">Unassigned</option>
              {pi.sprints.map((val, idx) => {
                return (
                  <option value={val.name} key={idx}>
                    {val.name}
                  </option>
                );
              })}
            </select>
          </div>
          <div className="flex-1">
            <div className="label">Estimate</div>
            <div className="estimate-value">{feature.estimate}</div>
          </div>
        </div>
        <div
          className={
            "plan-detail-container " + (storyDetailsToggle ? "closeds" : "")
          }
        >
          <div className="flex-1">
            <div className="label">Labels</div>
            <div className="label-holder">
              {feature.labels.split(",").map((val, idx) => {
                return (
                  <div className="labels" key={idx}>
                    {val}
                  </div>
                );
              })}
            </div>
          </div>
        </div>
        <div className="item-header-feature">
          <div className="item-name-feature">Acceptance Criteria</div>
          <div className="drawer-feature">
            <div
              className={
                "drawer-icon-feature " +
                (acceptanceCriteriaToggle ? "animate-drawer" : "")
              }
            >
              <i
                className="fa-solid fa-circle-chevron-down"
                onClick={toggleAcceptanceCriteria}
              ></i>
            </div>
          </div>
        </div>
        <div
          className={
            "plan-detail-container " +
            (acceptanceCriteriaToggle ? "closeds" : "")
          }
        >
          <div className="flex-3">
            <div className="label">Acceptance Criteria</div>
            <div className="description">
              <textarea
                className="textarea-desc"
                defaultValue={feature.acceptanceCriteria}
                readOnly
              ></textarea>
            </div>
          </div>
        </div>
        <div className="item-header-feature">
          <div className="item-name-feature">Description</div>
          <div className="drawer-feature">
            <div
              className={
                "drawer-icon-feature " +
                (descriptionDetailsToggle ? "animate-drawer" : "")
              }
            >
              <i
                className="fa-solid fa-circle-chevron-down"
                onClick={toggleDescriptionDetails}
              ></i>
            </div>
          </div>
        </div>
        <div
          className={
            "plan-detail-container " +
            (descriptionDetailsToggle ? "closeds" : "")
          }
        >
          <div className="flex-3">
            <div className="label">Description</div>
            <div className="description">
              <textarea
                className="textarea-desc"
                value={feature.description}
                id="description"
                onChange={(e) => setNewValue(e)}
              ></textarea>
            </div>
          </div>
        </div>
        <div className="item-header-feature">
          <div className="item-name-feature">Discussion/Planning Comments</div>
          <div className="drawer-feature">
            <div
              className={
                "drawer-icon-feature " +
                (discussionDetailsToggle ? "animate-drawer" : "")
              }
            >
              <i
                className="fa-solid fa-circle-chevron-down"
                onClick={toggleDiscussionDetails}
              ></i>
            </div>
          </div>
        </div>
        <div
          className={
            "plan-detail-container " +
            (discussionDetailsToggle ? "closeds" : "")
          }
        >
          <div className="flex-3">
            <div className="label">
              Discussion/
              <br />
              Comments/
              <br />
              Action Items
            </div>
            <div className="description">
              <textarea
                className="textarea-desc"
                value={feature.discussion}
                onChange={(e) => setNewValue(e)}
                id="discussion"
              ></textarea>
            </div>
          </div>
        </div>
        <div className="item-header-feature">
          <div className="item-name-feature">Stories</div>
          <div className="drawer-feature">
            <div
              className={
                "drawer-icon-feature " + (storiesToggle ? "animate-drawer" : "")
              }
            >
              <i
                className="fa-solid fa-circle-chevron-down"
                onClick={toggleStories}
              ></i>
            </div>
          </div>
        </div>
        <div className={"flex-story-btn " + (storiesToggle ? "closeds" : "")}>
          <div className="story-icon-holder">
            <div
              className={
                "add-story " + (feature.isPlanned === "Y" ? " invisible" : "")
              }
              onClick={addStory}
            >
              <span className="ic-content">
                <i className="fa-solid fa-bookmark add-story-icon"></i>
              </span>
            </div>
          </div>
          <div className="stories-container">
            {storiesInFeature.map((story, idx) => {
              return (
                <div
                  className="story"
                  id={idx}
                  key={idx}
                  onClick={() => openStoryEditor(story)}
                >
                  <div className="story-heading">
                    <div className="story-icon">
                      <i className="fa-solid fa-bookmark"></i>
                    </div>
                    <div className="story-title">{story.key}</div>
                  </div>
                  <div className="story-summary">{story.title}</div>
                  <div className="story-detail-holder">
                    <div className="story-sprint-value">{story.sprint}</div>
                    <div className="story-estimate-value">{story.estimate}</div>
                  </div>
                </div>
              );
            })}
          </div>
        </div>
        <div className="flex-4">
          <div className="planned-btn" onClick={() => saveFeature(null)}>
            Save
          </div>
          <div className="push-to-jira">Push to JIRA</div>
          <div
            className={
              "planned-btn " + (feature.isPlanned === "Y" ? " closeds" : "")
            }
            onClick={() => togglePlanned("Y")}
          >
            Mark as Planned!
          </div>
          <div
            className={
              "unplanned-btn " + (feature.isPlanned === "N" ? " closeds" : "")
            }
            onClick={() => togglePlanned("N")}
          >
            Mark as Unplanned!
          </div>
        </div>
      </div>
    </div>
  );
}

export default FeatureDetail;
