package org.example.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.example.pages.DashboardKasirPage;

public class Hooks extends BaseTest {
    @Before
    public void beforeScenario(Scenario scenario) {
        System.out.println("====================================================");
        System.out.println("MEMULAI SKENARIO: " + scenario.getName());
        System.out.println("====================================================");
        try {
            setUp();
        } catch (Exception e) {
            // Memberikan pesan error yang jelas jika setup gagal
            throw new RuntimeException("Gagal melakukan setup driver: " + e.getMessage(), e);
        }
    }

    // MODIFIKASI TOTAL PADA METODE @After
    @After
    public void afterScenario(Scenario scenario) {
        System.out.println("----------------------------------------------------");
        System.out.println("SELESAI SKENARIO: " + scenario.getName() + " | STATUS: " + scenario.getStatus());
        System.out.println("----------------------------------------------------");

        // Logika Logout ditambahkan di sini, sebelum driver ditutup
        if (driver != null) {
            try {
                // Inisialisasi page object yang dibutuhkan untuk logout
                DashboardKasirPage dashboardPage = new DashboardKasirPage(driver);

                // MODIFIKASI: Menggunakan metode isUserLoggedIn() yang lebih andal.
                // Ini akan mendeteksi status login baik di halaman Dasbor, Menu, atau halaman lainnya.
                if (dashboardPage.isUserLoggedIn()) {
                    System.out.println("Pengguna terdeteksi login (navigation drawer ditemukan). Memulai proses logout...");
                    dashboardPage.logout();
                } else {
                    System.out.println("Pengguna tidak dalam status login (navigation drawer tidak ditemukan), proses logout dilewati.");
                }
            } catch (Exception e) {
                // Jangan sampai proses teardown gagal hanya karena logout error.
                // Cukup catat errornya untuk debugging.
                System.err.println("Terjadi kesalahan saat mencoba logout: " + e.getMessage());
            }
        }

        // Panggil tearDown untuk menutup driver setelah semuanya selesai
        System.out.println("Menutup driver...");
        tearDown();
        System.out.println("====================================================\n");
    }
}