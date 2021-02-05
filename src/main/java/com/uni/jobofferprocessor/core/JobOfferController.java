package com.uni.jobofferprocessor.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ivelin.dimitrov
 */
@Controller
@RequestMapping("/api/mutliple")
public class JobOfferController {

    @Autowired
    JobOfferService jobOfferService;

    //TODO expose endpoints when service is done
}
