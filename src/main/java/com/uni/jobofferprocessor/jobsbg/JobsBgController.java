package com.uni.jobofferprocessor.jobsbg;

import com.uni.jobofferprocessor.core.general.JobOffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author ivelin.dimitrov
 */
@Controller
@RequestMapping("/api/jobsbg")
public class JobsBgController {

    @Autowired
    JobsBgService jobsBgService;

    @GetMapping()
    public ResponseEntity<List<JobOffer>> findAllJobs(
            @RequestParam(name = "size") Integer size,
            @RequestParam(name = "locationId") Integer locationId,
            @RequestParam(name = "categoryId") Integer categoryId
    ) {
        return new ResponseEntity<>(jobsBgService.findAllJobs(size, locationId, categoryId), HttpStatus.OK);
    }

    @GetMapping("/locations")
    public ResponseEntity<List<JobsBgParameter>> findAllLocations() {
        return new ResponseEntity<>(jobsBgService.findAllLocations(), HttpStatus.OK);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<JobsBgParameter>> findAllCategories() {
        return new ResponseEntity<>(jobsBgService.findAllCategories(), HttpStatus.OK);
    }
}
