package com.uni.jobofferprocessor.zaplatabg;

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
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author ivelin.dimitrov
 */
@Repository
public class ZaplataBgRepository {

    private final SeleniumWebDriverConfiguration seleniumWebDriverConfiguration;
    private final Integer timeout;

    private static final String ZAPLATA_BG_HOST = "https://www.zaplata.bg/";

    private static final List<String> availableLocations = List.of(
            "plovdiv",
            "sofia",
            "varna",
            "stara-zagora",
            "rousse",
            "bourgas",
            "gr-pleven"
    );

    public ZaplataBgRepository(SeleniumWebDriverConfiguration seleniumWebDriverConfiguration, Environment env) {
        this.seleniumWebDriverConfiguration = seleniumWebDriverConfiguration;
        this.timeout = Integer.parseInt(Objects.requireNonNull(env.getProperty("driver.timeoutInSeconds")));
    }

    /**
     * gets zaplata bg locations from static hardcoded list
     *
     * @return
     */
    public List<String> findAllLocations() {
        return availableLocations;
    }

    /**
     * gets zaplata bg categories from modal
     *
     * @return
     */
    public List<ZaplataBgCategoryParameter> getCategories() {
        List<ZaplataBgCategoryParameter> categoryParameterList = new ArrayList<>();
        WebDriver driver = seleniumWebDriverConfiguration.getStaticDriver();
        driver.get(ZAPLATA_BG_HOST);
        driver.findElement(By.id("hsCatLink")).click();
        WebDriverWait block = new WebDriverWait(driver, this.timeout);
        block.until(ExpectedConditions.visibilityOfElementLocated(By.id("hsCatPU")));
        categoryParameterList.addAll(extractCategoriesFromModalTd("#hsCatPU > div.inside > div:nth-child(1)", driver));
        categoryParameterList.addAll(extractCategoriesFromModalTd("#hsCatPU > div.inside > div:nth-child(2)", driver));
        return categoryParameterList
                .stream()
                .sorted(Comparator.comparingInt(ZaplataBgCategoryParameter::getId))
                .collect(Collectors.toList());
    }

    public List<JobOffer> getJobOffers(Integer max, Integer categoryId, String locationName) {
        List<JobOffer> offerList = new ArrayList<>();
        IntStream range = IntStream.rangeClosed(1, max / 20);
        range.parallel().forEach(currentStep -> {
            WebDriver driver = seleniumWebDriverConfiguration.getNewDriver();
            driver.get(ZAPLATA_BG_HOST + "/" + locationName + "/?&cat%5B0%5D=" + categoryId + "&page=" + currentStep);
            driver.findElement(By.cssSelector("body > div.page.lineHeightFix.pInside > ul.scheme23 > li.left > div.listItems"))
                    .findElements(By.tagName("ul"))
                    .parallelStream()
                    .forEach(webElement -> {
                        WebElement element = webElement.findElement(By.className("c2"));
                        JobOffer offer = JobOffer.builder()
                                .offerLink(element.findElement(By.tagName("a")).getAttribute("href"))
                                .location(element.findElement(By.className("location")).getText().split(",")[1])
                                .referenceNumber(element.findElement(By.tagName("a")).getAttribute("href").replaceAll("\\D+", ""))
                                .description(element.findElement(By.tagName("a")).getText())
                                .build();
                        try {
                            offer.setSalary(element.findElement(By.className("is_visibility_salary")).getText());
                        } catch (NoSuchElementException ignored) {
                        }
                        System.out.println(offer);
                        offerList.add(offer);
                    });
            driver.close();
        });

        return offerList
                .stream()
                .filter(jobOffer -> jobOffer.getReferenceNumber() != null)
                .filter(jobOffer -> !jobOffer.getReferenceNumber().isBlank())
                .collect(Collectors.toList());
    }

    /**
     * Extracts zaplata bg categories from a single td element
     *
     * @param selector
     * @param driver
     * @return
     */
    private List<ZaplataBgCategoryParameter> extractCategoriesFromModalTd(String selector, WebDriver driver) {
        List<ZaplataBgCategoryParameter> tmpList = new ArrayList<>();
        driver.findElement(By.cssSelector(selector))
                .findElements(By.tagName("input"))
                .parallelStream()
                .forEach(label -> tmpList.add(
                        ZaplataBgCategoryParameter.builder()
                                .id(Integer.parseInt(label.getAttribute("value")))
                                .description(label.getAttribute("attr-text"))
                                .build()
                ));
        return tmpList;
    }
}
