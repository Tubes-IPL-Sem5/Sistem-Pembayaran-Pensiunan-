/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pensiunan.model;

import java.time.LocalDate;

public class HistoryPembayaran {

    private LocalDate tanggal;
    private double jumlah;
    private String status;

    public HistoryPembayaran(LocalDate tanggal, double jumlah, String status) {
        this.tanggal = tanggal;
        this.jumlah = jumlah;
        this.status = status;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    public double getJumlah() {
        return jumlah;
    }

    public String getStatus() {
        return status;
    }
}
