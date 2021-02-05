package com.uni.jobofferprocessor.jobsbg;

import com.uni.jobofferprocessor.core.JobOffer;
import com.uni.jobofferprocessor.util.JobOfferError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author ivelin.dimitrov
 */
@Service
@Slf4j
public class JobsBgService {

    private final JobsBgRepository jobsBgRepository;

    private final List<JobsBgParameter> locationsList;

    private final List<JobsBgParameter> categoriesList;

    /**
     * Init and persist jobs bg locations and categories inmemory, executed on app start up
     *
     * @param jobsBgRepository
     */
    public JobsBgService(JobsBgRepository jobsBgRepository) {
        log.info("Fetching Jobs.bg available locatons.");
        locationsList = jobsBgRepository.findAllLocations();
        log.info("Fetching Jobs.bg available job categories.");
        categoriesList = jobsBgRepository.findAllCategories();
        log.info("Available Jobs.bg locations: " + locationsList.size());
        log.info("Available Jobs.bg job categories: " + categoriesList.size());
        this.jobsBgRepository = jobsBgRepository;
    }

    public List<JobOffer> findAllJobs(Integer size, Integer locationId, Integer categoryId) throws JobOfferError {
        Optional<JobsBgParameter> categoryIdFound = categoriesList
                .stream()
                .filter(it -> it.getId().equals(categoryId))
                .findAny();

        Optional<JobsBgParameter> locationIdFound = locationsList
                .stream()
                .filter(it -> it.getId().equals(locationId))
                .findAny();

        if (categoryIdFound.isEmpty()) {
            throw new JobOfferError("Category id is invalid. Received: " + categoryId);
        }
        if (locationIdFound.isEmpty()) {
            throw new JobOfferError("Location is invalid. Received: " + locationId);
        }
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
