/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pensiunan.model;

/**
 *
 * @author julia
 */
public class SummaryHistory {
    private double total;
    private int count;
    private double rataRata;

    public SummaryHistory(double total, int count, double rataRata) {
        this.total = total;
        this.count = count;
        this.rataRata = rataRata;
    }

    public double getTotal() { return total; }
    public int getCount() { return count; }
    public double getRataRata() { return rataRata; }
}

