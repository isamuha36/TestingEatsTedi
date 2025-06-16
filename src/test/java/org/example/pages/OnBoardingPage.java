package org.example.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
public class OnBoardingPage {
    private AppiumDriver driver;
    private WebDriverWait wait;

    @AndroidFindBy(id = "com.example.eatstedi:id/skipButton")
    private WebElement skipButton;

    @AndroidFindBy(id = "com.example.eatstedi:id/viewPager")
    private WebElement viewPager;

    @AndroidFindBy(id = "com.example.eatstedi:id/dotsIndicator")
    private WebElement dotsIndicator;

    public OnBoardingPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public boolean isSplashScreenDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(viewPager));
            return viewPager.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void skipOnboarding() {
        wait.until(ExpectedConditions.elementToBeClickable(skipButton));
        skipButton.click();
    }

    public boolean isOnboardingDisplayed() {
        try {
            return viewPager.isDisplayed() && dotsIndicator.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
