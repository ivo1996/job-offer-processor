package com.uni.jobofferprocessor;

import com.uni.jobofferprocessor.joboffersources.olx.OlxRepository;
import com.uni.jobofferprocessor.joboffersources.olx.OlxService;
import com.uni.jobofferprocessor.util.JobOfferError;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author ivelin.dimitrov
 */
@SpringBootTest
@Slf4j
class OlxTests {

    @Autowired
    OlxService olxService;

    @Autowired
    OlxRepository olxRepository;

    @Test
    void getAllLocationsPositiveTest() {
        assertFalse(olxRepository.findAllLocations().isEmpty());
    }

    @Test
    void getAllJobOffersPositiveTest() throws JobOfferError {
        assertFalse(olxService.findAllJobs(40, "oblast-varna").isEmpty());
    }

    @Test
    void getJobsFromMultiplePagesNegativeValidationForSizeTest() {
        assertThrows(JobOfferError.class, () -> olxService.findAllJobs(-1, "oblast-varna"));
    }

    @Test
    void getJobsFromMultiplePagesNegativeValidationForLocationTest() {
        assertThrows(JobOfferError.class, () -> olxService.findAllJobs(15, "fake-oblast"));
    }
}
