package com.uni.jobofferprocessor.joboffersources.alobg;

import com.uni.jobofferprocessor.configuration.SeleniumWebDriverConfiguration;
import com.uni.jobofferprocessor.core.JobOffer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * @author ivelin.dimitrov
 */
@Repository
public class AloBgRepository {

    private final SeleniumWebDriverConfiguration seleniumWebDriverConfiguration;
    private final Integer timeout;
    private final String offersSelector;
    private final String locationsSelector;
    private final String categoriesSelector;
    private final String host;

    /**
     * fetches properties from application configuration file, injects selenium config
     *
     * @param seleniumWebDriverConfiguration
     * @param env
     */
    public AloBgRepository(
            SeleniumWebDriverConfiguration seleniumWebDriverConfiguration,
            Environment env
    ) {
        this.seleniumWebDriverConfiguration = seleniumWebDriverConfiguration;
        this.timeout = Integer.parseInt(Objects.requireNonNull(env.getProperty("driver.timeoutInSeconds")));
        this.offersSelector = Objects.requireNonNull(env.getProperty("alobg.offers-selector"));
        this.locationsSelector = Objects.requireNonNull(env.getProperty("alobg.locations-selector"));
        this.categoriesSelector = Objects.requireNonNull(env.getProperty("alobg.categories-selector"));
        this.host = Objects.requireNonNull(env.getProperty("alobg.url"));
    }

    /**
     * Collects all locations from alobg
     *
     * @return
     */
    public List<AloBgParameter> findAllLocations() {
        List<AloBgParameter> locationsList = new ArrayList<>();
        WebDriver driver = seleniumWebDriverConfiguration.getNewDriver();
        driver.get(host);
        WebElement element = driver.findElement(By.cssSelector(locationsSelector));
        element.findElements(By.className("region_hover"))
                .parallelStream()
                .forEach(location -> locationsList.add(
                        AloBgParameter.builder()
                                .id(Integer.parseInt(location.getAttribute("region_id")))
                                .name(location.getText())
                                .build()
                ));
        driver.close();
        return locationsList;
    }


    /**
     * Collects all jobs categories
     *
     * @return
     */
    public List<AloBgParameter> findAllCategories() {
        List<AloBgParameter> categoriesList = new ArrayList<>();
        WebDriver driver = seleniumWebDriverConfiguration.getNewDriver();
        driver.get(host);
        driver.findElement(By.cssSelector("#consent-info > div:nth-child(2) > button")).click();
        driver.findElement(By.cssSelector(categoriesSelector)).click();
        WebDriverWait block = new WebDriverWait(driver, this.timeout);
        block.until(ExpectedConditions.visibilityOfElementLocated(By.id("call_lg_overlay")));
        WebElement categoriesCollection = driver.findElement(By.cssSelector("#group_form > div.filter-box-items"));
        categoriesCollection.findElements(By.className("fm-item"))
                .parallelStream()
                .forEach(category -> categoriesList.add(
                        AloBgParameter.builder()
                                .name(category.findElement(By.cssSelector(".bcheckbox > span:nth-child(3)")).getText().replaceAll("[0-9()]", ""))
                                .id(Integer.parseInt(category.findElement(By.className("gb_checkbox")).getAttribute("value")))
                                .build()
                ));
        driver.close();
        return categoriesList;
    }

    /**
     * Initiates parallel streaming from multiple pages and calls the business logic that extracts the available informations
     *
     * @param maxSize
     * @param locationId
     * @param categoryId
     * @return
     */
    public List<JobOffer> findAllJobsFromAllPages(Integer maxSize, Integer locationId, Integer categoryId) {
        List<JobOffer> offersList = new ArrayList<>();
        IntStream range = IntStream.rangeClosed(1, maxSize / 40 + 1);
        range.parallel().forEach(currentStep -> {
            WebDriver driver = seleniumWebDriverConfiguration.getNewDriver();
            driver.get(host + "?region_id=" + locationId + "&p[69]=" + categoryId + "&page=" + currentStep);
            offersList.addAll(findAllJobsFromSinglePage(driver));
            driver.close();
        });
        return offersList;
    }

    /**
     * Extracts the needed fields for a given offer and returns a list of everything that is collected
     *
     * @param driver
     * @return
     */
    public List<JobOffer> findAllJobsFromSinglePage(WebDriver driver) {
        List<JobOffer> offersList = new ArrayList<>();
        driver.findElement(By.cssSelector("#consent-info > div:nth-child(2) > button")).click();
        driver.findElement(By.cssSelector(offersSelector))
                .findElements(By.className("listvip-item"))
                .parallelStream()
                .forEach(item -> offersList.add(JobOffer.builder()
                        .offerLink(item.findElement(By.cssSelector("div.listvip-params > div.listvip-item-header > a")).getAttribute("href"))
                        .referenceNumber(item.findElement(By.cssSelector("div.listvip-params > div.listvip-item-header > a")).getAttribute("href").replaceAll("\\D+", ""))
                        .location(item.findElement(By.cssSelector("div.listvip-params > div.listvip-item-header > div.listvip-item-address > i")).getText())
                        .description(item.findElement(By.cssSelector("div.listvip-params > div.listvip-item-header > a")).getText())
                        .jobPosition(item.findElement(By.cssSelector("div.listvip-params > div.listvip-item-content > span:nth-child(1)")).getText())
                        .build()
                ));

        return offersList;
    }
}
