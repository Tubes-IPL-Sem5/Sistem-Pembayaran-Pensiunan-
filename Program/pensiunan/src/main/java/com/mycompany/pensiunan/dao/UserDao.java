package com.mycompany.pensiunan.dao;

import com.mycompany.pensiunan.config.Koneksi; // Asumsi Koneksi ada di config
import com.mycompany.pensiunan.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao{

    public User findAccountByUsername(String username) {
        String SQL = "SELECT id_akun, username, password, peran FROM akun_pengguna WHERE username = ?";
        User user = null;

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                
                if (rs.next()) {
                    return new User(
                        rs.getInt("id_akun"),
                        rs.getString("username"),
                        rs.getString("peran") 
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Database Error saat mencari akun: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Method tambahan untuk mengambil hash password (digunakan oleh Service).
     */
    public String getPasswordHash(String username) {
        String SQL = "SELECT password FROM akun_pengguna WHERE username = ?";
        
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("password");
                }
            }
        } catch (SQLException e) {
            System.err.println("Database Error saat mengambil hash: " + e.getMessage());
        }
        return null;
    }
}