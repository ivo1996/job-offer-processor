package com.uni.jobofferprocessor.core.general;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * @author ivelin.dimitrov
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class JobOffer {
    String referenceNumber;
    String description;
    String offerLink;
    String jobPosition;
    String location;
    Salary salary;
}
