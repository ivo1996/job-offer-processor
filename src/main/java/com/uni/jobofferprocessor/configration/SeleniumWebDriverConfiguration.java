package com.uni.jobofferprocessor.configration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * @author ivelin.dimitrov
 */
@Component
public class SeleniumWebDriverConfiguration {

    private final WebDriver driver;

    private final DriverArguments arguments;

    /**
     * Init chrome driver with arguments from application properties file
     */
    @Autowired
    public SeleniumWebDriverConfiguration(DriverArguments driverArguments) {
        this.arguments = driverArguments;
        driver = createNewDriver();
    }

    /**
     * create new webdriver instance
     * @return
     */
    private WebDriver createNewDriver() {
        ChromeOptions chromeOptions = new ChromeOptions();
        arguments.getArguments().forEach(chromeOptions::addArguments);
        WebDriver newDriver = new ChromeDriver(chromeOptions);
        newDriver.manage()
                .timeouts()
                .implicitlyWait(10, TimeUnit.SECONDS);
        return newDriver;
    }

    /**
     * return new webdriver instance
     * @return
     */
    public WebDriver getNewDriver() {
        return createNewDriver();
    }

    static {
        System.setProperty("webdriver.chrome.driver", findFile("chromedriver.exe"));
    }

    static private String findFile(String filename) {
        String paths[] = {"", "bin/", "target/classes"};
        for (String path : paths) {
            if (new File(path + filename).exists())
                return path + filename;
        }
        return "";
    }

    /**
     * close driver
     */
    public void close() {
        driver.close();
    }

    /**
     * navigate to url
     *
     * @param url
     */
    public void navigateTo(String url) {
        driver.navigate()
                .to(url);
    }

    /**
     * click an element
     *
     * @param element
     */
    public void clickElement(WebElement element) {
        element.click();
    }

    /**
     * get driver instance
     *
     * @return
     */
    public WebDriver getStaticDriver() {
        return driver;
    }
}
