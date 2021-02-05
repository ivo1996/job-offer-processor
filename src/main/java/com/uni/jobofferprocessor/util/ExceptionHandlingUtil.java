package com.uni.jobofferprocessor.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * @author ivelin.dimitrov
 */
@ControllerAdvice
public class ExceptionHandlingUtil {

    /**
     * Handles custom error that is manually thrown when validating
     * @param error
     * @return
     */
    @ExceptionHandler(value = {JobOfferError.class})
    protected ResponseEntity<Object> handleJobOfferError(JobOfferError error) {
        return new ResponseEntity<>(
                ExceptionBody.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .timestamp(new Timestamp(Calendar.getInstance().getTime().toInstant().toEpochMilli()))
                        .message(error.getMessage())
                        .build(),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles scenarios where endpoint parameter types are mismatched (ex. String is given to Integer field)
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return new ResponseEntity<>(
                ExceptionBody.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .timestamp(new Timestamp(Calendar.getInstance().getTime().toInstant().toEpochMilli()))
                        .message("Invalid parameter")
                        .details("Paremeter " + ex.getName() + " " + ex.getMostSpecificCause().getMessage())
                        .build(),
                HttpStatus.BAD_REQUEST);
    }
}
