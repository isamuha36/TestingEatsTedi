package org.example.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
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
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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

    public String getToastMessage() {
        try {
            wait.until(ExpectedConditions.visibilityOf(toastMessage));
            return toastMessage.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isErrorMessageDisplayed(String expectedMessage) {
        try {
            String actualMessage = getToastMessage();
            return actualMessage.contains(expectedMessage);
        } catch (Exception e) {
            return false;
        }
    }
}