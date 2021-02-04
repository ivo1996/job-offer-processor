package com.uni.jobofferprocessor;

import com.uni.jobofferprocessor.configuration.SeleniumWebDriverConfiguration;
import com.uni.jobofferprocessor.core.JobOffer;
import com.uni.jobofferprocessor.jobsbg.JobsBgRepository;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static com.uni.jobofferprocessor.jobsbg.JobsBgRepository.JOBS_BG_HOST;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class JobOfferProcessorApplicationTests {

    @Autowired
    JobsBgRepository jobsBgRepository;

    @Autowired
    SeleniumWebDriverConfiguration seleniumWebDriverConfiguration;

    @Test
    void getJobsBgCategories() {
        assertFalse(jobsBgRepository.findAllCategories().isEmpty());
    }

    @Test
    void getJobsBgTowns() {
        assertFalse(jobsBgRepository.findAllLocations().isEmpty());
    }

    @Test
    void getJobsFromSinglePage() {
        WebDriver driver = seleniumWebDriverConfiguration.getStaticDriver();
        driver.get(JOBS_BG_HOST + 0 + "&add_sh=1&categories%5B0%5D=" + 56 + "&location_sid=" + 3);
        List<JobOffer> offerList = new ArrayList<>(jobsBgRepository.getJobsFromPage(driver));
        offerList.stream()
                .filter(jobOffer -> jobOffer.getReferenceNumber() != null)
                .filter(jobOffer -> !jobOffer.getReferenceNumber().isBlank())
                .forEach(System.out::println);

        assertFalse(offerList.isEmpty());
    }

    @Test
    void getJobsFromMultiplePages() {
        List<JobOffer> jobOffers = jobsBgRepository.findAllJobs(200, 3, 56);
        System.out.println(jobOffers.size());
        assertFalse(jobOffers.isEmpty());
    }

}
