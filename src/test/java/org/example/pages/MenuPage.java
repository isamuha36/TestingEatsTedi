package org.example.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.List;

public class MenuPage {
    private AppiumDriver driver;
    private WebDriverWait wait;

    // --- Locators untuk Bagian Daftar Menu ---
    @AndroidFindBy(id = "com.example.eatstedi:id/et_search_menu")
    private WebElement searchField;

    @AndroidFindBy(id = "com.example.eatstedi:id/rv_all_menu")
    private WebElement menuRecyclerView;

    // --- Locators untuk Bagian Panel Order ---
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

    @AndroidFindBy(xpath = "//android.widget.Toast")
    private WebElement toastMessage;

    public MenuPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public boolean isMenuPageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(searchField));
            return searchField.isDisplayed() && menuRecyclerView.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void selectMenu(String menuName) {
        try {
            String menuXpath = String.format("//android.widget.TextView[@text='%s']", menuName);
            WebElement menuItem = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(menuXpath)));
            menuItem.click();
        } catch (Exception e) {
            throw new RuntimeException("Menu '" + menuName + "' tidak ditemukan atau tidak dapat diklik.", e);
        }
    }

    public boolean isMenuAvailable(String menuName) {
        try {
            String xpath = String.format("//*[@resource-id='com.example.eatstedi:id/tv_menu_name' and @text='%s']", menuName);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void ensureOrderPanelIsVisible() {
        try {
            System.out.println("Memastikan panel order terlihat...");
            wait.until(ExpectedConditions.visibilityOf(orderPanel));
            System.out.println("Panel order sudah terlihat.");
        } catch (Exception e) {
            throw new RuntimeException("Panel order tidak terlihat padahal seharusnya sudah terbuka.", e);
        }
    }

    // =======================================================================
    // PERBAIKAN TOTAL PADA METODE setMenuQuantity SESUAI XML ANDA
    // =======================================================================
    public void setMenuQuantity(String menuName, int targetQuantity) {
        ensureOrderPanelIsVisible();

        try {
            // 1. Temukan WADAH UTAMA (FrameLayout) dari item yang spesifik.
            // XPath ini mengatakan: "Di dalam rv_order_menu, cari FrameLayout yang
            // di dalamnya (descendant::) ada TextView dengan nama menu yang dicari."
            String itemContainerXpath = String.format(
                    "//*[@resource-id='com.example.eatstedi:id/rv_order_menu']//android.widget.FrameLayout[.//android.widget.TextView[@text='%s']]",
                    menuName
            );
            WebElement itemContainer = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(itemContainerXpath)));

            // 2. Sekarang, cari elemen-elemen di DALAM wadah FrameLayout yang sudah benar ini.
            WebElement quantityText = itemContainer.findElement(AppiumBy.id("com.example.eatstedi:id/tv_quantity"));
            WebElement plusButton = itemContainer.findElement(AppiumBy.id("com.example.eatstedi:id/btn_plus_order"));
            WebElement minusButton = itemContainer.findElement(AppiumBy.id("com.example.eatstedi:id/btn_minus_order"));

            int currentQuantity = Integer.parseInt(quantityText.getText());
            System.out.println("Mengatur jumlah '" + menuName + "' dari " + currentQuantity + " ke " + targetQuantity);

            // 3. Loop untuk menambah atau mengurangi jumlah.
            if (targetQuantity > currentQuantity) {
                for (int i = currentQuantity; i < targetQuantity; i++) {
                    plusButton.click();
                }
            } else if (targetQuantity < currentQuantity) {
                for (int i = currentQuantity; i > targetQuantity; i--) {
                    minusButton.click();
                }
            }
        } catch (Exception e) {
            System.err.println("Gagal menemukan elemen di dalam item order untuk '" + menuName + "'. Periksa XPath dan ID elemen (tv_quantity, btn_plus_order, dll).");
            throw new RuntimeException("Gagal mengatur jumlah menu '" + menuName + "'.", e);
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

    public String getToastMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(toastMessage)).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isMenuInOrder(String menuName) {
        ensureOrderPanelIsVisible();
        try {
            // Kita cari di dalam RecyclerView order, bukan di seluruh halaman
            String xpath = String.format("//*[@resource-id='com.example.eatstedi:id/rv_order_menu']//android.widget.TextView[@text='%s']", menuName);
            List<WebElement> menuItems = driver.findElements(By.xpath(xpath));
            return !menuItems.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}