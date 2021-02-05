package com.uni.jobofferprocessor.joboffersources.zaplatabg;

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
@RequestMapping("/api/zaplatabg")
public class ZaplataBgController {

    @Autowired
    ZaplataBgService zaplataBgService;

    /**
     * Returns specified number of offers and validates parameters
     * @param size
     * @param locationName
     * @param categoryId
     * @return
     * @throws JobOfferError
     */
    @GetMapping()
    public ResponseEntity<List<JobOffer>> getAllJobs(
            @RequestParam(name = "size") Integer size,
            @RequestParam(name = "locationName") String locationName,
            @RequestParam(name = "categoryId") Integer categoryId
    ) throws JobOfferError {
        return new ResponseEntity<>(zaplataBgService.findAllJobs(size, categoryId, locationName), HttpStatus.OK);
    }

    /**
     * Returns preloaded list of locations
     * @return
     */
    @GetMapping("/locations")
    public ResponseEntity<List<String>> findAllLocations() {
        return new ResponseEntity<>(zaplataBgService.findAllLocations(), HttpStatus.OK);
    }

    /**
     * Returns preloaded list of categories
     * @return
     */
    @GetMapping("/categories")
    public ResponseEntity<List<ZaplataBgCategoryParameter>> findAllCategories() {
        return new ResponseEntity<>(zaplataBgService.findAllCategories(), HttpStatus.OK);
    }
}
