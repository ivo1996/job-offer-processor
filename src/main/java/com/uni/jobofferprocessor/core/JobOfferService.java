package com.uni.jobofferprocessor.core;

import com.uni.jobofferprocessor.joboffersources.alobg.AloBgService;
import com.uni.jobofferprocessor.joboffersources.jobsbg.JobsBgService;
import com.uni.jobofferprocessor.joboffersources.olx.OlxService;
import com.uni.jobofferprocessor.joboffersources.zaplatabg.ZaplataBgService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author ivelin.dimitrov
 */
@Service
public class JobOfferService {

    JobsBgService jobsBgService;
    ZaplataBgService zaplataBgService;
    AloBgService aloBgService;
    OlxService olxService;
    Integer maxOffers;

    public JobOfferService(
            JobsBgService jobsBgService,
            ZaplataBgService zaplataBgService,
            AloBgService aloBgService,
            OlxService olxService,
            Environment env
    ) {
        this.aloBgService = aloBgService;
        this.jobsBgService = jobsBgService;
        this.olxService = olxService;
        this.zaplataBgService = zaplataBgService;
        this.maxOffers = Integer.parseInt(Objects.requireNonNull(env.getProperty("general.max-offers")));
    }

    //TODO implement parallel fetching from multiple sources
}
