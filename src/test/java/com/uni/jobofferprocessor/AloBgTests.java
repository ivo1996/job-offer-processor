package com.uni.jobofferprocessor;

import com.uni.jobofferprocessor.joboffersources.alobg.AloBgService;
import com.uni.jobofferprocessor.util.JobOfferError;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author ivelin.dimitrov
 */
@SpringBootTest
class AloBgTests {

    @Autowired
    AloBgService aloBgService;

    @Test
    void getAllLocationsPositiveTest() {
        assertFalse(aloBgService.findAllLocations().isEmpty());
    }

    @Test
    void getAllCategoriesPositiveTest() {
        assertFalse(aloBgService.findAllCategories().isEmpty());
    }

    @Test
    void getAllJobsPositiveTest() throws JobOfferError {
        assertFalse(aloBgService.findAllJobs(20, 22, 3433).isEmpty());
    }

    @Test
    void getJobsFromMultiplePagesNegativeValidationForSizeTest() {
        assertThrows(JobOfferError.class, () -> aloBgService.findAllJobs(-1, 22, 3433));
    }

    @Test
    void getJobsFromMultiplePagesNegativeValidationForCategoryTest() {
        assertThrows(JobOfferError.class, () -> aloBgService.findAllJobs(20, -1, 3433));
    }

    @Test
    void getJobsFromMultiplePagesNegativeValidationForLocationTest() {
        assertThrows(JobOfferError.class, () -> aloBgService.findAllJobs(20, 22, -1));
    }
}
