package com.mycompany.pensiunan.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {

    private static Connection conn;

    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {

                String url = "jdbc:mysql://localhost:3306/db_pembayarangaji"
                           + "?useSSL=false&serverTimezone=UTC";
                String user = "root";
                String password = "";

                conn = DriverManager.getConnection(url, user, password);
                System.out.println("Koneksi ke database berhasil");
            }
        } catch (SQLException e) {
            System.out.println("Koneksi ke database gagal");
            e.printStackTrace();
        }
        return conn;
    }
}
