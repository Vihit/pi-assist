package com.digitedgy.piassist.controller;

import com.digitedgy.piassist.entity.Feature;
import com.digitedgy.piassist.entity.PI;
import com.digitedgy.piassist.entity.User;
import com.digitedgy.piassist.service.FeatureService;
import com.digitedgy.piassist.service.PIService;
import com.digitedgy.piassist.service.UserService;
import com.digitedgy.piassist.util.ExcelRow;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping(path = "/report")
public class ReportController {

    @Autowired
    FeatureService featureService;

    @Autowired
    UserService userService;

    @Autowired
    PIService piService;

    @GetMapping(path = "/download/{team}")
    public ResponseEntity<?> downloadReport(@PathVariable String team) throws IOException {
        Date now = new Date();
        String fileNameSuffix = now.getDate() + "" +now.getMonth()+""+now.getYear()+""+now.getHours()+""+now.getMinutes();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Iterable<Feature> features = featureService.getAllFeaturesForATeam(team);
        Iterable<User> users = userService.findAllByTeam(team);
        Optional<PI> pi = piService.findOpenPIForTeam(team);
        Workbook workbook = new XSSFWorkbook();
        workbook = ExcelRow.fromFeature(workbook, features);
        workbook = ExcelRow.fromUsers(workbook, users, pi);
        workbook.write(out);
        InputStreamResource inputStreamResource = new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+"PI_Planning_Report_"+fileNameSuffix+".xlsx")
                .contentLength(out.toByteArray().length).contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(inputStreamResource);
    }
}
