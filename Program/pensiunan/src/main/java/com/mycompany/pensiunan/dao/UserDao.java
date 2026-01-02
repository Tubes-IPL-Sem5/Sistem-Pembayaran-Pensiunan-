package com.mycompany.pensiunan.dao;

import com.mycompany.pensiunan.config.Koneksi; // Asumsi Koneksi ada di config
import com.mycompany.pensiunan.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao{

    public User findAccountByUsername(String username) {

        String sql = """
            SELECT ap.id_akun, ap.username, ap.password, ap.peran,
                   p.id_pensiunan, p.nama
            FROM akun_pengguna ap
            LEFT JOIN akun_pensiunan p ON ap.id_akun = p.id_akun
            WHERE ap.username = ?
        """;

        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                    rs.getInt("id_akun"),
                    rs.getString("username"),
                    rs.getString("password"),   // hash
                    rs.getString("peran"),
                    rs.getInt("id_pensiunan"),
                    rs.getString("nama"),
                    false
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


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