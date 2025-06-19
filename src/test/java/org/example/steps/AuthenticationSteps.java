package org.example.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
// Perhatikan: 'import io.cucumber.java.After;' tidak lagi dibutuhkan di sini
import org.example.pages.DashboardKasirPage;
import org.example.pages.OnBoardingPage;
import org.example.pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import static org.junit.Assert.*;

public class AuthenticationSteps extends BaseTest {
    private OnBoardingPage onboardingPage;
    private LoginPage loginPage;
    private DashboardKasirPage dashboardPage;

    private void initPages() {
        onboardingPage = new OnBoardingPage(driver);
        loginPage = new LoginPage(driver);
        dashboardPage = new DashboardKasirPage(driver);
    }

    @Given("aplikasi dibuka")
    public void aplikasi_dibuka() {
        initPages();
        assertTrue("Aplikasi tidak berhasil dibuka", driver != null);
    }

    @When("pengguna melewati onboarding jika ditampilkan")
    public void pengguna_melewati_onboarding_jika_ditampilkan() {
        if (onboardingPage.isOnboardingScreenPresent()) {
            System.out.println("Onboarding screen terdeteksi, akan dilewati (skip).");
            onboardingPage.skipOnboarding();
        } else {
            System.out.println("Onboarding screen tidak ditemukan, menunggu halaman login.");
        }
        assertTrue("Gagal berada di halaman login setelah memeriksa onboarding.", loginPage.isLoginPageDisplayed());
    }

    @And("pengguna memasukkan username {string} dan password {string}")
    public void pengguna_memasukkan_username_dan_password(String username, String password) {
        System.out.println("Memasukan username dan password...");
        loginPage.login(username, password);
    }

    @And("pengguna menekan tombol {string}")
    public void pengguna_menekan_tombol(String buttonName) {
        System.out.println("Tombol " + buttonName + " ditekan.");
    }

    @Then("pengguna berhasil login dengan role {string}")
    public void pengguna_berhasil_login_dengan_role(String role) {
        List<WebElement> logActivityNav = driver.findElements(By.id("com.example.eatstedi:id/log_activity_nav"));
        if (role.equalsIgnoreCase("admin")) {
            assertTrue("Pengguna seharusnya admin, tetapi navigasi log aktivitas tidak ditemukan.", !logActivityNav.isEmpty());
        } else if (role.equalsIgnoreCase("kasir")) {
            assertTrue("Pengguna seharusnya kasir, tetapi navigasi log aktivitas ditemukan.", logActivityNav.isEmpty());
        }
    }

    @And("pengguna diarahkan ke halaman Dashboard Kasir")
    public void pengguna_diarahkan_ke_halaman_dashboard_kasir() {
        assertTrue("Pengguna tidak diarahkan ke Dashboard Kasir", dashboardPage.isDashboardActive());
    }

    @Then("aplikasi menampilkan pesan error {string}")
    public void aplikasi_menampilkan_pesan_error(String expectedMessage) {
        assertTrue("Pesan error '" + expectedMessage + "' tidak ditampilkan",
                loginPage.isErrorMessageDisplayed(expectedMessage));
    }

    @And("pengguna tetap di halaman Login")
    public void pengguna_tetap_di_halaman_login() {
        assertTrue("Pengguna tidak berada di halaman Login", loginPage.isLoginPageDisplayed());
    }

    @Given("pengguna sudah login sebagai Kasir")
    public void pengguna_sudah_login_sebagai_kasir() {
        initPages();

        if (onboardingPage.isOnboardingScreenPresent()) {
            onboardingPage.skipOnboarding();
        }

        // Pastikan di halaman login
        assertTrue("Tidak berada di halaman login setelah onboarding", loginPage.isLoginPageDisplayed());

        System.out.println("Melakukan login sebagai prasyarat (pre-condition)...");
        loginPage.login("cashier1", "password");

        // Tunggu sebentar untuk proses login
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Cek apakah berhasil keluar dari login page
        if (loginPage.isLoginPageDisplayed()) {
            System.out.println("Masih di login page, login mungkin gagal");
            // Coba sekali lagi
            loginPage.login("cashier1", "password");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Verifikasi login berhasil dengan fallback ke cek dashboard
        boolean loginSuccess = dashboardPage.isUserLoggedIn() || dashboardPage.isDashboardActive();
        assertTrue("Gagal login sebagai Kasir untuk prasyarat pengujian.", loginSuccess);

        System.out.println("Login prasyarat berhasil.");
    }

}