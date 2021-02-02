package com.uni.jobofferprocessor.core;

import com.uni.jobofferprocessor.configration.SeleniumWebDriverConfiguration;
import com.uni.jobofferprocessor.core.jobsbg.JobsBgParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ivelin.dimitrov
 */
@Service
public class JobsBgService {

    @Autowired
    JobsBgRepository jobsBgRepository;

    /**
     * returns all locations from modal list in jobs bg
     * @return locations
     */
    public List<JobsBgParameter> findAllLocations() {
        return jobsBgRepository.findAllLocations();
    }

    /**
     * returns all categories from modal list in jobs bg
     * @return categories
     */
    public List<JobsBgParameter> findAllCategories() {
        return jobsBgRepository.findAllCategories();
    }
}
