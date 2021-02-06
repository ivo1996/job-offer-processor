package com.uni.jobofferprocessor;

import com.uni.jobofferprocessor.configuration.SeleniumWebDriverConfiguration;
import com.uni.jobofferprocessor.core.JobOffer;
import com.uni.jobofferprocessor.joboffersources.jobsbg.JobsBgRepository;
import com.uni.jobofferprocessor.joboffersources.jobsbg.JobsBgService;
import com.uni.jobofferprocessor.util.JobOfferError;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class JobsBgTests {

    @Autowired
    JobsBgRepository jobsBgRepository;

    @Autowired
    JobsBgService jobsBgService;

    @Autowired
    SeleniumWebDriverConfiguration seleniumWebDriverConfiguration;

    @Test
    void getJobsBgCategoriesPositiveTest() {
        assertFalse(jobsBgService.findAllCategories().isEmpty());
    }

    @Test
    void getJobsBgTownsPositiveTest() {
        assertFalse(jobsBgService.findAllLocations().isEmpty());
    }

    @Test
    void getJobsFromSinglePagePositiveTest() {
        WebDriver driver = seleniumWebDriverConfiguration.getStaticDriver();
        driver.get(jobsBgRepository.getHost() + 0 + "&add_sh=1&categories%5B0%5D=" + 56 + "&location_sid=" + 3);
        List<JobOffer> offerList = new ArrayList<>(jobsBgRepository.getJobsFromPage(driver));
        offerList = offerList.stream()
                .filter(jobOffer -> jobOffer.getReferenceNumber() != null)
                .filter(jobOffer -> !jobOffer.getReferenceNumber().isBlank())
                .collect(Collectors.toList());

        assertFalse(offerList.isEmpty());
    }

    @Test
    void getJobsFromMultiplePagesPositiveTest() throws JobOfferError {
        List<JobOffer> jobOffers = jobsBgService.findAllJobs(15, 3, 56);
        assertFalse(jobOffers.isEmpty());
    }

    @Test
    void getJobsFromMultiplePagesNegativeValidationForSizeTest() {
        assertThrows(JobOfferError.class, () -> jobsBgService.findAllJobs(-1, 3, 56));
    }

    @Test
    void getJobsFromMultiplePagesNegativeValidationForLocationTest() {
        assertThrows(JobOfferError.class, () -> jobsBgService.findAllJobs(15, 0, 56));
    }

    @Test
    void getJobsFromMultiplePagesNegativeValidationForCategoryTest() {
        assertThrows(JobOfferError.class, () -> jobsBgService.findAllJobs(15, 3, 200000));
    }


}
