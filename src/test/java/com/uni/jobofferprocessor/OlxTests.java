package com.uni.jobofferprocessor;

import com.uni.jobofferprocessor.joboffersources.olx.OlxLocation;
import com.uni.jobofferprocessor.joboffersources.olx.OlxRepository;
import com.uni.jobofferprocessor.joboffersources.olx.OlxService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author ivelin.dimitrov
 */
@SpringBootTest
class OlxTests {

    @Autowired
    OlxService olxService;

    @Test
    void getAllLocationsPositiveTest() {
        assertFalse(olxService.findAllLocations().isEmpty());
    }
}
