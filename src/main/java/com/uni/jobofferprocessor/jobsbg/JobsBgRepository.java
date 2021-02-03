package com.uni.jobofferprocessor.jobsbg;

import com.uni.jobofferprocessor.configration.SeleniumWebDriverConfiguration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ivelin.dimitrov
 */
@Repository
public class JobsBgRepository {

    @Autowired
    SeleniumWebDriverConfiguration seleniumWebDriverConfiguration;

    /**
     * Returns a list of selectable job categories
     *
     * @return categories
     */
    public List<JobsBgParameter> findAllCategories() {
        List<JobsBgParameter> categories = new ArrayList<>();
        WebDriver driver = seleniumWebDriverConfiguration.getDriver();

        return extractAndAddChips(categories, driver, "categoriesChip", "categoriesSelectSheet");
    }

    public List<JobsBgParameter> findAllLocations() {
        List<JobsBgParameter> locations = new ArrayList<>();
        WebDriver driver = seleniumWebDriverConfiguration.getDriver();

        return extractAndAddChips(locations, driver, "locationChip", "citySelectSheet");
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
        WebDriverWait block = new WebDriverWait(driver, 10);

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
