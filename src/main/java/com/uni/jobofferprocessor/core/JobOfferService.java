package com.uni.jobofferprocessor.core;

import com.uni.jobofferprocessor.joboffersources.jobsbg.JobsBgService;
import com.uni.jobofferprocessor.joboffersources.zaplatabg.ZaplataBgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ivelin.dimitrov
 */
@Service
public class JobOfferService {

    @Autowired
    JobsBgService jobsBgService;

    @Autowired
    ZaplataBgService zaplataBgService;

    //TODO implement parallel fetching from multiple sources
}
