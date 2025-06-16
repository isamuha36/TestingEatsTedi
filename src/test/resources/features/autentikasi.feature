# authentication.feature

Feature: Authentication

  Background:
    Given aplikasi dibuka

  @auth @critical
  Scenario: Login valid
    Given Splash screen ditampilkan
    When pengguna melewati onboarding
    And pengguna memasukkan username "cashier1" dan password "password"
    And pengguna menekan tombol "Login"
    Then pengguna berhasil login dengan role "Kasir"
    And pengguna diarahkan ke halaman Dashboard Kasir

  @auth @critical
  Scenario: Login invalid (password salah)
    Given Splash screen ditampilkan
    When pengguna melewati onboarding
    And pengguna memasukkan username "cashier1" dan password "wrong_password"
    And pengguna menekan tombol "Login"
    Then aplikasi menampilkan pesan error "Username atau password salah"
    And pengguna tetap di halaman Login
