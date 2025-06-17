package org.example.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.example.pages.OnBoardingPage;
import org.example.pages.LoginPage;
import org.example.pages.MenuPage;
import static org.junit.Assert.*;

public class AuthenticationSteps extends BaseTest {

    private OnBoardingPage onboardingPage;
    private LoginPage loginPage;
    private MenuPage menuPage;

    // Method ini menginisialisasi semua Page Object yang dibutuhkan
    private void initPages() {
        onboardingPage = new OnBoardingPage(driver);
        loginPage = new LoginPage(driver);
        menuPage = new MenuPage(driver);
    }

    @Given("aplikasi dibuka")
    public void aplikasi_dibuka() {
        // Panggil initPages agar semua objek Page dibuat
        initPages();
        assertTrue("Aplikasi tidak berhasil dibuka", driver != null);
    }

    // Ini adalah implementasi untuk langkah yang 'undefined' tadi
    @When("pengguna melewati onboarding jika ditampilkan")
    public void pengguna_melewati_onboarding_jika_ditampilkan() {
        // Cek apakah halaman onboarding ada
        if (onboardingPage.isOnboardingScreenPresent()) {
            System.out.println("Onboarding screen terdeteksi, akan dilewati (skip).");
            onboardingPage.skipOnboarding();
        } else {
            System.out.println("Onboarding screen tidak ditemukan, menunggu halaman login.");
        }

        // Pastikan kita sudah di halaman login sebelum melanjutkan
        // Ini akan menunggu hingga 20 detik (sesuai update terakhir di LoginPage)
        assertTrue("Gagal berada di halaman login setelah memeriksa onboarding.", loginPage.isLoginPageDisplayed());
    }

    @And("pengguna memasukkan username {string} dan password {string}")
    public void pengguna_memasukkan_username_dan_password(String username, String password) {
        loginPage.login(username, password);
    }

    @And("pengguna menekan tombol {string}")
    public void pengguna_menekan_tombol(String buttonName) {
        // Logika klik sudah ada di method login(), jadi step ini hanya untuk kejelasan
        System.out.println("Tombol " + buttonName + " ditekan.");
    }

    @Then("pengguna berhasil login dengan role {string}")
    public void pengguna_berhasil_login_dengan_role(String role) {
        assertTrue("Gagal login, tidak diarahkan ke halaman Menu", menuPage.isMenuPageDisplayed());
    }

    @And("pengguna diarahkan ke halaman Dashboard Kasir")
    public void pengguna_diarahkan_ke_halaman_dashboard_kasir() {
        // Step ini hanya untuk kejelasan di Gherkin
        System.out.println("Pengguna berhasil diarahkan ke Dashboard/Menu.");
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

    // Ini adalah perbaikan untuk NullPointerException di skenario @menu
    @Given("pengguna sudah login sebagai Kasir")
    public void pengguna_sudah_login_sebagai_kasir() {
        // 1. Inisialisasi semua Page Objects terlebih dahulu
        initPages();

        // 2. Panggil langkah yang sudah robust untuk melewati onboarding
        pengguna_melewati_onboarding_jika_ditampilkan();

        // 3. Lanjutkan sisa langkah login
        pengguna_memasukkan_username_dan_password("cashier1", "password");
        pengguna_berhasil_login_dengan_role("Kasir");
    }
}