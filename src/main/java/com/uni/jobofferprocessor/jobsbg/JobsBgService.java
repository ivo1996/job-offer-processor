package com.uni.jobofferprocessor.jobsbg;

import com.uni.jobofferprocessor.core.general.JobOffer;
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

    public List<JobOffer> findAllJobs(Integer size, Integer locationId, Integer categoryId) {
        return jobsBgRepository.findAllJobs(size, locationId, categoryId);
    }

    /**
     * returns all locations from modal list in jobs bg
     *
     * @return locations
     */
    public List<JobsBgParameter> findAllLocations() {
        return jobsBgRepository.findAllLocations();
    }

    /**
     * returns all categories from modal list in jobs bg
     *
     * @return categories
     */
    public List<JobsBgParameter> findAllCategories() {
        return jobsBgRepository.findAllCategories();
    }
}
