package com.uni.jobofferprocessor.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

/**
 * @author ivelin.dimitrov
 */
@Controller
@RequestMapping("/api/multiple")
public class JobOfferController {

    @Autowired
    JobOfferService jobOfferService;

    @PostMapping
    public ResponseEntity<List<JobOffer>> getJobsFromMultipleSources(
            @Valid @RequestBody JobOfferRequest jobOfferRequest
    ) {
        return new ResponseEntity<>(jobOfferService.findJobsFromMultipleSources(jobOfferRequest), HttpStatus.OK);
    }
}
