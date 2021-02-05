package com.uni.jobofferprocessor.joboffersources.zaplatabg;

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
public class ZaplataBgService {

    private final ZaplataBgRepository zaplataBgRepository;

    private final List<ZaplataBgCategoryParameter> availableJobCategories;

    /**
     * Initializes on app startup all categories into an inmemory list
     *
     * @param zaplataBgRepository
     */
    public ZaplataBgService(ZaplataBgRepository zaplataBgRepository) {
        this.zaplataBgRepository = zaplataBgRepository;
        log.info("Fetching zaplata.bg available categories");
        this.availableJobCategories = zaplataBgRepository.getCategories();
        log.info("Available zaplata.bg categories: " + this.availableJobCategories.size());
    }

    /**
     * Returns preloaded list of locations
     * @return
     */
    public List<String> findAllLocations() {
        return zaplataBgRepository.findAllLocations();
    }

    /**
     * Returns preloaded list of categories
     * @return
     */
    public List<ZaplataBgCategoryParameter> findAllCategories() {
        return this.availableJobCategories;
    }

    /**
     * validates parameters and calls service to extract job offers
     * @param max
     * @param categoryId
     * @param locationName
     * @return
     * @throws JobOfferError
     */
    public List<JobOffer> findAllJobs(Integer max, Integer categoryId, String locationName) throws JobOfferError {
        Optional<ZaplataBgCategoryParameter> categoryIdFound = availableJobCategories
                .stream()
                .filter(it -> it.getId().equals(categoryId))
                .findAny();

        if (categoryIdFound.isEmpty()) {
            throw new JobOfferError("Category id is invalid. Received: " + categoryId);
        }
        if (!findAllLocations().contains(locationName)) {
            throw new JobOfferError("Location is invalid. Received: " + locationName);
        }
        if (max < 1) {
            throw new JobOfferError("Size is invalid. Received: " + max);
        }

        return zaplataBgRepository.getJobOffers(max, categoryId, locationName);
    }

}
