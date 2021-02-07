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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author ivelin.dimitrov
 */
@Repository
public class OlxRepository {

    private final SeleniumWebDriverConfiguration seleniumWebDriverConfiguration;
    private final Integer timeout;
    private final String offersSelector;
    private final String locationsSelector;
    private final String host;

    /**
     * Injects selenium config and fetches timout property from configuration
     *
     * @param seleniumWebDriverConfiguration
     * @param env
     */
    public OlxRepository(
            SeleniumWebDriverConfiguration seleniumWebDriverConfiguration,
            Environment env
    ) {
        this.seleniumWebDriverConfiguration = seleniumWebDriverConfiguration;
        this.timeout = Integer.parseInt(Objects.requireNonNull(env.getProperty("driver.timeoutInSeconds")));
        this.host = Objects.requireNonNull(env.getProperty("olx.url"));
        this.offersSelector = Objects.requireNonNull(env.getProperty("olx.offers-selector"));
        this.locationsSelector = Objects.requireNonNull(env.getProperty("olx.locations-selector"));
    }

    /**
     * Waits for stupid gdpr consent button and fetches all locations
     *
     * @return
     */
    public List<OlxLocation> findAllLocations() {
        List<OlxLocation> locations = new ArrayList<>();
        WebDriver driver = seleniumWebDriverConfiguration.getNewDriver();
        WebDriverWait block = new WebDriverWait(driver, this.timeout);
        driver.get(host);
        block.until(ExpectedConditions.visibilityOfElementLocated(By.id("onetrust-accept-btn-handler")));
        driver.findElement(By.id("onetrust-accept-btn-handler")).click();
        block.until(ExpectedConditions.visibilityOfElementLocated(By.id("cityField")));
        driver.findElement(By.id("cityField")).click();
        driver.findElement(By.cssSelector(locationsSelector))
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
        driver.close();
        return locations.stream().filter(it -> it.getDataId() != null).collect(Collectors.toList());
    }

    /**
     * parallel processing both for individual offers and pages
     *
     * @param maxSize
     * @param location
     * @return
     */
    public List<JobOffer> findAllJobs(Integer maxSize, String location) {
        List<JobOffer> offerList = new ArrayList<>();
        IntStream.range(1, maxSize / 40 + 1).parallel().forEach(idx -> {
            WebDriver driver = seleniumWebDriverConfiguration.getNewDriver();
            WebDriverWait block = new WebDriverWait(driver, this.timeout);
            driver.get(host + "/" + location + "/" + "?page=" + idx);
            offerList.addAll(getJobsFromSinglePage(driver, block));
            driver.close();
        });
        return offerList;
    }

    /**
     * Process a single page with offers
     *
     * @param driver
     * @param block
     * @return
     */
    private List<JobOffer> getJobsFromSinglePage(WebDriver driver, WebDriverWait block) {
        List<JobOffer> offers = new ArrayList<>();
        block.until(ExpectedConditions.visibilityOfElementLocated(By.id("onetrust-accept-btn-handler")));
        driver.findElement(By.id("onetrust-accept-btn-handler")).click();
        block.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(offersSelector)));
        driver.findElement(By.cssSelector(offersSelector))
                .findElements(By.className("wrap"))
                .parallelStream()
                .forEach(offer -> {
                    WebElement header = offer.findElement(By.cssSelector("td > div > table > tbody > tr:nth-child(1) > td.title-cell.title-cell--jobs > div "));
                    JobOffer jobOffer = JobOffer.builder()
                            .offerLink(header.findElement(By.tagName("h3")).findElement(By.tagName("a")).getAttribute("href"))
                            .referenceNumber(header.findElement(By.tagName("h3")).findElement(By.tagName("a")).getAttribute("href").split("-ID")[1].split("\\.html")[0])
                            .description(header.findElement(By.tagName("h3")).findElement(By.tagName("a")).findElement(By.tagName("strong")).getText())
                            .build();
                    try {
                        jobOffer.setSalary(header.findElement(By.cssSelector(" div > span.price-label")).getText());
                    } catch (NoSuchElementException ignored) {
                    }
                    jobOffer.setLocation(offer.findElement(By.cssSelector("td > div > table > tbody > tr:nth-child(2) > td.bottom-cell > div > p > small:nth-child(1)")).getText());
                    offers.add(jobOffer);
                });
        return offers;
    }
}
