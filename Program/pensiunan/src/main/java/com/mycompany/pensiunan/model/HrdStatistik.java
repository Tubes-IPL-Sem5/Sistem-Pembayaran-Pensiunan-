/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pensiunan.model;

public class HrdStatistik {

    private int totalPensiunan;
    private int pensiunTahunIni;
    private double rataMasaKerja;

    public HrdStatistik(int totalPensiunan, int pensiunTahunIni, double rataMasaKerja) {
        this.totalPensiunan = totalPensiunan;
        this.pensiunTahunIni = pensiunTahunIni;
        this.rataMasaKerja = rataMasaKerja;
    }

    public int getTotalPensiunan() {
        return totalPensiunan;
    }

    public int getPensiunTahunIni() {
        return pensiunTahunIni;
    }

    public double getRataMasaKerja() {
        return rataMasaKerja;
    }
}
