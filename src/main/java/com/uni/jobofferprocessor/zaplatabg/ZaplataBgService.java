package com.uni.jobofferprocessor.zaplatabg;

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
        log.info("Fetching zaplata.bg categories");
        this.availableJobCategories = zaplataBgRepository.getCategories();
        log.info("Available zaplata.bg categories: " + this.availableJobCategories.size());
    }

    public List<String> findAllLocations() {
        return zaplataBgRepository.findAllLocations();
    }

    public List<ZaplataBgCategoryParameter> findAllCategories() {
        return this.availableJobCategories;
    }

    public List<JobOffer> getAllJobs(Integer max, Integer categoryId, String locationName) throws JobOfferError {
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

        return zaplataBgRepository.getJobOffers(max, categoryId, locationName);
    }

}
