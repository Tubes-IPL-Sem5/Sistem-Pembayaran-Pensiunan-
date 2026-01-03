package com.mycompany.pensiunan.model;

import javafx.beans.property.*;

public class Keuangan {
    private int idTransaksi;
    private int idPensiunan; // FIELD BARU: Penting untuk Foreign Key database

    private final StringProperty nama;
    private final StringProperty nip;
    private final StringProperty tanggalPensiun;
    private final StringProperty nominalLalu;
    private final StringProperty nominalAkan;
    private final IntegerProperty golongan;
    private final StringProperty status;

    // Constructor Updated
    public Keuangan(int idTransaksi, int idPensiunan, String nama, String nip, String tanggal, double nominalLalu, double nominalAkan, int golongan, String status) {
        this.idTransaksi = idTransaksi;
        this.idPensiunan = idPensiunan;

        this.nama = new SimpleStringProperty(nama);
        this.nip = new SimpleStringProperty(nip);
        this.tanggalPensiun = new SimpleStringProperty(tanggal);

        // Format angka ke mata uang sederhana (tanpa Rp agar mudah dibaca)
        this.nominalLalu = new SimpleStringProperty(String.format("%,.0f", nominalLalu));
        this.nominalAkan = new SimpleStringProperty(nominalAkan == 0 ? "0" : String.format("%,.0f", nominalAkan));

        this.golongan = new SimpleIntegerProperty(golongan);

        if (status == null || status.trim().isEmpty()) {
            this.status = new SimpleStringProperty("Menunggu");
        } else {
            this.status = new SimpleStringProperty(status);
        }
    }

    // Getters
    public int getIdTransaksi() { return idTransaksi; }
    public int getIdPensiunan() { return idPensiunan; } // Getter Baru

    public StringProperty namaProperty() { return nama; }
    public StringProperty nipProperty() { return nip; }
    public StringProperty tanggalProperty() { return tanggalPensiun; }
    public StringProperty nominalLaluProperty() { return nominalLalu; }
    public StringProperty nominalAkanProperty() { return nominalAkan; }
    public StringProperty statusProperty() { return status; }
    public IntegerProperty golonganProperty() { return golongan; }

    public int getGolongan() { return golongan.get(); }
    public String getNama() { return nama.get(); }

    // Setters
    public void setNominalAkan(String value) { this.nominalAkan.set(value); }
    public void setStatus(String value) { this.status.set(value); }
}