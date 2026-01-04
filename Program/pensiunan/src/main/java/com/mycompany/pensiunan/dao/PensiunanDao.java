package com.mycompany.pensiunan.dao;

import com.mycompany.pensiunan.config.Koneksi;
import com.mycompany.pensiunan.model.Pensiunan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PensiunanDao {
    
    public List<Pensiunan> getAllPensiunan() {
        List<Pensiunan> list = new ArrayList<>();
        String sql = "SELECT * FROM akun_pensiunan";
        
        try (Connection conn = Koneksi.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(new Pensiunan(
                    rs.getInt("id_pensiunan"),
                    rs.getString("nip"),
                    rs.getString("nama"),
                    rs.getString("golongan"),
                    rs.getInt("masa_kerja"),
                    rs.getDate("tanggal_pensiun").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public Pensiunan getByIdAkun(int idAkun) {
    String sql = """
        SELECT *
        FROM akun_pensiunan
        WHERE id_akun = ?
    """;

    try (Connection c = Koneksi.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setInt(1, idAkun);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new Pensiunan(
                rs.getInt("id_pensiunan"),
                rs.getString("nip"),
                rs.getString("nama"),
                rs.getString("golongan"),
                rs.getInt("masa_kerja"),
                rs.getDate("tanggal_pensiun").toLocalDate()
            );
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

    public boolean insertPensiunan(Pensiunan p, String username, String passwordHash, int idHrd) {
        Connection conn = Koneksi.getConnection();
        String sqlAkun = "INSERT INTO akun_pengguna (username, password, peran) VALUES (?, ?, 'PENSIUNAN')";
        String sqlProfil = "INSERT INTO akun_pensiunan (id_akun, id_hrd, nip, nama, tanggal_pensiun, golongan, masa_kerja) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            conn.setAutoCommit(false); // Mulai transaksi

            PreparedStatement psAkun = conn.prepareStatement(sqlAkun, Statement.RETURN_GENERATED_KEYS);
            psAkun.setString(1, username);
            psAkun.setString(2, passwordHash);
            psAkun.executeUpdate();

            // Ambil ID akun yang baru saja dibuat
            ResultSet rs = psAkun.getGeneratedKeys();
            if (rs.next()) {
                int idAkunBaru = rs.getInt(1);

                // 2. Simpan ke akun_pensiunan
                PreparedStatement psProfil = conn.prepareStatement(sqlProfil);
                psProfil.setInt(1, idAkunBaru);
                psProfil.setInt(2, idHrd);
                psProfil.setString(3, p.getNip());
                psProfil.setString(4, p.getNama());
                psProfil.setDate(5, java.sql.Date.valueOf(p.getTanggalPensiun()));
                psProfil.setString(6, p.getGolongan());
                psProfil.setInt(7, p.getMasaKerja());
                psProfil.executeUpdate();
            }

            conn.commit(); // Simpan permanen jika semua berhasil
            return true;
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        }
    }
    
}
