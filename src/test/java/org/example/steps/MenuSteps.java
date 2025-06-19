package org.example.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.example.pages.DashboardKasirPage;
import org.example.pages.InvoicePage;
import org.example.pages.LoginPage;
import org.example.pages.MenuPage;
import org.example.pages.OnBoardingPage;
import static org.junit.Assert.*;

public class MenuSteps extends BaseTest {

    private LoginPage loginPage;
    private MenuPage menuPage;
    private InvoicePage invoicePage;
    private OnBoardingPage onBoardingPage;

    private void initPages() {
        this.loginPage = new LoginPage(driver);
        this.menuPage = new MenuPage(driver);
        this.invoicePage = new InvoicePage(driver);
        this.onBoardingPage = new OnBoardingPage(driver);
    }

    @Given("menu {string} milik supplier {string} tersedia dengan stok ≥ {int}")
    public void menu_tersedia_dengan_stok_cukup(String menuName, String supplier, int stok) {
        initPages();
        assertTrue("Pre-condition failed: Menu '" + menuName + "' tidak tersedia di daftar.", menuPage.isMenuAvailable(menuName));
    }

    @Given("menu {string} milik supplier {string} tersedia dengan stok = {int}")
    public void menu_tersedia_dengan_stok_nol(String menuName, String supplier, int stok) {
        initPages();
        assertTrue("Pre-condition failed: Menu '" + menuName + "' tidak tersedia di daftar.", menuPage.isMenuAvailable(menuName));
    }

    @When("pengguna membuka halaman Daftar Menu")
    public void pengguna_membuka_halaman_daftar_menu() {
        initPages();
        new DashboardKasirPage(driver).navigateToMenu();
        assertTrue("Gagal membuka halaman Daftar Menu setelah navigasi.", menuPage.isMenuPageDisplayed());

        // Tunggu toast loading hilang sebelum melanjutkan test
        menuPage.waitForLoadingToastToDisappear();
    }

    @And("memilih menu {string} dan {string}")
    public void memilih_menu(String menu1, String menu2) {
        menuPage.selectMenu(menu1);
        menuPage.selectMenu(menu2);
    }

    @And("mengatur jumlah {string} menjadi {int} dan {string} menjadi {int}")
    public void mengatur_jumlah_menu(String menu1, int qty1, String menu2, int qty2) {
        menuPage.setMenuQuantity(menu1, qty1);
        menuPage.setMenuQuantity(menu2, qty2);
    }

    @When("pengguna memilih metode pembayaran {string}")
    public void pengguna_memilih_metode_pembayaran(String method) {
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
        assertTrue("Halaman invoice seharusnya ditampilkan setelah menekan Bayar.", invoicePage.isInvoiceDisplayed());
        assertFalse("Total pembayaran pada invoice tidak boleh kosong.", invoicePage.getTotalAmount().isEmpty());
        assertFalse("Tanggal order pada invoice tidak boleh kosong.", invoicePage.getOrderDate().isEmpty());
        invoicePage.closeInvoice();
    }

    @When("mencoba memilih menu {string}")
    public void mencoba_memilih_menu(String menuName) {
        // Tunggu sebentar untuk memastikan halaman sudah stable
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Mencoba memilih menu: " + menuName);
        menuPage.selectMenu(menuName);

        // Tunggu sebentar setelah klik untuk memastikan toast muncul
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Then("menu tidak ditambahkan ke order")
    public void menu_tidak_ditambahkan_ke_order() {
        // Tunggu sebentar sebelum mengecek
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertFalse("Menu 'Ayam Bakar' seharusnya tidak ada di panel order.", menuPage.isMenuInOrder("Ayam Bakar"));
    }

    @And("aplikasi menampilkan pesan {string}")
    public void aplikasi_menampilkan_pesan(String expectedMessage) {
        System.out.println("Mengecek toast dengan pesan: '" + expectedMessage + "'");

        // Panggil metode yang sudah diperbaiki dengan multiple strategy
        String actualMessage = menuPage.waitForToastMessageWithText(expectedMessage);

        // Assertion dengan pesan error
        assertTrue(
                "Pesan Toast yang diharapkan ('" + expectedMessage + "') tidak ditemukan. " +
                        "Pesan yang diterima: '" + actualMessage + "'",
                !actualMessage.isEmpty() && actualMessage.contains(expectedMessage)
        );

        System.out.println("Toast berhasil ditemukan: " + actualMessage);
    }

    @Given("menu {string} milik supplier {string} sudah ada di panel order")
    public void menu_sudah_ada_di_panel_order(String menuName, String supplier) {
        initPages();
        menuPage.selectMenu(menuName);
        assertTrue("Setup failed: Menu '" + menuName + "' tidak ada di panel order.", menuPage.isMenuInOrder(menuName));
    }

    @When("pengguna mengatur jumlah {string} menjadi {int}")
    public void pengguna_mengatur_jumlah_menjadi(String menuName, int quantity) {
        menuPage.setMenuQuantity(menuName, quantity);
    }

    @Then("menu {string} dihapus dari panel order")
    public void menu_dihapus_dari_panel_order(String menuName) {
        assertFalse("Menu '" + menuName + "' seharusnya sudah terhapus dari panel order.", menuPage.isMenuInOrder(menuName));
    }

    @Given("menu {string} milik supplier {string} sudah ada di panel order dengan jumlah {int}")
    public void menu_ada_di_order_dengan_jumlah(String menuName, String supplier, int qty) {
        initPages();
        menuPage.selectMenu(menuName);
        menuPage.setMenuQuantity(menuName, qty);
        assertTrue("Setup failed: Gagal mengatur jumlah menu.", menuPage.isMenuInOrder(menuName));
    }

    @When("memasukkan jumlah pembayaran lebih kecil dari total")
    public void memasukkan_jumlah_pembayaran_lebih_kecil_dari_total() {
        // Menghapus semua karakter non-digit kecuali koma, lalu mengganti koma dengan titik
        String totalText = menuPage.getTotalAmount().replaceAll("[^\\d,]", "").replace(",", ".");
        // Mengonversi ke double dan mengurangi 1000 untuk mendapatkan jumlah yang lebih kecil
        double totalAmount = Double.parseDouble(totalText);
        double smallerAmount = totalAmount - 1000;
        menuPage.enterPaymentAmount(String.valueOf((int)smallerAmount));
    }
}