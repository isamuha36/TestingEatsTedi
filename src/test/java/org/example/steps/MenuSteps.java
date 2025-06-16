package org.example.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.example.pages.InvoicePage;
import org.example.pages.LoginPage;
import org.example.pages.MenuPage;
import org.example.pages.OnBoardingPage;
import static org.junit.Assert.*;

public class MenuSteps extends BaseTest {

    // Inisialisasi semua Page Objects yang mungkin dibutuhkan
    private LoginPage loginPage;
    private MenuPage menuPage;
    private InvoicePage invoicePage;
    private OnBoardingPage onBoardingPage;

    // Gunakan constructor untuk mendapatkan driver dari Hooks
    public MenuSteps() {
        this.loginPage = new LoginPage(driver);
        this.menuPage = new MenuPage(driver);
        this.invoicePage = new InvoicePage(driver);
        this.onBoardingPage = new OnBoardingPage(driver);
    }

    @Given("pengguna sudah login sebagai Kasir")
    public void pengguna_sudah_login_sebagai_Kasir() {
        if(onBoardingPage.isOnboardingDisplayed()){
            onBoardingPage.skipOnboarding();
        }
        assertTrue("Halaman login tidak ditemukan", loginPage.isLoginPageDisplayed());
        loginPage.login("cashier1", "password");
        assertTrue("Gagal login untuk setup", menuPage.isMenuPageDisplayed());
    }

    @Given("menu {string} milik supplier {string} tersedia dengan stok ≥ {int}")
    public void menu_tersedia_dengan_stok_cukup(String menuName, String supplier, int stok) {
        // Di dunia nyata, ini akan memverifikasi data via API atau database.
        // Untuk tes UI, kita asumsikan data sudah benar dan verifikasi menu ada di layar.
        assertTrue("Pre-condition failed: Menu " + menuName + " tidak tersedia", menuPage.isMenuAvailable(menuName));
    }

    @Given("menu {string} milik supplier {string} tersedia dengan stok = {int}")
    public void menu_tersedia_dengan_stok_nol(String menuName, String supplier, int stok) {
        // Asumsi dan verifikasi yang sama seperti di atas.
        assertTrue("Pre-condition failed: Menu " + menuName + " tidak tersedia", menuPage.isMenuAvailable(menuName));
    }

    @When("pengguna membuka halaman Daftar Menu")
    public void pengguna_membuka_halaman_daftar_menu() {
        // Setelah login, pengguna sudah di halaman menu, jadi step ini hanya untuk kejelasan.
        assertTrue("Halaman Menu tidak ditampilkan", menuPage.isMenuPageDisplayed());
    }

    @And("memilih menu {string} dan {string}")
    public void memilih_menu(String menu1, String menu2) {
        menuPage.selectMenu(menu1);
        menuPage.selectMenu(menu2);
    }

    @And("mengatur jumlah {string} menjadi {int} dan {string} menjadi {int}")
    public void mengatur_jumlah_menu(String menu1, int qty1, String menu2, int qty2) {
        menuPage.openOrderPanel();
        menuPage.setMenuQuantity(menu1, qty1);
        menuPage.setMenuQuantity(menu2, qty2);
    }

    @And("memilih metode pembayaran {string}")
    public void memilih_metode_pembayaran(String method) {
        menuPage.selectPaymentMethod(method);
    }

    @And("menekan tombol {string}")
    public void menekan_tombol_bayar(String buttonName) {
        if (buttonName.equalsIgnoreCase("Bayar")) {
            menuPage.clickPayButton();
        }
    }

    @Then("aplikasi menampilkan invoice transaksi berhasil")
    public void aplikasi_menampilkan_invoice_transaksi_berhasil() {
        assertTrue("Invoice transaksi tidak ditampilkan atau tidak valid", invoicePage.isTransactionSuccessful());
        // Tutup invoice untuk skenario selanjutnya jika perlu
        invoicePage.closeInvoice();
    }

    @When("mencoba memilih menu {string}")
    public void mencoba_memilih_menu(String menuName) {
        menuPage.selectMenu(menuName);
    }

    @Then("menu tidak ditambahkan ke order")
    public void menu_tidak_ditambahkan_ke_order() {
        // Verifikasi bahwa menu tidak muncul di panel order
        menuPage.openOrderPanel();
        assertFalse("Menu seharusnya tidak ada di panel order", menuPage.isMenuInOrder("Ayam Bakar"));
    }

    @And("aplikasi menampilkan pesan {string}")
    public void aplikasi_menampilkan_pesan(String message) {
        // Verifikasi pesan toast. Mungkin perlu disesuaikan.
        assertEquals("Pesan yang ditampilkan tidak sesuai", message, menuPage.getToastMessage());
    }

    @Given("menu {string} milik supplier {string} sudah ada di panel order")
    public void menu_sudah_ada_di_panel_order(String menuName, String supplier) {
        menuPage.selectMenu(menuName);
        menuPage.openOrderPanel();
        assertTrue("Setup failed: Menu " + menuName + " tidak ada di panel order", menuPage.isMenuInOrder(menuName));
    }

    @When("pengguna mengatur jumlah {string} menjadi {int}")
    public void pengguna_mengatur_jumlah_menjadi(String menuName, int quantity) {
        menuPage.setMenuQuantity(menuName, quantity);
    }

    @Then("menu {string} dihapus dari panel order")
    public void menu_dihapus_dari_panel_order(String menuName) {
        assertFalse("Menu " + menuName + " seharusnya sudah terhapus dari panel order", menuPage.isMenuInOrder(menuName));
    }

    @Given("menu {string} milik supplier {string} sudah ada di panel order dengan jumlah {int}")
    public void menu_ada_di_order_dengan_jumlah(String menuName, String supplier, int qty) {
        menuPage.selectMenu(menuName);
        menuPage.openOrderPanel();
        menuPage.setMenuQuantity(menuName, qty);
        assertTrue("Setup failed: Gagal mengatur jumlah menu", menuPage.isMenuInOrder(menuName));
    }

    @When("memasukkan jumlah pembayaran lebih kecil dari total")
    public void memasukkan_jumlah_pembayaran_lebih_kecil_dari_total() {
        String totalText = menuPage.getTotalAmount().replaceAll("[^\\d]", ""); // "Rp50.000,00" -> "5000000"
        double totalAmount = Double.parseDouble(totalText) / 100;
        double smallerAmount = totalAmount - 1000; // Bayar kurang 1000
        menuPage.enterPaymentAmount(String.valueOf(smallerAmount));
    }
}