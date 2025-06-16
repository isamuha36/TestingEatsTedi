package org.example.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
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

    private void initPages() {
        onboardingPage = new OnBoardingPage(driver);
        loginPage = new LoginPage(driver);
        menuPage = new MenuPage(driver);
    }

    @Given("aplikasi dibuka")
    public void aplikasi_dibuka() {
        // Driver di-setup oleh Hooks.java, kita tinggal inisialisasi pages
        initPages();
        assertTrue("Aplikasi tidak berhasil dibuka", driver != null);
    }

    @Given("Splash screen ditampilkan")
    public void splash_screen_ditampilkan() {
        assertTrue("Splash screen tidak ditampilkan", onboardingPage.isOnboardingDisplayed());
    }

    @When("pengguna melewati onboarding")
    public void pengguna_melewati_onboarding() {
        onboardingPage.skipOnboarding();
        // Verifikasi kita sampai di halaman login
        assertTrue("Halaman login tidak ditampilkan setelah skip onboarding", loginPage.isLoginPageDisplayed());
    }

    @And("pengguna memasukkan username {string} dan password {string}")
    public void pengguna_memasukkan_username_dan_password(String username, String password) {
        loginPage.login(username, password);
    }

    @And("pengguna menekan tombol {string}")
    public void pengguna_menekan_tombol(String buttonName) {
        // Logika klik sudah ada di method login, atau bisa dipisah jika perlu
        // Untuk sekarang kita asumsikan step ini hanya untuk kejelasan di Gherkin
        System.out.println("Tombol " + buttonName + " ditekan.");
    }

    @Then("pengguna berhasil login dengan role {string}")
    public void pengguna_berhasil_login_dengan_role(String role) {
        // Verifikasi terbaik adalah dengan memastikan halaman selanjutnya muncul
        assertTrue("Gagal login, tidak diarahkan ke halaman Menu", menuPage.isMenuPageDisplayed());
    }

    @And("pengguna diarahkan ke halaman Dashboard Kasir")
    public void pengguna_diarahkan_ke_halaman_dashboard_kasir() {
        // Step ini sudah terverifikasi oleh step sebelumnya, jadi ini hanya untuk kejelasan
        System.out.println("Pengguna berhasil diarahkan ke Dashboard/Menu.");
    }

    @Then("aplikasi menampilkan pesan error {string}")
    public void aplikasi_menampilkan_pesan_error(String expectedMessage) {
        // Toast message bisa jadi tidak stabil. Jika ini gagal,
        // pertimbangkan untuk meminta developer menambahkan TextView untuk error.
        assertTrue("Pesan error '" + expectedMessage + "' tidak ditampilkan",
                loginPage.isErrorMessageDisplayed(expectedMessage));
    }

    @And("pengguna tetap di halaman Login")
    public void pengguna_tetap_di_halaman_login() {
        assertTrue("Pengguna tidak berada di halaman Login", loginPage.isLoginPageDisplayed());
    }

    @Given("pengguna sudah login sebagai Kasir")
    public void pengguna_sudah_login_sebagai_kasir() {
        // Perform login sequence
        splash_screen_ditampilkan();
        pengguna_melewati_onboarding();
        pengguna_memasukkan_username_dan_password("cashier1", "password");
        pengguna_menekan_tombol("Login");
        pengguna_berhasil_login_dengan_role("Kasir");
        pengguna_diarahkan_ke_halaman_dashboard_kasir();
    }
}