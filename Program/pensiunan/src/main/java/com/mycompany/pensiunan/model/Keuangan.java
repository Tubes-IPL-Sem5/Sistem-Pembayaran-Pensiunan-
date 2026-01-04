package com.mycompany.pensiunan.model;

import javafx.beans.property.*;

public class Keuangan {

    private final int idPensiunan;
    private final StringProperty nama;
    private final StringProperty nip;
    private final StringProperty nominalLalu;
    private final StringProperty nominalAkan;
    private final StringProperty status;
    private final IntegerProperty golongan;

    public Keuangan(int idPensiunan, String nama, String nip,
                    double nominalLalu, double nominalAkan,
                    int golongan, String status) {

        this.idPensiunan = idPensiunan;
        this.nama = new SimpleStringProperty(nama);
        this.nip = new SimpleStringProperty(nip);
        this.nominalLalu = new SimpleStringProperty(format(nominalLalu));
        this.nominalAkan = new SimpleStringProperty(format(nominalAkan));
        this.golongan = new SimpleIntegerProperty(golongan);
        this.status = new SimpleStringProperty(status);
    }

    private String format(double v) {
        return String.format("%,.0f", v).replace(',', '.');
    }

    public int getIdPensiunan() { return idPensiunan; }
    public int getGolongan() { return golongan.get(); }
    public String getNama() {
        return nama.get();
    }


    public StringProperty namaProperty() { return nama; }
    public StringProperty nipProperty() { return nip; }
    public StringProperty nominalLaluProperty() { return nominalLalu; }
    public StringProperty nominalAkanProperty() { return nominalAkan; }
    public StringProperty statusProperty() { return status; }

    public void setStatus(String s) { status.set(s); }
    public void setNominalAkan(String n) { nominalAkan.set(n); }
}
