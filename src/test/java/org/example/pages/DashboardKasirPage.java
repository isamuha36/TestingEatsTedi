package org.example.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class DashboardKasirPage {
    private AppiumDriver driver;
    private WebDriverWait wait;

    public DashboardKasirPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public boolean isDashboardActive() {
        try {
            // Wait for and locate the "Dasbor" menu item that is checked
            WebElement dashboardMenu = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//android.widget.CheckedTextView[@text='Dasbor' and @checked='true']")
            ));
            return dashboardMenu != null && "true".equals(dashboardMenu.getAttribute("checked"));
        } catch (Exception e) {
            return false;
        }
    }
}