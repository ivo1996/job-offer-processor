package com.uni.jobofferprocessor.joboffersources.olx;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ivelin.dimitrov
 */
@Getter
@AllArgsConstructor
@Builder
public class OlxLocation {
    String dataId;
    String name;
}
