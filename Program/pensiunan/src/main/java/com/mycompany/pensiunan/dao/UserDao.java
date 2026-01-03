package com.mycompany.pensiunan.dao;

import com.mycompany.pensiunan.config.Koneksi;
import com.mycompany.pensiunan.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    public User findAccountByUsername(String username) {
        // PERBAIKAN: Join ke 3 tabel (Pensiunan, Keuangan, HRD)
        // COALESCE akan mengambil nilai pertama yang TIDAK NULL
        String sql = """
            SELECT 
                ap.id_akun, ap.username, ap.password, ap.peran,
                p.id_pensiunan,
                COALESCE(p.nama, k.nama, h.nama) AS nama_lengkap
            FROM akun_pengguna ap
            LEFT JOIN akun_pensiunan p ON ap.id_akun = p.id_akun
            LEFT JOIN akun_divisi_keuangan k ON ap.id_akun = k.id_akun
            LEFT JOIN akun_hrd h ON ap.id_akun = h.id_akun
            WHERE ap.username = ?
        """;

        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Handle jika id_pensiunan null (karena user adalah HRD/Keuangan)
                int idPensiunan = rs.getInt("id_pensiunan");
                if (rs.wasNull()) {
                    idPensiunan = 0;
                }

                return new User(
                        rs.getInt("id_akun"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("peran"),
                        idPensiunan,
                        rs.getString("nama_lengkap"), // Nama yang diambil dari salah satu tabel
                        false
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method getPasswordHash bisa dihapus atau dibiarkan,
    // tapi logika login utama ada di findAccountByUsername di atas.
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