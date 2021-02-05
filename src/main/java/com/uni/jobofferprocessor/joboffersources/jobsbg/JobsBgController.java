package com.uni.jobofferprocessor.joboffersources.jobsbg;

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
@RequestMapping("/api/jobsbg")
public class JobsBgController {

    @Autowired
    JobsBgService jobsBgService;

    /**
     * Finds all jobs bg offers by parameters
     * @param size
     * @param locationId
     * @param categoryId
     * @return 200 OK
     * @throws JobOfferError
     */
    @GetMapping()
    public ResponseEntity<List<JobOffer>> findAllJobs(
            @RequestParam(name = "size") Integer size,
            @RequestParam(name = "locationId") Integer locationId,
            @RequestParam(name = "categoryId") Integer categoryId
    ) throws JobOfferError {
        return new ResponseEntity<>(jobsBgService.findAllJobs(size, locationId, categoryId), HttpStatus.OK);
    }

    /**
     * Returns preloaded locations
     * @return
     */
    @GetMapping("/locations")
    public ResponseEntity<List<JobsBgParameter>> findAllLocations() {
        return new ResponseEntity<>(jobsBgService.findAllLocations(), HttpStatus.OK);
    }

    /**
     * Returns preloaded categories
     * @return
     */
    @GetMapping("/categories")
    public ResponseEntity<List<JobsBgParameter>> findAllCategories() {
        return new ResponseEntity<>(jobsBgService.findAllCategories(), HttpStatus.OK);
    }
}
