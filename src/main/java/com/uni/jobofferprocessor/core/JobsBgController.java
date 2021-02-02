package com.uni.jobofferprocessor.core;

import com.uni.jobofferprocessor.core.jobsbg.JobsBgParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author ivelin.dimitrov
 */
@Controller
@RequestMapping("/api/jobsbg")
public class JobsBgController {

    @Autowired
    JobsBgService jobsBgService;

    @GetMapping("/locations")
    public ResponseEntity<List<JobsBgParameter>> findAllLocations() {
        return new ResponseEntity<>(jobsBgService.findAllLocations(), HttpStatus.OK);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<JobsBgParameter>> findAllCategories() {
        return new ResponseEntity<>(jobsBgService.findAllCategories(), HttpStatus.OK);
    }
}
