Feature: Menu & Transaksi Pembayaran

  Background:
    Given pengguna sudah login sebagai Kasir
    And pengguna membuka halaman Daftar Menu

  @menu @critical
  Scenario: Transaksi pembayaran valid (QRIS)
    Given menu "Nasi Goreng" milik supplier "Putri" tersedia dengan stok ≥ 3
    And menu "Ayam Geprek" milik supplier "Naila" tersedia dengan stok ≥ 3
    When memilih menu "Nasi Goreng" dan "Ayam Geprek"
    And mengatur jumlah "Nasi Goreng" menjadi 3 dan "Ayam Geprek" menjadi 2
    And pengguna memilih metode pembayaran "QRIS"
    And menekan tombol "Bayar"
    Then aplikasi menampilkan invoice transaksi berhasil

  @menu @critical
  Scenario: Memilih menu invalid (stok habis)
    Given menu "Ayam Bakar" milik supplier "Reza" tersedia dengan stok = 0
    When mencoba memilih menu "Ayam Bakar"
    Then menu tidak ditambahkan ke order
    And aplikasi menampilkan pesan "Maaf, menu sudah habis"

  @menu @critical
  Scenario: Mengatur jumlah order menu invalid (0)
    Given menu "Nasi Goreng" milik supplier "Putri" sudah ada di panel order
    When pengguna mengatur jumlah "Nasi Goreng" menjadi 0
    Then menu "Nasi Goreng" dihapus dari panel order

  @menu @critical
  Scenario: Transaksi pembayaran invalid (cash tidak cukup)
    Given menu "Nasi Goreng" milik supplier "Putri" sudah ada di panel order dengan jumlah 2
    When pengguna memilih metode pembayaran "Cash"
    And memasukkan jumlah pembayaran lebih kecil dari total
    And menekan tombol "Bayar"
    Then aplikasi menampilkan pesan "Jumlah pembayaran kurang"