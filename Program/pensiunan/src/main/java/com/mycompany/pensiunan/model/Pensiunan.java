
package com.mycompany.pensiunan.model;

import java.time.LocalDate;

public class Pensiunan {
    private int idPensiunan;
    private int idAkun;
    private int idHrd;
    private String nip;
    private String nama;
    private String golongan;
    private int masaKerja;
    private LocalDate tanggalPensiun;
    
    public Pensiunan(String nip, String nama, String golongan, int masaKerja, LocalDate tanggalPensiun) {
        this.nip = nip;
        this.nama = nama;
        this.golongan = golongan;
        this.masaKerja = masaKerja;
        this.tanggalPensiun = tanggalPensiun;
    }

    public Pensiunan(int idPensiunan, String nip, String nama, String golongan, int masaKerja, LocalDate tanggalPensiun) {
        this.idPensiunan = idPensiunan;
        this.nip = nip;
        this.nama = nama;
        this.golongan = golongan;
        this.masaKerja = masaKerja;
        this.tanggalPensiun = tanggalPensiun;
    }
    
    public Pensiunan(int idAkun, int idHrd, String nip, String nama,
                     String golongan, int masaKerja, LocalDate tanggalPensiun) {
        this.idAkun = idAkun;
        this.idHrd = idHrd;
        this.nip = nip;
        this.nama = nama;
        this.golongan = golongan;
        this.masaKerja = masaKerja;
        this.tanggalPensiun = tanggalPensiun;
    }

    public int getIdAkun() { return idAkun; }
    public int getIdHrd() { return idHrd; }
    public String getNip() { return nip; }
    public String getNama() { return nama; }
    public String getGolongan() { return golongan; }
    public int getMasaKerja() { return masaKerja; }
    public LocalDate getTanggalPensiun() { return tanggalPensiun; }
}