package com.uni.jobofferprocessor;

import com.uni.jobofferprocessor.joboffersources.zaplatabg.ZaplataBgRepository;
import com.uni.jobofferprocessor.joboffersources.zaplatabg.ZaplataBgService;
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
class ZaplataBgTests {

    @Autowired
    ZaplataBgService zaplataBgService;

    @Autowired
    ZaplataBgRepository zaplataBgRepository;

    @Test
    void getAllLocationsPositiveTest() {
        assertFalse(zaplataBgRepository.findAllLocations().isEmpty());
    }

    @Test
    void getAllCategoriesPositiveTest() {
        assertFalse(zaplataBgRepository.findAllCategories().isEmpty());
    }

    @Test
    void getAllJobsPositiveTest() throws JobOfferError {
        assertFalse(zaplataBgService.findAllJobs(20, 4, "sofia").isEmpty());
    }

    @Test
    void getJobsFromMultiplePagesNegativeValidationForSizeTest() {
        assertThrows(JobOfferError.class, () -> zaplataBgService.findAllJobs(-1, 3, "sofia"));
    }

    @Test
    void getJobsFromMultiplePagesNegativeValidationForCategoryTest() {
        assertThrows(JobOfferError.class, () -> zaplataBgService.findAllJobs(20, 0, "rousse"));
    }

    @Test
    void getJobsFromMultiplePagesNegativeValidationForLocationTest() {
        assertThrows(JobOfferError.class, () -> zaplataBgService.findAllJobs(20, 3, "fakeName"));
    }
}
