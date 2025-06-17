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
        // Kita gunakan waktu tunggu 20 detik agar konsisten dengan halaman lain
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public boolean isOnboardingScreenPresent() {
        try {
            // Kita anggap jika viewPager ada, maka halaman onboarding sedang tampil.
            // Pengecekan skipButton ditambahkan untuk keyakinan.
            return viewPager.isDisplayed() && skipButton.isDisplayed();
        } catch (Exception e) {
            // Jika elemen tidak ditemukan, exception akan ditangkap dan mengembalikan false.
            // Ini adalah perilaku yang kita inginkan.
            return false;
        }
    }

    public void skipOnboarding() {
        wait.until(ExpectedConditions.elementToBeClickable(skipButton));
        skipButton.click();
    }
}