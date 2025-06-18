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

public class DashboardKasirPage {
    private AppiumDriver driver;
    private WebDriverWait wait;

    @AndroidFindBy(id = "com.example.eatstedi:id/nav_logout")
    private WebElement logoutMenuItem;

    // BARU: Tambahkan locator untuk item navigasi 'Menu'
    @AndroidFindBy(id = "com.example.eatstedi:id/nav_menu")
    private WebElement menuNavigationItem;

    @AndroidFindBy(id = "android:id/button1")
    private WebElement confirmLogoutButton;


    public DashboardKasirPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public boolean isDashboardActive() {
        try {
            WebElement dashboardMenu = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//android.widget.CheckedTextView[@text='Dasbor' and @checked='true']")
            ));
            return dashboardMenu != null && "true".equals(dashboardMenu.getAttribute("checked"));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isUserLoggedIn() {
        try {
            wait.until(ExpectedConditions.visibilityOf(logoutMenuItem));
            return logoutMenuItem.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // BARU: Tambahkan metode untuk melakukan navigasi ke halaman Menu
    public void navigateToMenu() {
        System.out.println("Menavigasi ke halaman Menu...");
        wait.until(ExpectedConditions.elementToBeClickable(menuNavigationItem)).click();
    }

    public void logout() {
        System.out.println("Drawer selalu terbuka. Memulai proses logout...");
        try {
            System.out.println("Menunggu tombol 'Keluar' untuk bisa diklik...");
            wait.until(ExpectedConditions.elementToBeClickable(logoutMenuItem)).click();
            System.out.println("Tombol 'Keluar' berhasil diklik.");

        } catch (Exception e) {
            System.err.println("GAGAL mengklik tombol 'Keluar'. Pastikan ID 'com.example.eatstedi:id/nav_logout' sudah benar dan elemennya interaktif.");
            throw new RuntimeException("Tidak dapat menemukan atau mengklik tombol logout.", e);
        }

        System.out.println("Menekan tombol konfirmasi logout...");
        wait.until(ExpectedConditions.elementToBeClickable(confirmLogoutButton)).click();

        LoginPage loginPage = new LoginPage(driver);
        wait.until(driver -> loginPage.isLoginPageDisplayed());
        System.out.println("Logout berhasil, kembali ke halaman Login.");
    }
}