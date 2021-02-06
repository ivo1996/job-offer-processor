package com.uni.jobofferprocessor.joboffersources.zaplatabg;

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
    private final ZaplataBgService zaplataBgService;
    private final Integer timeout;
    private final String host;
    private final String offersSelector;
    private final String categoriesSelector;

    /**
     * Hardcoded list of location IDs
     */
    private static final List<String> availableLocations = List.of(
            "plovdiv",
            "sofia",
            "varna",
            "stara-zagora",
            "rousse",
            "bourgas",
            "gr-pleven"
    );

    /**
     * Injects selenium config and fetches timeout property from yml
     *
     * @param seleniumWebDriverConfiguration
     * @param zaplataBgService
     * @param env
     */
    public ZaplataBgRepository(SeleniumWebDriverConfiguration seleniumWebDriverConfiguration, ZaplataBgService zaplataBgService, Environment env) {
        this.seleniumWebDriverConfiguration = seleniumWebDriverConfiguration;
        this.zaplataBgService = zaplataBgService;
        this.timeout = Integer.parseInt(Objects.requireNonNull(env.getProperty("driver.timeoutInSeconds")));
        this.host = Objects.requireNonNull(env.getProperty("zaplatabg.url"));
        this.offersSelector = Objects.requireNonNull(env.getProperty("zaplatabg.offers-selector"));
        this.categoriesSelector = Objects.requireNonNull(env.getProperty("zaplatabg.categories-selector"));
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
        driver.get(host);
        driver.findElement(By.id("hsCatLink")).click();
        WebDriverWait block = new WebDriverWait(driver, this.timeout);
        block.until(ExpectedConditions.visibilityOfElementLocated(By.id("hsCatPU")));
        categoryParameterList.addAll(extractCategoriesFromModalTd(categoriesSelector + "(1)", driver));
        categoryParameterList.addAll(extractCategoriesFromModalTd(categoriesSelector + "(2)", driver));
        return categoryParameterList
                .stream()
                .sorted(Comparator.comparingInt(ZaplataBgCategoryParameter::getId))
                .collect(Collectors.toList());
    }

    /**
     * Parallel iteration of both offers and pages, serializes jobOffer obj
     *
     * @param max
     * @param categoryId
     * @param locationName
     * @return
     */
    public List<JobOffer> getJobOffers(Integer max, Integer categoryId, String locationName) {
        List<JobOffer> offerList = new ArrayList<>();
        String category = zaplataBgService.findAllCategories().stream().findAny().filter(it -> it.id.equals(categoryId)).get().description;
        IntStream range = IntStream.rangeClosed(1, max / 20);
        range.parallel().forEach(currentStep -> {
            WebDriver driver = seleniumWebDriverConfiguration.getNewDriver();
            driver.get(host + "/" + locationName + "/?&cat%5B0%5D=" + categoryId + "&page=" + currentStep);
            driver.findElement(By.cssSelector(offersSelector))
                    .findElements(By.tagName("ul"))
                    .parallelStream()
                    .forEach(webElement -> {
                        WebElement element = webElement.findElement(By.className("c2"));
                        JobOffer offer = JobOffer.builder()
                                .jobPosition(category)
                                .offerLink(element.findElement(By.tagName("a")).getAttribute("href"))
                                .location(element.findElement(By.className("location")).getText().split(",")[1])
                                .referenceNumber(element.findElement(By.tagName("a")).getAttribute("href").replaceAll("\\D+", ""))
                                .description(element.findElement(By.tagName("a")).getText())
                                .build();
                        try {
                            offer.setSalary(element.findElement(By.className("is_visibility_salary")).getText());
                        } catch (NoSuchElementException ignored) {
                        }
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
