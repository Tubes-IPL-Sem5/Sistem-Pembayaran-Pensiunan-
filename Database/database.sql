-- =====================================================
-- DATABASE 3NF SISTEM PEMBAYARAN PENSIUNAN PT GRAHADI
-- =====================================================

CREATE DATABASE IF NOT EXISTS db_pembayaran_pensiunan
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE db_pembayaran_pensiunan;

-- =========================
-- TABEL MASTER UMUM
-- =========================

CREATE TABLE role_user (
    id_role      INT AUTO_INCREMENT PRIMARY KEY,
    nama_role    VARCHAR(50) NOT NULL UNIQUE
    -- contoh isi: 'PENSIUNAN', 'HRD', 'KEUANGAN', 'ADMIN'
);

CREATE TABLE status_akun (
    id_status_akun INT AUTO_INCREMENT PRIMARY KEY,
    nama_status    VARCHAR(30) NOT NULL UNIQUE
    -- contoh isi: 'BELUM_AKTIF', 'AKTIF', 'DIBLOKIR', 'DIHAPUS'
);

CREATE TABLE status_pensiunan (
    id_status_pensiunan INT AUTO_INCREMENT PRIMARY KEY,
    nama_status         VARCHAR(30) NOT NULL UNIQUE
    -- contoh isi: 'AKTIF', 'PENSIUN', 'NON_AKTIF'
);

CREATE TABLE bank (
    id_bank      INT AUTO_INCREMENT PRIMARY KEY,
    nama_bank    VARCHAR(100) NOT NULL,
    kode_bank    VARCHAR(10) UNIQUE
);

CREATE TABLE golongan (
    id_golongan     INT AUTO_INCREMENT PRIMARY KEY,
    kode_golongan   VARCHAR(10) NOT NULL UNIQUE,
    keterangan      VARCHAR(100)
);

CREATE TABLE status_dokumen (
    id_status_dokumen INT AUTO_INCREMENT PRIMARY KEY,
    nama_status       VARCHAR(30) NOT NULL UNIQUE
    -- contoh isi: 'BELUM_DIUPLOAD', 'DIUPLOAD', 'VALID', 'INVALID'
);

CREATE TABLE jenis_dokumen (
    id_jenis_dokumen INT AUTO_INCREMENT PRIMARY KEY,
    nama_jenis       VARCHAR(50) NOT NULL UNIQUE
    -- contoh isi: 'KTP', 'KK', 'SK PENSIUN', dll
);

CREATE TABLE metode_pembayaran (
    id_metode_pembayaran INT AUTO_INCREMENT PRIMARY KEY,
    nama_metode          VARCHAR(50) NOT NULL UNIQUE
    -- contoh isi: 'TRANSFER', 'TUNAI'
);

CREATE TABLE status_pembayaran (
    id_status_pembayaran INT AUTO_INCREMENT PRIMARY KEY,
    nama_status          VARCHAR(30) NOT NULL UNIQUE
    -- contoh isi: 'LUNAS', 'GAGAL', 'PENDING'
);

-- =========================
-- TABEL USER
-- =========================

CREATE TABLE users (
    id_user        INT AUTO_INCREMENT PRIMARY KEY,
    nama_lengkap   VARCHAR(100) NOT NULL,
    email          VARCHAR(100) NOT NULL UNIQUE,
    password_hash  VARCHAR(255) NOT NULL,
    id_role        INT NOT NULL,
    id_status_akun INT NOT NULL,
    created_at     DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_users_role
        FOREIGN KEY (id_role) REFERENCES role_user(id_role)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_users_status_akun
        FOREIGN KEY (id_status_akun) REFERENCES status_akun(id_status_akun)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

-- =========================
-- TABEL INTI: PENSIUNAN
-- =========================

CREATE TABLE pensiunan (
    id_pensiunan       INT AUTO_INCREMENT PRIMARY KEY,
    nip                VARCHAR(30) NOT NULL UNIQUE,
    nama_pensiunan     VARCHAR(100) NOT NULL,
    tanggal_lahir      DATE,
    tanggal_pensiun    DATE NOT NULL,
    masa_kerja_tahun   INT NOT NULL,
    id_golongan        INT NOT NULL,
    id_status_pensiunan INT NOT NULL,
    id_bank            INT,
    no_rekening        VARCHAR(50),
    alamat             TEXT,
    id_user            INT,  -- akun login milik pensiunan (opsional)
    CONSTRAINT fk_pensiunan_golongan
        FOREIGN KEY (id_golongan) REFERENCES golongan(id_golongan)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_pensiunan_status
        FOREIGN KEY (id_status_pensiunan) REFERENCES status_pensiunan(id_status_pensiunan)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_pensiunan_bank
        FOREIGN KEY (id_bank) REFERENCES bank(id_bank)
        ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT fk_pensiunan_user
        FOREIGN KEY (id_user) REFERENCES users(id_user)
        ON UPDATE CASCADE ON DELETE SET NULL
);

-- =========================
-- TABEL PERIODE GAJI
-- =========================

CREATE TABLE periode (
    id_periode      INT AUTO_INCREMENT PRIMARY KEY,
    bulan           TINYINT NOT NULL,   -- 1-12
    tahun           SMALLINT NOT NULL,
    tanggal_mulai   DATE,
    tanggal_selesai DATE,
    UNIQUE (bulan, tahun)
);

-- =========================
-- PARAMETER PERHITUNGAN GAJI
-- =========================
-- Satu kombinasi golongan + rentang masa kerja -> 1 baris parameter

CREATE TABLE parameter_gaji (
    id_parameter       INT AUTO_INCREMENT PRIMARY KEY,
    id_golongan        INT NOT NULL,
    masa_kerja_min     INT NOT NULL,
    masa_kerja_max     INT NOT NULL,
    gaji_pokok         DECIMAL(18,2) NOT NULL,
    tunjangan_default  DECIMAL(18,2) DEFAULT 0,
    CONSTRAINT fk_param_golongan
        FOREIGN KEY (id_golongan) REFERENCES golongan(id_golongan)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

-- =========================
-- TABEL GAJI PENSIUNAN
-- =========================

CREATE TABLE gaji_pensiunan (
    id_gaji          INT AUTO_INCREMENT PRIMARY KEY,
    id_pensiunan     INT NOT NULL,
    id_periode       INT NOT NULL,
    id_parameter     INT,
    gaji_pokok       DECIMAL(18,2) NOT NULL,
    tunjangan        DECIMAL(18,2) DEFAULT 0,
    potongan         DECIMAL(18,2) DEFAULT 0,
    total_gaji       DECIMAL(18,2) NOT NULL,
    tanggal_hitung   DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_gaji_pensiunan
        FOREIGN KEY (id_pensiunan) REFERENCES pensiunan(id_pensiunan)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_gaji_periode
        FOREIGN KEY (id_periode) REFERENCES periode(id_periode)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_gaji_parameter
        FOREIGN KEY (id_parameter) REFERENCES parameter_gaji(id_parameter)
        ON UPDATE CASCADE ON DELETE SET NULL,
    UNIQUE (id_pensiunan, id_periode)  -- satu gaji per pensiunan per periode
);

-- =========================
-- TABEL DOKUMEN PENSIUNAN
-- =========================

CREATE TABLE dokumen_pensiunan (
    id_dokumen        INT AUTO_INCREMENT PRIMARY KEY,
    id_pensiunan      INT NOT NULL,
    id_jenis_dokumen  INT NOT NULL,
    nama_file_asli    VARCHAR(255) NOT NULL,
    path_file         VARCHAR(255) NOT NULL,
    id_status_dokumen INT NOT NULL,
    uploaded_at       DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_dokumen_pensiunan
        FOREIGN KEY (id_pensiunan) REFERENCES pensiunan(id_pensiunan)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_dokumen_jenis
        FOREIGN KEY (id_jenis_dokumen) REFERENCES jenis_dokumen(id_jenis_dokumen)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_dokumen_status
        FOREIGN KEY (id_status_dokumen) REFERENCES status_dokumen(id_status_dokumen)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

-- =========================
-- TABEL HISTORI PEMBAYARAN
-- =========================

CREATE TABLE histori_pembayaran (
    id_histori           INT AUTO_INCREMENT PRIMARY KEY,
    id_pensiunan         INT NOT NULL,
    id_gaji              INT NOT NULL,
    id_metode_pembayaran INT NOT NULL,
    id_status_pembayaran INT NOT NULL,
    tanggal_bayar        DATETIME NOT NULL,
    jumlah_bayar         DECIMAL(18,2) NOT NULL,
    keterangan           VARCHAR(255),
    created_at           DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_histori_pensiunan
        FOREIGN KEY (id_pensiunan) REFERENCES pensiunan(id_pensiunan)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_histori_gaji
        FOREIGN KEY (id_gaji) REFERENCES gaji_pensiunan(id_gaji)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_histori_metode
        FOREIGN KEY (id_metode_pembayaran) REFERENCES metode_pembayaran(id_metode_pembayaran)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_histori_status_bayar
        FOREIGN KEY (id_status_pembayaran) REFERENCES status_pembayaran(id_status_pembayaran)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

-- =========================
-- TABEL LOG AKTIVITAS
-- =========================

CREATE TABLE log_aktivitas (
    id_log        INT AUTO_INCREMENT PRIMARY KEY,
    id_user       INT,
    aktivitas     VARCHAR(255) NOT NULL,
    waktu_log     DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_log_user
        FOREIGN KEY (id_user) REFERENCES users(id_user)
        ON UPDATE CASCADE ON DELETE SET NULL
);
