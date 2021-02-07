package com.uni.jobofferprocessor.joboffersources.alobg;

import com.uni.jobofferprocessor.core.JobOffer;
import com.uni.jobofferprocessor.util.JobOfferError;
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
@RequestMapping("api/alobg")
public class AloBgController {

    @Autowired
    AloBgService service;

    @GetMapping("/locations")
    public ResponseEntity<List<AloBgParameter>> getAllLocations() {
        return new ResponseEntity<>(service.findAllLocations(), HttpStatus.OK);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<AloBgParameter>> getAllCategories() {
        return new ResponseEntity<>(service.findAllCategories(), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<JobOffer>> findAllJobs(
            @RequestParam(name = "size") Integer size,
            @RequestParam(name = "locationId") Integer locationId,
            @RequestParam(name = "categoryId") Integer categoryId
    ) throws JobOfferError {
        return new ResponseEntity<>(service.findAllJobs(size, locationId, categoryId), HttpStatus.OK);
    }
}
