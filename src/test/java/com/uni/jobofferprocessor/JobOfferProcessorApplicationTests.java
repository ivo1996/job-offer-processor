package com.uni.jobofferprocessor;

import com.uni.jobofferprocessor.configration.SeleniumWebDriverConfiguration;
import com.uni.jobofferprocessor.core.general.JobOffer;
import com.uni.jobofferprocessor.jobsbg.JobsBgRepository;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class JobOfferProcessorApplicationTests {

    @Autowired
    JobsBgRepository jobsBgRepository;

    @Autowired
    SeleniumWebDriverConfiguration seleniumWebDriverConfiguration;

    @Test
    void getJobsBgCategories() {
        assertFalse(jobsBgRepository.findAllCategories().isEmpty());
    }

    @Test
    void getJobsBgTowns() {
        assertFalse(jobsBgRepository.findAllLocations().isEmpty());
    }

    @Test
    void getJobsBgOffer() throws InterruptedException {
        WebDriver driver = seleniumWebDriverConfiguration.getDriver();
        List<JobOffer> offersList = new ArrayList<>();
        driver.get("https://www.jobs.bg/front_job_search.php?frompage=0&add_sh=1&categories%5B0%5D=56&location_sid=1");
        System.out.println(driver.findElement(By.id("search_results_div"))
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
                .findElements(By.tagName("tr")).get(1)
                .findElements(By.tagName("td")).get(2)
                .findElement(By.className("company_link")).getText());
        HighlightElement.highlightElement(driver.findElement(By.id("search_results_div"))
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
                        .findElements(By.tagName("tr")).get(0)
                        .findElements(By.tagName("td")).get(2)
                        .findElement(By.className("company_link"))
                ,
                driver);
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
                .forEach(tableRow -> {
                    JobOffer jobOffer = new JobOffer();
                    try {
                        IntStream.range(0, tableRow.findElements(By.tagName("td")).size())
                                .forEach(index -> {
                                    if (index == 0) {
                                        jobOffer.setDescription(tableRow
                                                .findElements(By.tagName("td"))
                                                .get(index)
                                                .findElement(By.tagName("a"))
                                                .getText()
                                        );

                                        jobOffer.setOfferLink(tableRow.
                                                findElements(By.tagName("td"))
                                                .get(index)
                                                .findElement(By.tagName("a"))
                                                .getAttribute("href")
                                        );

                                        jobOffer.setReferenceNumber(tableRow.
                                                findElements(By.tagName("td"))
                                                .get(index)
                                                .findElement(By.tagName("a"))
                                                .getAttribute("href").replaceAll("\\D+", "")
                                        );

                                        jobOffer.setLocation(tableRow.
                                                findElements(By.tagName("td"))
                                                .get(index)
                                                .findElement(By.tagName("div"))
                                                .findElement(By.tagName("span")).getText().split(";")[0]
                                        );

                                        jobOffer.setSalary(tableRow.
                                                findElements(By.tagName("td"))
                                                .get(index)
                                                .findElement(By.tagName("div"))
                                                .findElement(By.tagName("span")).getText().split(";")[1]
                                        );

                                    } else if (index == 2) {
                                        jobOffer.setCompany(tableRow.
                                                findElements(By.tagName("td"))
                                                .get(index)
                                                .findElement(By.tagName("a"))
                                                .getText());
                                    }
                                });
                    } catch (org.openqa.selenium.NoSuchElementException | ArrayIndexOutOfBoundsException ignored) {
                    }
                    offersList.add(jobOffer);
                });
        offersList
                .stream().filter(jobOffer -> jobOffer.getReferenceNumber() != null)
                .filter(jobOffer -> !jobOffer.getReferenceNumber().isBlank())
                .forEach(System.out::println);
    }

}
