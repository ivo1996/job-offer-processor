package com.uni.jobofferprocessor.joboffersources.jobsbg;

import lombok.AllArgsConstructor;
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
public class JobsBgParameter {
    Integer id;
    String name;

    @Override
    public String toString() {
        return "JobsBgParameter{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
