package com.mycompany.pensiunan.dao;

import com.mycompany.pensiunan.config.Koneksi;
import com.mycompany.pensiunan.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDao {

    public User findAccountByUsername(String username) {
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

    public int findIdHrdByIdAkun(int idAkun) {
        String sql = "SELECT id_hrd FROM akun_hrd WHERE id_akun = ?";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, idAkun);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_hrd");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public int findIdPensiunanByIdAkun(int idAkun) {
        String sql = "SELECT id_pensiunan FROM akun_pensiunan WHERE id_akun = ?";
        try (Connection c = Koneksi.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {

                ps.setInt(1, idAkun);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt("id_pensiunan");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
    }

    public int findIdKeuanganByIdAkun(int idAkun) {
        String sql = "SELECT id_keuangan FROM akun_divisi_keuangan WHERE id_akun = ?";
        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, idAkun);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id_keuangan");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
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
    
    public int insertPensiunan(String username, String password, String peran) throws SQLException {
        String sql = """
            INSERT INTO akun_pengguna (username, password, peran)
            VALUES (?, ?, ?)
        """;

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps =
                     conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, peran);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        throw new SQLException("Gagal menyimpan akun pengguna");
    }
}