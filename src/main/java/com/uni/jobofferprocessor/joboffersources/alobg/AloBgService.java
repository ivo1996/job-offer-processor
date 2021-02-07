package com.uni.jobofferprocessor.joboffersources.alobg;

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
public class AloBgService {

    private final Integer maxOffers;
    private final AloBgRepository aloBgRepository;
    private final List<AloBgParameter> locationsList;
    private final List<AloBgParameter> categoriesList;

    /**
     * preloads locations and categories, injects repository and fetches application config property
     *
     * @param aloBgRepository
     * @param env
     */
    public AloBgService(
            AloBgRepository aloBgRepository,
            Environment env
    ) {
        this.aloBgRepository = aloBgRepository;
        log.info("Fetching Alo.bg available locatons.");
        locationsList = this.aloBgRepository.findAllLocations();
        log.info("Available Alo.bg locations: " + locationsList.size());
        log.info("Fetching Alo.bg available job categories.");
        categoriesList = this.aloBgRepository.findAllCategories();
        log.info("Available Alo.bg job categories: " + categoriesList.size());
        this.maxOffers = Integer.parseInt(Objects.requireNonNull(env.getProperty("general.max-offers")));
    }

    /**
     * returns the preloaded list with locations
     *
     * @return
     */
    public List<AloBgParameter> findAllLocations() {
        return locationsList;
    }

    /**
     * returns the preloaded list with categories
     *
     * @return
     */
    public List<AloBgParameter> findAllCategories() {
        return categoriesList;
    }

    /**
     * validates input for location, category and size, calls the repository
     *
     * @param size
     * @param locationId
     * @param categoryId
     * @return
     * @throws JobOfferError
     */
    public List<JobOffer> findAllJobs(Integer size, Integer locationId, Integer categoryId) throws JobOfferError {
        Optional<AloBgParameter> categoryIdFound = categoriesList
                .stream()
                .filter(it -> it.getId().equals(categoryId))
                .findAny();

        Optional<AloBgParameter> locationIdFound = locationsList
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
        return aloBgRepository.findAllJobsFromAllPages(size, locationId, categoryId);
    }
}
