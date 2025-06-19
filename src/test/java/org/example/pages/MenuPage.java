package org.example.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.List;

public class MenuPage {
    private AppiumDriver driver;
    private WebDriverWait wait;

    @AndroidFindBy(id = "com.example.eatstedi:id/et_search_menu")
    private WebElement searchField;

    @AndroidFindBy(id = "com.example.eatstedi:id/rv_all_menu")
    private WebElement menuRecyclerView;

    @AndroidFindBy(id = "com.example.eatstedi:id/nested_scroll_view")
    private WebElement orderPanel;

    @AndroidFindBy(id = "com.example.eatstedi:id/rv_order_menu")
    private WebElement orderRecyclerView;

    @AndroidFindBy(id = "com.example.eatstedi:id/tv_total_payment_amount")
    private WebElement totalPaymentAmount;

    @AndroidFindBy(id = "com.example.eatstedi:id/rb_cash")
    private WebElement cashRadioButton;

    @AndroidFindBy(id = "com.example.eatstedi:id/rb_qris")
    private WebElement qrisRadioButton;

    @AndroidFindBy(id = "com.example.eatstedi:id/et_input_payment")
    private WebElement paymentInputField;

    @AndroidFindBy(id = "com.example.eatstedi:id/btn_pay_now")
    private WebElement payButton;

    public MenuPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public boolean isMenuPageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(searchField));
            return searchField.isDisplayed() && menuRecyclerView.isDisplayed();
        } catch (Exception e) { return false; }
    }

    public void selectMenu(String menuName) {
        try {
            String menuXpath = String.format("//android.widget.TextView[@text='%s']", menuName);
            WebElement menuItem = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(menuXpath)));
            menuItem.click();
        } catch (Exception e) { throw new RuntimeException("Menu '" + menuName + "' tidak ditemukan atau tidak dapat diklik.", e); }
    }

    public boolean isMenuAvailable(String menuName) {
        try {
            String xpath = String.format("//*[@resource-id='com.example.eatstedi:id/tv_menu_name' and @text='%s']", menuName);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
            return true;
        } catch (Exception e) { return false; }
    }

    private void ensureOrderPanelIsVisible() {
        try {
            System.out.println("Memastikan panel order terlihat...");
            wait.until(ExpectedConditions.visibilityOf(orderPanel));
            System.out.println("Panel order sudah terlihat.");
        } catch (Exception e) { throw new RuntimeException("Panel order tidak terlihat padahal seharusnya sudah terbuka.", e); }
    }

    public void setMenuQuantity(String menuName, int targetQuantity) {
        ensureOrderPanelIsVisible();
        try {
            String itemContainerXpath = String.format("//*[@resource-id='com.example.eatstedi:id/rv_order_menu']//android.widget.FrameLayout[.//android.widget.TextView[@text='%s']]", menuName);
            WebElement itemContainer = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(itemContainerXpath)));
            WebElement quantityText = itemContainer.findElement(AppiumBy.id("com.example.eatstedi:id/tv_quantity"));
            WebElement plusButton = itemContainer.findElement(AppiumBy.id("com.example.eatstedi:id/btn_plus_order"));
            WebElement minusButton = itemContainer.findElement(AppiumBy.id("com.example.eatstedi:id/btn_minus_order"));
            int currentQuantity = Integer.parseInt(quantityText.getText());
            System.out.println("Mengatur jumlah '" + menuName + "' dari " + currentQuantity + " ke " + targetQuantity);
            if (targetQuantity > currentQuantity) {
                for (int i = currentQuantity; i < targetQuantity; i++) { plusButton.click(); }
            } else if (targetQuantity < currentQuantity) {
                for (int i = currentQuantity; i > targetQuantity; i--) { minusButton.click(); }
            }
        } catch (Exception e) { throw new RuntimeException("Gagal mengatur jumlah menu '" + menuName + "'.", e); }
    }

    public String waitForToastMessageWithText(String expectedText) {
        System.out.println("Mencari toast dengan teks yang mengandung: '" + expectedText + "'");

        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(8));

        try {
            String toastXpath = String.format("//android.widget.Toast[contains(@text, '%s')]", expectedText);
            System.out.println("Menggunakan XPath: " + toastXpath);

            WebElement toastElement = shortWait.until(
                    ExpectedConditions.presenceOfElementLocated(By.xpath(toastXpath))
            );

            String message = toastElement.getText();
            System.out.println("Toast ditemukan dengan teks: '" + message + "'");
            return message;

        } catch (TimeoutException e) {
            System.out.println("Toast dengan teks '" + expectedText + "' tidak ditemukan dalam 8 detik");
            return "";
        } catch (Exception e) {
            System.out.println("Error saat mencari toast: " + e.getMessage());
            return "";
        }
    }

    // Metode tambahan untuk menunggu dan mengabaikan toast loading
    public void waitForLoadingToastToDisappear() {
        try {
            System.out.println("Menunggu toast loading hilang...");
            Thread.sleep(3000); // Wait 3 seconds untuk loading toast hilang

            // Coba hapus semua toast yang ada
            List<WebElement> existingToasts = driver.findElements(By.xpath("//android.widget.Toast"));
            if (!existingToasts.isEmpty()) {
                System.out.println("Menunggu " + existingToasts.size() + " toast existing hilang...");
                Thread.sleep(2000); // Wait additional 2 seconds
            }
        } catch (Exception e) {
            System.out.println("Error saat menunggu loading toast: " + e.getMessage());
        }
    }

    public void selectPaymentMethod(String method) {
        ensureOrderPanelIsVisible();
        if (method.equalsIgnoreCase("Cash") || method.equalsIgnoreCase("Tunai")) {
            wait.until(ExpectedConditions.elementToBeClickable(cashRadioButton)).click();
        } else if (method.equalsIgnoreCase("QRIS")) {
            wait.until(ExpectedConditions.elementToBeClickable(qrisRadioButton)).click();
        }
    }

    public void enterPaymentAmount(String amount) {
        ensureOrderPanelIsVisible();
        if (paymentInputField.isDisplayed()) {
            wait.until(ExpectedConditions.elementToBeClickable(paymentInputField));
            paymentInputField.clear();
            paymentInputField.sendKeys(amount);
        }
    }

    public void clickPayButton() {
        ensureOrderPanelIsVisible();
        wait.until(ExpectedConditions.elementToBeClickable(payButton)).click();
    }

    public String getTotalAmount() {
        ensureOrderPanelIsVisible();
        try {
            wait.until(ExpectedConditions.visibilityOf(totalPaymentAmount));
            return totalPaymentAmount.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isMenuInOrder(String menuName) {
        ensureOrderPanelIsVisible();
        try {
            String xpath = String.format("//*[@resource-id='com.example.eatstedi:id/rv_order_menu']//android.widget.TextView[@text='%s']", menuName);
            List<WebElement> menuItems = driver.findElements(By.xpath(xpath));
            return !menuItems.isEmpty();
        } catch (Exception e) { return false; }
    }
}