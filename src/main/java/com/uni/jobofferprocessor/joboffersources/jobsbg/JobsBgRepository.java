package com.uni.jobofferprocessor.joboffersources.jobsbg;

import com.uni.jobofferprocessor.configuration.SeleniumWebDriverConfiguration;
import com.uni.jobofferprocessor.core.JobOffer;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author ivelin.dimitrov
 */
@Repository
public class JobsBgRepository {

    public static final String JOBS_BG_HOST = "https://www.jobs.bg/front_job_search.php?frompage=";

    private final SeleniumWebDriverConfiguration seleniumWebDriverConfiguration;
    private final Integer timeout;

    /**
     * Inits timeout from application properties and injects selenium configuration
     * @param seleniumWebDriverConfiguration
     * @param env
     */
    public JobsBgRepository(SeleniumWebDriverConfiguration seleniumWebDriverConfiguration, Environment env) {
        this.seleniumWebDriverConfiguration = seleniumWebDriverConfiguration;
        this.timeout = Integer.parseInt(Objects.requireNonNull(env.getProperty("driver.timeoutInSeconds")));
    }

    /**
     * Returns a list of selectable job categories
     *
     * @return categories
     */
    public List<JobsBgParameter> findAllCategories() {
        List<JobsBgParameter> categories = new ArrayList<>();
        WebDriver driver = seleniumWebDriverConfiguration.getStaticDriver();

        return extractAndAddChips(categories, driver, "categoriesChip", "categoriesSelectSheet");
    }

    /**
     * Returns a list of selectable job locations
     *
     * @return locations
     */
    public List<JobsBgParameter> findAllLocations() {
        List<JobsBgParameter> locations = new ArrayList<>();
        WebDriver driver = seleniumWebDriverConfiguration.getStaticDriver();

        return extractAndAddChips(locations, driver, "locationChip", "citySelectSheet");
    }

    /**
     * Iterate through pages and extract jobs
     *
     * @param maxSize
     * @param locationId
     * @param categoryId
     * @return
     */
    public List<JobOffer> findAllJobs(Integer maxSize, Integer locationId, Integer categoryId) {
        List<JobOffer> offersList = new ArrayList<>();

        IntStream range = IntStream.rangeClosed(0, maxSize / 15 - 1);
        range.parallel().forEach(currentStep -> {
            WebDriver driver = seleniumWebDriverConfiguration.getNewDriver();
            driver.get(JOBS_BG_HOST + currentStep * 15 + "&add_sh=1&categories%5B0%5D=" + categoryId + "&location_sid=" + locationId);
            offersList.addAll(getJobsFromPage(driver));
            driver.close();
        });

        return offersList.stream()
                .filter(jobOffer -> jobOffer.getReferenceNumber() != null)
                .filter(jobOffer -> !jobOffer.getReferenceNumber().isBlank())
                .collect(Collectors.toList());
    }

    /**
     * Extract jobs from a single page
     *
     * @param driver
     */
    public List<JobOffer> getJobsFromPage(WebDriver driver) {
        List<JobOffer> offerList = new ArrayList<>();
        driver.findElement(By.id("search_results_div"))
                .findElement(By.tagName("table"))
                .findElement(By.tagName("tbody"))
                .findElement(By.tagName("tr"))
                .findElement(By.tagName("td"))
                .findElement(By.tagName("table"))
                .findElement(By.tagName("tbody"))
                .findElements(By.tagName("tr")).get(5)
                .findElement(By.tagName("td"))
                .findElement(By.tagName("table"))
                .findElement(By.tagName("tbody"))
                .findElement(By.tagName("tr"))
                .findElement(By.tagName("td"))
                .findElement(By.tagName("table"))
                .findElement(By.tagName("tbody"))
                .findElements(By.tagName("tr"))
                .parallelStream()
                .forEach(tableRow -> {
                    JobOffer jobOffer = new JobOffer();
                    try {
                        jobOffer.setDescription(tableRow
                                .findElements(By.tagName("td"))
                                .get(0)
                                .findElement(By.tagName("a"))
                                .getText()
                        );

                        jobOffer.setOfferLink(tableRow.
                                findElements(By.tagName("td"))
                                .get(0)
                                .findElement(By.tagName("a"))
                                .getAttribute("href")
                        );

                        jobOffer.setReferenceNumber(tableRow.
                                findElements(By.tagName("td"))
                                .get(0)
                                .findElement(By.tagName("a"))
                                .getAttribute("href").replaceAll("\\D+", "")
                        );

                        jobOffer.setLocation(tableRow.
                                findElements(By.tagName("td"))
                                .get(0)
                                .findElement(By.tagName("div"))
                                .findElement(By.tagName("span")).getText().split(";")[0]
                        );

                        try {
                            jobOffer.setSalary(tableRow.
                                    findElements(By.tagName("td"))
                                    .get(0)
                                    .findElement(By.tagName("div"))
                                    .findElement(By.tagName("span")).getText().split(";")[1]
                            );
                        } catch (ArrayIndexOutOfBoundsException ignored) {
                        }
                    } catch (NoSuchElementException ignored) {
                    }
                    offerList.add(jobOffer);
                });

        return offerList;
    }

    /**
     * Interal method that extracts chips from modals for jobsBg
     *
     * @param parameters
     * @param driver
     * @param modalButton
     * @param modalId
     * @return
     */
    private List<JobsBgParameter> extractAndAddChips(List<JobsBgParameter> parameters, WebDriver driver, String modalButton, String modalId) {
        WebDriverWait block = new WebDriverWait(driver, this.timeout);

        // open modal and iterate through all chips
        driver.get("https://www.jobs.bg/");
        driver.findElement(
                By.id(modalButton)
        ).click();
        block.until(ExpectedConditions.visibilityOfElementLocated(By.id(modalId)));
        driver.findElement(By.id(modalId)).findElements(By.className("mdc-chip")).forEach(webElement -> {
            try {
                parameters.add(
                        new JobsBgParameter(
                                webElement.getAttribute("val") == null ? Integer.parseInt(webElement.getAttribute("id").split("_")[1]) : Integer.parseInt(webElement.getAttribute("val")),
                                !webElement.getText().isBlank() ? webElement.getText() : webElement.findElement(By.tagName("span")).getText()
                        )
                );
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }
        });

        return parameters
                .stream()
                .sorted(Comparator.comparingInt(JobsBgParameter::getId))
                .filter(jobsBgParameter -> jobsBgParameter.getId() != null)
                .collect(Collectors.toList());
    }
}
