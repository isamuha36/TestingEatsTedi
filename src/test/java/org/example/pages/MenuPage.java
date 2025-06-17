package org.example.pages;

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

    @AndroidFindBy(id = "com.example.eatstedi:id/et_search_menu")
    private WebElement searchField;

    @AndroidFindBy(id = "com.example.eatstedi:id/sp_filter_name")
    private WebElement filterSpinner;

    @AndroidFindBy(id = "com.example.eatstedi:id/btn_add_new_menu")
    private WebElement addMenuButton;

    @AndroidFindBy(id = "com.example.eatstedi:id/chip_all_menu")
    private WebElement chipAllMenu;

    @AndroidFindBy(id = "com.example.eatstedi:id/chip_foods")
    private WebElement chipFoods;

    @AndroidFindBy(id = "com.example.eatstedi:id/chip_drinks")
    private WebElement chipDrinks;

    @AndroidFindBy(id = "com.example.eatstedi:id/chip_snacks")
    private WebElement chipSnacks;

    @AndroidFindBy(id = "com.example.eatstedi:id/rv_all_menu")
    private WebElement menuRecyclerView;

    @AndroidFindBy(id = "com.example.eatstedi:id/tv_no_data")
    private WebElement noDataMessage;

    @AndroidFindBy(id = "com.example.eatstedi:id/nested_scroll_view")
    private WebElement orderPanel;

    @AndroidFindBy(id = "com.example.eatstedi:id/rv_order_menu")
    private WebElement orderRecyclerView;

    @AndroidFindBy(id = "com.example.eatstedi:id/tv_empty_order")
    private WebElement emptyOrderMessage;

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

    @AndroidFindBy(id = "com.example.eatstedi:id/arrow_toggle")
    private WebElement arrowToggle;

    @AndroidFindBy(xpath = "//android.widget.Toast")
    private WebElement toastMessage;

    public MenuPage(AppiumDriver driver) {
        this.driver = driver;
        // PERBAIKAN: Waktu tunggu ditingkatkan menjadi 20 detik
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

    public void searchMenu(String menuName) {
        wait.until(ExpectedConditions.elementToBeClickable(searchField));
        searchField.clear();
        searchField.sendKeys(menuName);
    }

    public void selectMenu(String menuName) {
        try {
            String xpath = String.format("//android.widget.TextView[@text='%s']", menuName);
            WebElement menuItem = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
            menuItem.click();
        } catch (Exception e) {
            throw new RuntimeException("Menu " + menuName + " tidak ditemukan atau tidak dapat diklik");
        }
    }

    public boolean isMenuAvailable(String menuName) {
        try {
            String xpath = String.format("//android.widget.TextView[@text='%s']", menuName);
            WebElement menuItem = driver.findElement(By.xpath(xpath));
            return menuItem.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void setMenuQuantity(String menuName, int quantity) {
        try {
            // Find the menu item in order panel
            String menuXpath = String.format("//android.widget.TextView[@text='%s']", menuName);
            WebElement menuInOrder = driver.findElement(By.xpath(menuXpath));

            // Find the quantity controls relative to the menu item
            WebElement quantityText = menuInOrder.findElement(By.xpath(".//following-sibling::*//*[@id='tv_quantity']"));
            WebElement plusButton = menuInOrder.findElement(By.xpath(".//following-sibling::*//*[@id='btn_plus_order']"));
            WebElement minusButton = menuInOrder.findElement(By.xpath(".//following-sibling::*//*[@id='btn_minus_order']"));

            int currentQuantity = Integer.parseInt(quantityText.getText());

            if (quantity > currentQuantity) {
                for (int i = currentQuantity; i < quantity; i++) {
                    plusButton.click();
                    Thread.sleep(500);
                }
            } else if (quantity < currentQuantity) {
                for (int i = currentQuantity; i > quantity; i--) {
                    minusButton.click();
                    Thread.sleep(500);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Gagal mengatur jumlah menu " + menuName + " menjadi " + quantity);
        }
    }

    public void selectPaymentMethod(String method) {
        wait.until(ExpectedConditions.elementToBeClickable(arrowToggle));
        if (!orderPanel.isDisplayed()) {
            arrowToggle.click();
        }

        if (method.equalsIgnoreCase("Cash") || method.equalsIgnoreCase("Tunai")) {
            wait.until(ExpectedConditions.elementToBeClickable(cashRadioButton));
            cashRadioButton.click();
        } else if (method.equalsIgnoreCase("QRIS")) {
            wait.until(ExpectedConditions.elementToBeClickable(qrisRadioButton));
            qrisRadioButton.click();
        }
    }

    public void enterPaymentAmount(String amount) {
        if (paymentInputField.isDisplayed()) {
            wait.until(ExpectedConditions.elementToBeClickable(paymentInputField));
            paymentInputField.clear();
            paymentInputField.sendKeys(amount);
        }
    }

    public void clickPayButton() {
        wait.until(ExpectedConditions.elementToBeClickable(payButton));
        payButton.click();
    }

    public String getTotalAmount() {
        try {
            wait.until(ExpectedConditions.visibilityOf(totalPaymentAmount));
            return totalPaymentAmount.getText();
        } catch (Exception e) {
            return "";
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

    public boolean isMenuInOrder(String menuName) {
        try {
            String xpath = String.format("//android.widget.TextView[@text='%s']", menuName);
            List<WebElement> menuItems = orderRecyclerView.findElements(By.xpath(xpath));
            return !menuItems.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isOrderPanelEmpty() {
        try {
            return emptyOrderMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void openOrderPanel() {
        if (!orderPanel.isDisplayed()) {
            wait.until(ExpectedConditions.elementToBeClickable(arrowToggle));
            arrowToggle.click();
        }
    }
}