package com.uni.jobofferprocessor.util;

/**
 * Custom exception that is called on validation sites
 * @author ivelin.dimitrov
 */
public class JobOfferError extends Throwable {
    public JobOfferError(String message) {
        super(message);
    }
}
