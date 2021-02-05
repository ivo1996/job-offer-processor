package com.uni.jobofferprocessor.joboffersources.olx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ivelin.dimitrov
 */
@Service
@Slf4j
public class OlxService {

    private final List<OlxLocation> locationList;
    private final OlxRepository olxRepository;

    /**
     * Loads locations on application start up
     * @param olxRepository
     */
    public OlxService(OlxRepository olxRepository) {
        this.olxRepository = olxRepository;
        log.info("Fetching OLX available locatons.");
        locationList = this.olxRepository.findAllLocations();
        log.info("OLX available locations: " + locationList.size());
    }

    /**
     * Returns singleton preloaded list of locations
     * @return
     */
    public List<OlxLocation> findAllLocations() {
        return locationList;
    }
}
