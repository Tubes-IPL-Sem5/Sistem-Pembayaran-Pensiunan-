CREATE TABLE akun_pengguna (
    id_akun INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(16) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    peran ENUM('PENSIUNAN','HRD','KEUANGAN') NOT NULL
);

CREATE TABLE akun_hrd (
    id_hrd INT AUTO_INCREMENT PRIMARY KEY,
    id_akun INT NOT NULL,
    nama VARCHAR(100) NOT NULL,
    CONSTRAINT fk_akunhrd_akun
        FOREIGN KEY (id_akun) REFERENCES akun_pengguna(id_akun)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE akun_divisi_keuangan (
    id_keuangan INT AUTO_INCREMENT PRIMARY KEY,
    id_akun INT NOT NULL,
    nama VARCHAR(100) NOT NULL,
    CONSTRAINT fk_keu_akun
        FOREIGN KEY (id_akun) REFERENCES akun_pengguna(id_akun)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE akun_pensiunan (
    id_pensiunan INT AUTO_INCREMENT PRIMARY KEY,
    id_akun INT NOT NULL,
    id_hrd INT NOT NULL,
    nip VARCHAR(20) NOT NULL UNIQUE,
    nama VARCHAR(100) NOT NULL,
    tanggal_pensiun DATE NOT NULL,
    golongan ENUM('1','2','3','4') NOT NULL,
    masa_kerja INT NOT NULL,
    CONSTRAINT fk_pensiunan_akun
        FOREIGN KEY (id_akun) REFERENCES akun_pengguna(id_akun)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_pensiunan_hrd
        FOREIGN KEY (id_hrd) REFERENCES akun_hrd(id_hrd)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE gaji_pensiunan (
    id_gaji INT AUTO_INCREMENT PRIMARY KEY,
    id_pensiunan INT NOT NULL,
    tanggal DATE NOT NULL,
    jumlah_gaji DECIMAL(15,2) NOT NULL,
    status_perhitungan ENUM('BELUM','SELESAI','GAGAL') NOT NULL DEFAULT 'BELUM',
    CONSTRAINT fk_gaji_pensiunan
        FOREIGN KEY (id_pensiunan) REFERENCES akun_pensiunan(id_pensiunan)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE pembayaran (
    id_pembayaran INT AUTO_INCREMENT PRIMARY KEY,
    id_pensiunan INT NOT NULL,
    id_keuangan INT NOT NULL,
    tanggal DATE NOT NULL,
    jumlah DECIMAL(15,2) NOT NULL,
    status_pembayaran ENUM('BERHASIL','GAGAL','PENDING') NOT NULL DEFAULT 'PENDING',
    CONSTRAINT fk_bayar_pensiunan
        FOREIGN KEY (id_pensiunan) REFERENCES akun_pensiunan(id_pensiunan)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_bayar_keu
        FOREIGN KEY (id_keuangan) REFERENCES akun_divisi_keuangan(id_keuangan)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE history_pembayaran (
    id_history INT AUTO_INCREMENT PRIMARY KEY,
    id_pembayaran INT NOT NULL,
    id_pensiunan INT NOT NULL,
    tanggal DATE NOT NULL,
    jumlah DECIMAL(15,2) NOT NULL,
    status ENUM('BERHASIL','GAGAL') NOT NULL,
    CONSTRAINT fk_hist_bayar
        FOREIGN KEY (id_pembayaran) REFERENCES pembayaran(id_pembayaran)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_hist_pensiunan
        FOREIGN KEY (id_pensiunan) REFERENCES akun_pensiunan(id_pensiunan)
        ON UPDATE CASCADE ON DELETE RESTRICT
);
