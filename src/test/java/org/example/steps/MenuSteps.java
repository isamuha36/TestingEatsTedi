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

    // Deklarasi Page Objects
    private LoginPage loginPage;
    private MenuPage menuPage;
    private InvoicePage invoicePage;
    private OnBoardingPage onBoardingPage;

    // PERBAIKAN: Hapus constructor, ganti dengan method initPages
    private void initPages() {
        this.loginPage = new LoginPage(driver);
        this.menuPage = new MenuPage(driver);
        this.invoicePage = new InvoicePage(driver);
        this.onBoardingPage = new OnBoardingPage(driver);
    }

    @Given("menu {string} milik supplier {string} tersedia dengan stok ≥ {int}")
    public void menu_tersedia_dengan_stok_cukup(String menuName, String supplier, int stok) {
        // PERBAIKAN: Inisialisasi pages di awal step
        initPages();
        assertTrue("Pre-condition failed: Menu " + menuName + " tidak tersedia", menuPage.isMenuAvailable(menuName));
    }

    @Given("menu {string} milik supplier {string} tersedia dengan stok = {int}")
    public void menu_tersedia_dengan_stok_nol(String menuName, String supplier, int stok) {
        initPages();
        assertTrue("Pre-condition failed: Menu " + menuName + " tidak tersedia", menuPage.isMenuAvailable(menuName));
    }

    @When("pengguna membuka halaman Daftar Menu")
    public void pengguna_membuka_halaman_daftar_menu() {
        initPages();
        assertTrue("Halaman Menu tidak ditampilkan", menuPage.isMenuPageDisplayed());
    }

    // Sisa method tidak perlu `initPages()` karena sudah dipanggil di step @Given sebelumnya
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
        invoicePage.closeInvoice();
    }

    @When("mencoba memilih menu {string}")
    public void mencoba_memilih_menu(String menuName) {
        menuPage.selectMenu(menuName);
    }

    @Then("menu tidak ditambahkan ke order")
    public void menu_tidak_ditambahkan_ke_order() {
        assertFalse("Menu Ayam Bakar seharusnya tidak ada di panel order", menuPage.isMenuInOrder("Ayam Bakar"));
    }

    @And("aplikasi menampilkan pesan {string}")
    public void aplikasi_menampilkan_pesan(String message) {
        assertTrue("Pesan yang ditampilkan tidak sesuai: " + menuPage.getToastMessage(),
                menuPage.getToastMessage().contains(message));
    }

    @Given("menu {string} milik supplier {string} sudah ada di panel order")
    public void menu_sudah_ada_di_panel_order(String menuName, String supplier) {
        initPages();
        menuPage.selectMenu(menuName);
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
        initPages();
        menuPage.selectMenu(menuName);
        menuPage.setMenuQuantity(menuName, qty);
        assertTrue("Setup failed: Gagal mengatur jumlah menu", menuPage.isMenuInOrder(menuName));
    }

    @When("memasukkan jumlah pembayaran lebih kecil dari total")
    public void memasukkan_jumlah_pembayaran_lebih_kecil_dari_total() {
        String totalText = menuPage.getTotalAmount().replaceAll("[^\\d,]", "").replace(",", "."); // "Rp50.000,00" -> "50000.00"
        double totalAmount = Double.parseDouble(totalText);
        double smallerAmount = totalAmount - 1000;
        menuPage.enterPaymentAmount(String.valueOf((int)smallerAmount));
    }
}