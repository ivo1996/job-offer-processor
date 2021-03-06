package com.uni.jobofferprocessor.joboffersources.olx;

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
@RequestMapping("/api/olx")
public class OlxController {

    @Autowired
    OlxService olxService;

    /**
     * Returns preloaded locations
     *
     * @return
     */
    @GetMapping("locations")
    public ResponseEntity<List<OlxLocation>> findAllLocations() {
        return new ResponseEntity<>(olxService.findAllLocations(), HttpStatus.OK);
    }

    /**
     * Returns all jobs filtered by locaiton parameter
     *
     * @param size
     * @param locationName
     * @return
     * @throws JobOfferError
     */
    @GetMapping()
    public ResponseEntity<List<JobOffer>> findAllJobs(
            @RequestParam(name = "size") Integer size,
            @RequestParam(name = "locationName") String locationName
    ) throws JobOfferError {
        return new ResponseEntity<>(olxService.findAllJobs(size, locationName), HttpStatus.OK);
    }
}
