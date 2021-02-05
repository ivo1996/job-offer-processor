package com.uni.jobofferprocessor.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ivelin.dimitrov
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobOffer {
    String referenceNumber;
    String description;
    String offerLink;
    String jobPosition;
    String location;
    String salary;

    @Override
    public String toString() {
        return "JobOffer{" +
                "referenceNumber='" + referenceNumber + '\'' +
                ", description='" + description + '\'' +
                ", offerLink='" + offerLink + '\'' +
                ", jobPosition='" + jobPosition + '\'' +
                ", location='" + location + '\'' +
                ", salary='" + salary + '\'' +
                '}';
    }
}
