package org.example.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.net.UrlChecker;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class LoginPage {
    private AppiumDriver driver;
    private WebDriverWait wait;

    @AndroidFindBy(id = "com.example.eatstedi:id/et_username")
    private WebElement usernameField;

    @AndroidFindBy(id = "com.example.eatstedi:id/et_password")
    private WebElement passwordField;

    @AndroidFindBy(id = "com.example.eatstedi:id/btn_login")
    private WebElement loginButton;

    @AndroidFindBy(id = "com.example.eatstedi:id/btn_login_progress")
    private WebElement loginProgress;

    @AndroidFindBy(xpath = "//android.widget.Toast")
    private WebElement toastMessage;

    public LoginPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public boolean isLoginPageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(usernameField));
            return usernameField.isDisplayed() && passwordField.isDisplayed() && loginButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void enterUsername(String username) {
        wait.until(ExpectedConditions.elementToBeClickable(usernameField));
        usernameField.clear();
        usernameField.sendKeys(username);
    }

    public void enterPassword(String password) {
        wait.until(ExpectedConditions.elementToBeClickable(passwordField));
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void clickLoginButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        loginButton.click();
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    public boolean isLoadingDisplayed() {
        try {
            return loginProgress.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isErrorMessageDisplayed(String expectedMessage) {
        try {
            String actualMessage = getToastMessage(expectedMessage, 1);
            return actualMessage.contains(expectedMessage);
        } catch (Exception e) {
            return false;
        }
    }

public String getToastMessage(String expectedToastText, int timeoutSeconds) {
        try {
            // Define the XPath dynamically based on the expected toast text
            String toastXPath = String.format("//android.widget.Toast[contains(@text, '%s')]", expectedToastText);

            // Initialize WebDriverWait with the specified timeout
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));

            // Wait for the Toast to appear and locate it
            WebElement toastElement = wait.until(ExpectedConditions.presenceOfElementLocated(
                    MobileBy.xpath(toastXPath)
            ));

            // Retrieve and return the Toast text
            String toastText = toastElement.getText();
            return toastText;

        } catch (NoSuchElementException e) {
            throw new RuntimeException("Failed to locate Toast element", e);
        }
    }
}