package com.uni.jobofferprocessor.joboffersources.jobsbg;

import com.uni.jobofferprocessor.core.JobOffer;
import com.uni.jobofferprocessor.util.JobOfferError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author ivelin.dimitrov
 */
@Service
@Slf4j
public class JobsBgService {

    private final Integer maxOffers;

    private final JobsBgRepository jobsBgRepository;

    private final List<JobsBgParameter> locationsList;

    private final List<JobsBgParameter> categoriesList;

    /**
     * Init and persist jobs bg locations and categories inmemory, executed on app start up
     *
     * @param jobsBgRepository
     */
    public JobsBgService(JobsBgRepository jobsBgRepository, Environment env) {
        log.info("Fetching Jobs.bg available locatons.");
        locationsList = jobsBgRepository.findAllLocations();
        log.info("Available Jobs.bg locations: " + locationsList.size());
        log.info("Fetching Jobs.bg available job categories.");
        categoriesList = jobsBgRepository.findAllCategories();
        log.info("Available Jobs.bg job categories: " + categoriesList.size());
        this.jobsBgRepository = jobsBgRepository;
        this.maxOffers = Integer.parseInt(Objects.requireNonNull(env.getProperty("general.max-offers")));
    }

    /**
     * Validate and call repository
     *
     * @param size
     * @param locationId
     * @param categoryId
     * @return
     * @throws JobOfferError
     */
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
        if (size < 1 || size >= maxOffers) {
            throw new JobOfferError("Size is invalid. Received: " + size);
        }
        String category = categoriesList.stream().filter(it -> it.getId().equals(categoryId)).findAny().get().getName();
        List<JobOffer> offers = jobsBgRepository.findAllJobs(size, locationId, categoryId);
        offers.forEach(it -> it.setJobPosition(category));
        return offers;
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
