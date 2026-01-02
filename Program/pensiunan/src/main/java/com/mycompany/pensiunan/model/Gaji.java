package com.mycompany.pensiunan.model;

public class Gaji {
    private String tanggal, keterangan;
    private double jumlah;

    public Gaji(String tgl, String ket, double jml) {
        this.tanggal = tgl;
        this.keterangan = ket;
        this.jumlah = jml;
    }
    public String getTanggal() { return tanggal; }
    public String getKeterangan() { return keterangan; }
    public double getJumlah() { return jumlah; }
}