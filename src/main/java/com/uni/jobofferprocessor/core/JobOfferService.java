package com.uni.jobofferprocessor.core;

import com.uni.jobofferprocessor.joboffersources.alobg.AloBgService;
import com.uni.jobofferprocessor.joboffersources.jobsbg.JobsBgService;
import com.uni.jobofferprocessor.joboffersources.olx.OlxService;
import com.uni.jobofferprocessor.joboffersources.zaplatabg.ZaplataBgService;
import com.uni.jobofferprocessor.util.JobOfferError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ivelin.dimitrov
 */
@Service
@Slf4j
public class JobOfferService {

    private final JobsBgService jobsBgService;
    private final ZaplataBgService zaplataBgService;
    private final AloBgService aloBgService;
    private final OlxService olxService;

    /**
     * Injects all services
     *
     * @param jobsBgService
     * @param zaplataBgService
     * @param aloBgService
     * @param olxService
     */
    public JobOfferService(
            JobsBgService jobsBgService,
            ZaplataBgService zaplataBgService,
            AloBgService aloBgService,
            OlxService olxService
    ) {
        this.aloBgService = aloBgService;
        this.jobsBgService = jobsBgService;
        this.olxService = olxService;
        this.zaplataBgService = zaplataBgService;
    }

    /**
     * Using reflection to determine available fields from the request
     * Calling services in a parallel manner
     *
     * @param jobOfferRequest
     * @return
     */
    public List<JobOffer> findJobsFromMultipleSources(JobOfferRequest jobOfferRequest) {
        List<JobOffer> offerList = new ArrayList<>();

        Arrays.stream(jobOfferRequest.getClass().getDeclaredFields()).parallel().forEach(field -> {
            field.setAccessible(true);
            try {
                if (field.get(jobOfferRequest) != null) {
                    switch (field.getName()) {
                        case "aloBg":
                            log.info("Fetching job offers from aloBg");
                            offerList.addAll(aloBgService.findAllJobs(
                                    jobOfferRequest.getAloBg().getSize(),
                                    jobOfferRequest.getAloBg().getLocationId(),
                                    jobOfferRequest.getAloBg().getCategoryId()
                                    )
                            );
                            log.info("Finished fetching job offers from aloBg");
                            break;
                        case "zaplataBg":
                            log.info("Fetching job offers from zaplataBg");
                            offerList.addAll(zaplataBgService.findAllJobs(
                                    jobOfferRequest.getZaplataBg().getSize(),
                                    jobOfferRequest.getZaplataBg().getCategoryId(),
                                    jobOfferRequest.getZaplataBg().getLocationName()
                                    )
                            );
                            log.info("Finished fetching job offers from zaplataBg");
                            break;
                        case "jobsBg":
                            log.info("Fetching job offers from jobsBg");
                            offerList.addAll(jobsBgService.findAllJobs(
                                    jobOfferRequest.getJobsBg().getSize(),
                                    jobOfferRequest.getJobsBg().getLocationId(),
                                    jobOfferRequest.getJobsBg().getCategoryId()
                                    )
                            );
                            log.info("Finished fetching job offers from jobsBg");
                            break;
                        case "olx":
                            log.info("Fetching job offers from olx");
                            offerList.addAll(olxService.findAllJobs(
                                    jobOfferRequest.getOlx().getSize(),
                                    jobOfferRequest.getOlx().getLocationName()
                                    )
                            );
                            log.info("Finished fetching job offers from olx");
                            break;
                    }
                }
            } catch (IllegalAccessException | JobOfferError ignored) {
            }
        });

        return offerList;
    }
}
