package com.uni.jobofferprocessor;

import com.uni.jobofferprocessor.configration.SeleniumWebDriverConfiguration;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ivelin.dimitrov
 */
public class HighlightElement {

    public static void highlightElement(WebElement element, WebDriver driver) throws InterruptedException {
        for (int i = 0; i < 2; i++) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "color: red; border: 4px solid yellow;");
            Thread.sleep(200);
            js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
        }
    }
}
