package com.uni.jobofferprocessor.joboffersources.zaplatabg;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZaplataBgCategoryParameter {

    Integer id;
    String description;

    @Override
    public String toString() {
        return "ZaplataBgCategoryParameter{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }
}
