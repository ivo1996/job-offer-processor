package com.uni.jobofferprocessor;

import com.uni.jobofferprocessor.configuration.SeleniumWebDriverConfiguration;
import com.uni.jobofferprocessor.util.JobOfferError;
import com.uni.jobofferprocessor.zaplatabg.ZaplataBgRepository;
import com.uni.jobofferprocessor.zaplatabg.ZaplataBgService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;

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
    void getAllLocations() {
        assertFalse(zaplataBgService.findAllLocations().isEmpty());
    }

    @Test
    void getAllCategories() {
        assertFalse(zaplataBgService.findAllCategories().isEmpty());
    }

    @Test
    void getAllJobs() throws JobOfferError {
        assertFalse(zaplataBgService.getAllJobs(20, 4, "sofia").isEmpty());
    }


}
