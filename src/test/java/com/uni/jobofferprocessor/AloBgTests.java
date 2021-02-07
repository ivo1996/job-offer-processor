package com.uni.jobofferprocessor;

import com.uni.jobofferprocessor.joboffersources.alobg.AloBgRepository;
import com.uni.jobofferprocessor.joboffersources.alobg.AloBgService;
import com.uni.jobofferprocessor.util.JobOfferError;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.annotation.Retention;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author ivelin.dimitrov
 */
@SpringBootTest
@Slf4j
class AloBgTests {

    @Autowired
    AloBgService aloBgService;

    @Autowired
    AloBgRepository aloBgRepository;

    @Test
    void getAllLocationsPositiveTest() {
        assertFalse(aloBgRepository.findAllLocations().isEmpty());
    }

    @Test
    void getAllCategoriesPositiveTest() {
        assertFalse(aloBgRepository.findAllCategories().isEmpty());
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
