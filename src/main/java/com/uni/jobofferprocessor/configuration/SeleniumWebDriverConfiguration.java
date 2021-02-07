package com.uni.jobofferprocessor.configuration;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.File;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author ivelin.dimitrov
 */
@Component
@Slf4j
public class SeleniumWebDriverConfiguration {

    private final DriverArguments arguments;

    private final Integer timeout;

    /**
     * Init chrome driver with arguments from application properties file
     */
    @Autowired
    public SeleniumWebDriverConfiguration(DriverArguments driverArguments, Environment env) {
        this.arguments = driverArguments;
        this.timeout = Integer.parseInt(Objects.requireNonNull(env.getProperty("driver.timeoutInSeconds")));
    }

    /**
     * create new webdriver instance
     *
     * @return
     */
    private WebDriver createNewDriver() {
        ChromeOptions chromeOptions = new ChromeOptions();
        arguments.getArguments().forEach(chromeOptions::addArguments);
        WebDriver newDriver = new ChromeDriver(chromeOptions);
        newDriver.manage()
                .timeouts()
                .implicitlyWait(timeout, TimeUnit.SECONDS);
        return newDriver;
    }

    /**
     * return new webdriver instance
     *
     * @return
     */
    public WebDriver getNewDriver() {
        return createNewDriver();
    }


    /**
     * configure location of webdriver executable file
     */
    static {
        System.setProperty("webdriver.chrome.driver", findFile("chromedriver.exe"));
        System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");

    }

    /**
     * Used to find the webdriver file in hardcoded but common directiories
     *
     * @param filename
     * @return
     */
    static private String findFile(String filename) {
        String paths[] = {"", "bin/", "target/classes"};
        for (String path : paths) {
            if (new File(path + filename).exists())
                return path + filename;
        }
        return "";
    }
}
