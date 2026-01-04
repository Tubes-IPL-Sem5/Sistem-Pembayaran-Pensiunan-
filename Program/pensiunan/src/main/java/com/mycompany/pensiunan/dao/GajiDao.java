package com.mycompany.pensiunan.dao;

import com.mycompany.pensiunan.config.Koneksi;
import com.mycompany.pensiunan.model.Gaji;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GajiDao {

    public List<Gaji> getRiwayatPembayaran() {

        List<Gaji> list = new ArrayList<>();

        String sql = """
            SELECT 
                p.nama,
                p.nip,
                h.tanggal,
                h.jumlah,
                p.golongan,
                h.status
            FROM history_pembayaran h
            JOIN akun_pensiunan p 
                ON h.id_pensiunan = p.id_pensiunan
            ORDER BY h.tanggal DESC
        """;

        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Gaji(
                        rs.getString("nama"),
                        rs.getString("nip"),
                        rs.getDate("tanggal").toString(),
                        rs.getDouble("jumlah"),
                        rs.getString("golongan"),
                        rs.getString("status")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
