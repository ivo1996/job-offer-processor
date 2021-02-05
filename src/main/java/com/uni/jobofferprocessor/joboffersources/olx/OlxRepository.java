package com.uni.jobofferprocessor.joboffersources.olx;

import com.uni.jobofferprocessor.configuration.SeleniumWebDriverConfiguration;
import com.uni.jobofferprocessor.core.JobOffer;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author ivelin.dimitrov
 */
@Repository
public class OlxRepository {

    private final SeleniumWebDriverConfiguration seleniumWebDriverConfiguration;
    private final Integer timeout;

    private static final String OLX_HOST = "https://www.olx.bg/rabota/";

    /**
     * Injects selenium config and fetches timout property from configuration
     * @param seleniumWebDriverConfiguration
     * @param env
     */
    public OlxRepository(SeleniumWebDriverConfiguration seleniumWebDriverConfiguration, Environment env) {
        this.seleniumWebDriverConfiguration = seleniumWebDriverConfiguration;
        this.timeout = Integer.parseInt(Objects.requireNonNull(env.getProperty("driver.timeoutInSeconds")));
    }

    /**
     * Waits for stupid gdpr consent button and fetches all locations
     * @return
     */
    public List<OlxLocation> findAllLocations() {
        List<OlxLocation> locations = new ArrayList<>();
        WebDriver driver = seleniumWebDriverConfiguration.getStaticDriver();
        WebDriverWait block = new WebDriverWait(driver, this.timeout);
        driver.get(OLX_HOST);
        block.until(ExpectedConditions.visibilityOfElementLocated(By.id("onetrust-accept-btn-handler")));
        driver.findElement(By.id("onetrust-accept-btn-handler")).click();
        block.until(ExpectedConditions.visibilityOfElementLocated(By.id("cityField")));
        driver.findElement(By.id("cityField")).click();
        driver.findElement(By.cssSelector("#proposalContainer > div.abs.categorySelectContainer > ul"))
                .findElements(By.cssSelector("li"))
                .parallelStream()
                .forEach(region -> {
                    try {
                        WebElement anchorTag = region.findElement(By.cssSelector("a"));
                        locations.add(
                                OlxLocation.builder()
                                        .dataId(anchorTag.getAttribute("data-url"))
                                        .name(anchorTag.getAttribute("data-name"))
                                        .build()
                        );
                    } catch (NoSuchElementException ignored) {
                    }
                });

        return locations;
    }

    /**
     * Fetches all locations by parameters, parallel processing both for individual offers and pages
     * @param maxSize
     * @param location
     * @return
     */
    public List<JobOffer> findAllJobs(Integer maxSize, String location) {
        List<JobOffer> offerList = new ArrayList<>();
        WebDriver driver = seleniumWebDriverConfiguration.getStaticDriver();
        WebDriverWait block = new WebDriverWait(driver, this.timeout);
        driver.get(OLX_HOST);
        block.until(ExpectedConditions.visibilityOfElementLocated(By.id("onetrust-accept-btn-handler")));
        driver.findElement(By.id("onetrust-accept-btn-handler")).click();
        block.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#offers_table > tbody")));
        driver.findElement(By.cssSelector("#offers_table > tbody"))
                .findElements(By.className("wrap"))
                .parallelStream()
                .forEach(offer -> {
                    WebElement internalTable = offer.findElement(By.cssSelector("td > div > table"));

                    //TODO complete logic, extract and implement parallel page iteration
                });
        return offerList;
    }
}
