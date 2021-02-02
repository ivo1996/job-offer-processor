package com.uni.jobofferprocessor.configration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ivelin.dimitrov
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "driver")
@NoArgsConstructor
public class DriverArguments {
    private List<String> arguments = new ArrayList<>();
}
