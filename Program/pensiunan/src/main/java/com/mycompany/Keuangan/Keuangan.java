package com.pensiun.keuangan;

import javafx.beans.property.*;

public class Keuangan {
    private final StringProperty nama;
    private final StringProperty nip;
    private final StringProperty tanggalPensiun;
    private final StringProperty nominalLalu;
    private final StringProperty nominalAkan;
    private final IntegerProperty golongan;
    private final StringProperty status;

    public Keuangan(String nama, String nip, String tanggal, String nominalLalu, int golongan) {
        this.nama = new SimpleStringProperty(nama);
        this.nip = new SimpleStringProperty(nip);
        this.tanggalPensiun = new SimpleStringProperty(tanggal);
        this.nominalLalu = new SimpleStringProperty(nominalLalu);
        this.nominalAkan = new SimpleStringProperty("0.000");
        this.golongan = new SimpleIntegerProperty(golongan);
        this.status = new SimpleStringProperty("Menunggu"); // State awal
    }

    public StringProperty namaProperty() { return nama; }
    public StringProperty nipProperty() { return nip; }
    public StringProperty tanggalProperty() { return tanggalPensiun; }
    public StringProperty nominalLaluProperty() { return nominalLalu; }
    public StringProperty nominalAkanProperty() { return nominalAkan; }
    public StringProperty statusProperty() { return status; }
    public IntegerProperty golonganProperty() { return golongan; }

    public int getGolongan() { return golongan.get(); }
    public String getNama() { return nama.get(); }
    public void setNominalAkan(String value) { this.nominalAkan.set(value); }
    public void setStatus(String value) { this.status.set(value); }
}