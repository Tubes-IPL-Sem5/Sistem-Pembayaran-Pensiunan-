package com.mycompany.pensiunan.config;
import java.sql.*;

public class Config {
    public static Connection connectDB() {
        try {
            // Nama database dari instruksi temanmu
            String url = "jdbc:mysql://localhost:3306/db_pembayarangaji";
            return DriverManager.getConnection(url, "root", "");
        } catch (SQLException e) {
            System.err.println("Koneksi Gagal: " + e.getMessage());
            return null;
        }
    }
}