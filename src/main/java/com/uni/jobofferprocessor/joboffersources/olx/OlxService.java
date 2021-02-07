package com.uni.jobofferprocessor.joboffersources.olx;

import com.uni.jobofferprocessor.core.JobOffer;
import com.uni.jobofferprocessor.joboffersources.jobsbg.JobsBgParameter;
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
public class OlxService {

    private final List<OlxLocation> locationList;
    private final OlxRepository olxRepository;
    private final Integer maxOffers;

    /**
     * Loads locations on application start up
     *
     * @param olxRepository
     */
    public OlxService(
            OlxRepository olxRepository,
            Environment env
    ) {
        this.olxRepository = olxRepository;
        log.info("Fetching OLX available locatons.");
        locationList = this.olxRepository.findAllLocations();
        log.info("OLX available locations: " + locationList.size());
        this.maxOffers = Integer.parseInt(Objects.requireNonNull(env.getProperty("general.max-offers")));
    }

    /**
     * Returns singleton preloaded list of locations
     *
     * @return
     */
    public List<OlxLocation> findAllLocations() {
        return locationList;
    }

    /**
     * Validates input for maxsize and location and calls the repository to fetch the desired data
     *
     * @param maxSize
     * @param location
     * @return
     * @throws JobOfferError
     */
    public List<JobOffer> findAllJobs(Integer maxSize, String location) throws JobOfferError {
        Optional<OlxLocation> locationIdFound = locationList
                .stream()
                .filter(it -> it.getDataId().equals(location))
                .findAny();

        if (locationIdFound.isEmpty()) {
            throw new JobOfferError("Location is invalid. Received: " + location);
        }
        if (maxSize < 1 || maxSize >= maxOffers) {
            throw new JobOfferError("Size is invalid. Received: " + maxSize);
        }
        return olxRepository.findAllJobs(maxSize, location);
    }
}
