package com.uni.jobofferprocessor;

import com.uni.jobofferprocessor.configuration.SeleniumWebDriverConfiguration;
import com.uni.jobofferprocessor.util.JobOfferError;
import com.uni.jobofferprocessor.zaplatabg.ZaplataBgRepository;
import com.uni.jobofferprocessor.zaplatabg.ZaplataBgService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author ivelin.dimitrov
 */
@SpringBootTest
class ZaplataBgTests {

    private static final String ZAPLATA_BG_HOST = "https://www.zaplata.bg/";

    @Autowired
    ZaplataBgRepository zaplataBgRepository;

    @Autowired
    ZaplataBgService zaplataBgService;

    @Autowired
    SeleniumWebDriverConfiguration seleniumWebDriverConfiguration;

    @Test
    void getAllLocationsPositiveTest() {
        assertFalse(zaplataBgService.findAllLocations().isEmpty());
    }

    @Test
    void getAllCategoriesPositiveTest() {
        assertFalse(zaplataBgService.findAllCategories().isEmpty());
    }

    @Test
    void getAllJobsPositiveTest() throws JobOfferError {
        assertFalse(zaplataBgService.findAllJobs(20, 4, "sofia").isEmpty());
    }

    @Test
    void getJobsFromMultiplePagesNegativeValidationForSizeTest() throws JobOfferError {
        assertThrows(JobOfferError.class, () -> zaplataBgService.findAllJobs(-1, 3, "sofia"));
    }

    @Test
    void getJobsFromMultiplePagesNegativeValidationForCategoryTest() throws JobOfferError {
        assertThrows(JobOfferError.class, () -> zaplataBgService.findAllJobs(20, 0, "rousse"));
    }

    @Test
    void getJobsFromMultiplePagesNegativeValidationForLocationTest() throws JobOfferError {
        assertThrows(JobOfferError.class, () -> zaplataBgService.findAllJobs(20, 3, "fakeName"));
    }


}
