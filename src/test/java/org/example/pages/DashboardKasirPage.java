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

    // KARENA DRAWER SELALU TERBUKA, kita bisa menggunakan @AndroidFindBy dengan aman.
    // Elemen ini akan menjadi indikator utama status login.
    @AndroidFindBy(id = "com.example.eatstedi:id/nav_logout")
    private WebElement logoutMenuItem;

    // DIHAPUS: Locator untuk tombol hamburger (Open navigation drawer) karena tidak ada di UI Anda.

    public DashboardKasirPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    /**
     * Metode ini tetap berguna jika ada skenario yang spesifik perlu memeriksa
     * apakah menu 'Dasbor' sedang aktif.
     */
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

    /**
     * Memeriksa apakah pengguna sudah login dengan cara yang paling andal untuk UI Anda:
     * mencari keberadaan tombol 'Keluar'.
     * @return true jika tombol 'Keluar' terlihat, false jika tidak.
     */
    public boolean isUserLoggedIn() {
        try {
            // Tunggu hingga tombol 'Keluar' terlihat. WebDriverWait akan menangani timeout.
            wait.until(ExpectedConditions.visibilityOf(logoutMenuItem));
            return logoutMenuItem.isDisplayed();
        } catch (Exception e) {
            // Jika elemen tidak ditemukan setelah waktu tunggu, berarti pengguna tidak login.
            return false;
        }
    }

    /**
     * Menjalankan proses logout lengkap.
     * Metode ini sekarang sangat sederhana karena tidak perlu membuka drawer.
     */
    public void logout() {
        System.out.println("Drawer selalu terbuka. Memulai proses logout...");
        try {
            // Langkah 1: Langsung tunggu hingga tombol 'Keluar' bisa diklik, lalu klik.
            // Ini adalah cara paling sabar untuk memastikan UI siap menerima input.
            System.out.println("Menunggu tombol 'Keluar' untuk bisa diklik...");
            wait.until(ExpectedConditions.elementToBeClickable(logoutMenuItem)).click();
            System.out.println("Tombol 'Keluar' berhasil diklik.");

        } catch (Exception e) {
            System.err.println("GAGAL mengklik tombol 'Keluar'. Pastikan ID 'com.example.eatstedi:id/nav_logout' sudah benar dan elemennya interaktif.");
            throw new RuntimeException("Tidak dapat menemukan atau mengklik tombol logout.", e);
        }

        // Langkah 2: Verifikasi bahwa logout berhasil dengan menunggu halaman login muncul kembali
        LoginPage loginPage = new LoginPage(driver);
        wait.until(driver -> loginPage.isLoginPageDisplayed());
        System.out.println("Logout berhasil, kembali ke halaman Login.");
    }
}