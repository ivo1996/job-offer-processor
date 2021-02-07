package com.uni.jobofferprocessor.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.util.Map;

/**
 * The response structure on error
 *
 * @author ivelin.dimitrov
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionBody {
    HttpStatus status;
    Timestamp timestamp;
    String message;
    String details;
    Map<String, String> fields;
}


