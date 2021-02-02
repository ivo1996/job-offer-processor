package com.uni.jobofferprocessor;

import com.uni.jobofferprocessor.configration.SeleniumWebDriverConfiguration;
import com.uni.jobofferprocessor.core.JobsBgRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class JobOfferProcessorApplicationTests {

    @Autowired
    JobsBgRepository jobsBgRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void getCategories() {
        assertFalse(jobsBgRepository.findAllCategories().isEmpty());
    }

    @Test
    void getTowns() {
        assertFalse(jobsBgRepository.findAllLocations().isEmpty());
    }

}
