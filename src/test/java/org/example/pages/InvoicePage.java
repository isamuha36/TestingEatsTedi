package org.example.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class InvoicePage {
    private AppiumDriver driver;
    private WebDriverWait wait;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='EATS TEDI']")
    private WebElement invoiceTitle;

    @AndroidFindBy(id = "com.example.eatstedi:id/tv_date_order")
    private WebElement orderDate;

    @AndroidFindBy(id = "com.example.eatstedi:id/tv_time_order")
    private WebElement orderTime;

    @AndroidFindBy(id = "com.example.eatstedi:id/tv_name_employee")
    private WebElement employeeName;

    @AndroidFindBy(id = "com.example.eatstedi:id/tv_menu_name")
    private WebElement menuName;

    @AndroidFindBy(id = "com.example.eatstedi:id/tv_menu_price")
    private WebElement menuPrice;

    @AndroidFindBy(id = "com.example.eatstedi:id/tv_total_payment_amount")
    private WebElement totalAmount;

    @AndroidFindBy(id = "com.example.eatstedi:id/tv_payment_method_label")
    private WebElement paymentMethodLabel;

    @AndroidFindBy(id = "com.example.eatstedi:id/tv_money_pay")
    private WebElement moneyPaid;

    @AndroidFindBy(id = "com.example.eatstedi:id/tv_money_change")
    private WebElement moneyChange;

    @AndroidFindBy(id = "com.example.eatstedi:id/btn_close")
    private WebElement closeButton;

    public InvoicePage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public boolean isInvoiceDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(invoiceTitle));
            return invoiceTitle.isDisplayed() &&
                    orderDate.isDisplayed() &&
                    totalAmount.isDisplayed() &&
                    closeButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getOrderDate() {
        try {
            wait.until(ExpectedConditions.visibilityOf(orderDate));
            return orderDate.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getOrderTime() {
        try {
            wait.until(ExpectedConditions.visibilityOf(orderTime));
            return orderTime.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getEmployeeName() {
        try {
            wait.until(ExpectedConditions.visibilityOf(employeeName));
            return employeeName.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getMenuName() {
        try {
            wait.until(ExpectedConditions.visibilityOf(menuName));
            return menuName.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getMenuPrice() {
        try {
            wait.until(ExpectedConditions.visibilityOf(menuPrice));
            return menuPrice.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getTotalAmount() {
        try {
            wait.until(ExpectedConditions.visibilityOf(totalAmount));
            return totalAmount.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getPaymentMethod() {
        try {
            wait.until(ExpectedConditions.visibilityOf(paymentMethodLabel));
            return paymentMethodLabel.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getMoneyPaid() {
        try {
            wait.until(ExpectedConditions.visibilityOf(moneyPaid));
            return moneyPaid.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getMoneyChange() {
        try {
            wait.until(ExpectedConditions.visibilityOf(moneyChange));
            return moneyChange.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public void closeInvoice() {
        wait.until(ExpectedConditions.elementToBeClickable(closeButton));
        closeButton.click();
    }

    public boolean isTransactionSuccessful() {
        return isInvoiceDisplayed() &&
                !getTotalAmount().isEmpty() &&
                !getOrderDate().isEmpty();
    }
}