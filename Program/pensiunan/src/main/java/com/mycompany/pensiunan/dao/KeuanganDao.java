package com.mycompany.pensiunan.dao;

import com.mycompany.pensiunan.config.Koneksi;
import com.mycompany.pensiunan.model.Keuangan;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class KeuanganDao {

    public List<Keuangan> getAllTransaksi() {
        List<Keuangan> list = new ArrayList<>();

        String sql = """
            SELECT 
                g.id_gaji_pensiunan AS id_transaksi,
                p.id_pensiunan,
                p.nama, 
                p.nip, 
                p.golongan, 
                p.tanggal_pensiun,
                g.jumlah_gaji AS nominal, 
                g.status_perhitungan 
            FROM gaji_pensiunan g
            JOIN akun_pensiunan p ON g.id_pensiunan = p.id_pensiunan
        """;

        System.out.println("DEBUG DAO: Menjalankan Query getAllTransaksi...");

        try (Connection conn = Koneksi.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // 1. Parsing Golongan ke Angka
                int gol = 1;
                try {
                    String gStr = rs.getString("golongan");
                    if(gStr != null) {
                        if(gStr.startsWith("IV")) gol = 4;
                        else if(gStr.startsWith("III")) gol = 3;
                        else if(gStr.startsWith("II")) gol = 2;
                    }
                } catch (Exception e) { gol = 1; }

                // 2. LOGIKA HITUNG OTOMATIS (PERBAIKAN DISINI)
                // Rumus: 1.500.000 + (Golongan * 500.000)
                double nominalLalu = rs.getDouble("nominal");
                double nominalHitung = 1500000 + (gol * 500000);

                list.add(new Keuangan(
                        rs.getInt("id_transaksi"),
                        rs.getInt("id_pensiunan"),
                        rs.getString("nama"),
                        rs.getString("nip"),
                        rs.getString("tanggal_pensiun"),
                        nominalLalu,   // Nominal Terakhir (Data Asli DB)
                        nominalHitung, // Nominal Akan (HASIL HITUNGAN RUMUS)
                        gol,
                        "Menunggu"
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // FUNGSI SIMPAN (Tetap Sama)
    public boolean simpanPembayaran(int idPensiunan, int idStaffKeuangan, double jumlah, String status) {
        String sqlInsert = "INSERT INTO pembayaran (id_pensiunan, id_keuangan, tanggal, jumlah, status_pembayaran) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlInsert)) {

            ps.setInt(1, idPensiunan);
            ps.setInt(2, idStaffKeuangan);
            ps.setDate(3, Date.valueOf(LocalDate.now()));
            ps.setDouble(4, jumlah);
            ps.setString(5, status);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("DEBUG DAO: Berhasil simpan pembayaran ID: " + idPensiunan + " Status: " + status);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("ERROR DAO SIMPAN: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}