package com.mycompany.pensiunan.model;

public class Gaji {
    private final String nama;
    private final String nip;
    private final String tanggal;
    private final double jumlah;
    private final String golongan;
    private final String status;

    public Gaji(String nama, String nip, String tanggal,
                double jumlah, String golongan, String status) {
        this.nama = nama;
        this.nip = nip;
        this.tanggal = tanggal;
        this.jumlah = jumlah;
        this.golongan = golongan;
        this.status = status;
    }

    public String getNama() { return nama; }
    public String getNip() { return nip; }
    public String getTanggal() { return tanggal; }
    public double getJumlah() { return jumlah; }
    public String getGolongan() { return golongan; }
    public String getStatus() { return status; }
}
