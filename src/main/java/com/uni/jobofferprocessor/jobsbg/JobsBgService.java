package com.uni.jobofferprocessor.jobsbg;

import com.uni.jobofferprocessor.core.JobOffer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ivelin.dimitrov
 */
@Service
@Slf4j
public class JobsBgService {

    JobsBgRepository jobsBgRepository;

    private final List<JobsBgParameter> locationsList;

    private final List<JobsBgParameter> categoriesList;

    /**
     * Init and persist jobs bg locations and categories inmemory, executed on app start up
     * @param jobsBgRepository
     */
    @Autowired
    public JobsBgService(JobsBgRepository jobsBgRepository) {
        assert false;
        log.info("Fetching Jobs.bg available locatons.");
        locationsList= jobsBgRepository.findAllLocations();
        log.info("Fetching Jobs.bg available job categories.");
        categoriesList = jobsBgRepository.findAllCategories();
        log.info("Available Jobs.bg locations: " + locationsList.size());
        log.info("Available Jobs.bg job categories: " + categoriesList.size());
    }

    public List<JobOffer> findAllJobs(Integer size, Integer locationId, Integer categoryId) {
        return jobsBgRepository.findAllJobs(size, locationId, categoryId);
    }

    /**
     * returns all locations from modal list in jobs bg
     *
     * @return locations
     */
    public List<JobsBgParameter> findAllLocations() {
        return locationsList;
    }

    /**
     * returns all categories from modal list in jobs bg
     *
     * @return categories
     */
    public List<JobsBgParameter> findAllCategories() {
        return categoriesList;
    }
}
