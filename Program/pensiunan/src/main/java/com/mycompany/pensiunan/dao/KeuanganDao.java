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
            SELECT p.id_pensiunan, p.nama, p.nip,
                   COALESCE(MAX(h.jumlah),0) AS terakhir,
                   0 AS akan,
                   p.golongan,
                   'Menunggu' AS status
            FROM akun_pensiunan p
            LEFT JOIN history_pembayaran h
                   ON p.id_pensiunan = h.id_pensiunan
            GROUP BY p.id_pensiunan
        """;

        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Keuangan(
                        rs.getInt("id_pensiunan"),
                        rs.getString("nama"),
                        rs.getString("nip"),
                        rs.getDouble("terakhir"),
                        rs.getDouble("akan"),
                        Integer.parseInt(rs.getString("golongan")),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public boolean simpanPembayaran(
              int idPensiunan,
              int idKeuangan,
              double jumlah,
              String status
      ) {
          Connection conn = null;

          try {
              conn = Koneksi.getConnection();
              conn.setAutoCommit(false);
              
              String sqlPembayaran = """
                  INSERT INTO pembayaran
                  (id_pensiunan, id_keuangan, tanggal, jumlah, status_pembayaran)
                  VALUES (?, ?, CURDATE(), ?, ?)
              """;

              PreparedStatement psBayar =
                      conn.prepareStatement(sqlPembayaran, Statement.RETURN_GENERATED_KEYS);

              psBayar.setInt(1, idPensiunan);
              psBayar.setInt(2, idKeuangan);
              psBayar.setDouble(3, jumlah);
              psBayar.setString(4, status);

              int rows = psBayar.executeUpdate();
              if (rows == 0) {
                  conn.rollback();
                  return false;
              }

              ResultSet rs = psBayar.getGeneratedKeys();
              if (!rs.next()) {
                  conn.rollback();
                  return false;
              }

              int idPembayaran = rs.getInt(1);

              // 2. INSERT history_pembayaran
              String sqlHistory = """
                  INSERT INTO history_pembayaran
                  (id_pembayaran, id_pensiunan, tanggal, jumlah, status)
                  VALUES (?, ?, CURDATE(), ?, ?)
              """;

              PreparedStatement psHist = conn.prepareStatement(sqlHistory);
              psHist.setInt(1, idPembayaran);
              psHist.setInt(2, idPensiunan);
              psHist.setDouble(3, jumlah);
              psHist.setString(4, status);

              psHist.executeUpdate();

              // 3. INSERT gaji_pensiunan (HANYA JIKA BERHASIL)
              if ("BERHASIL".equals(status)) {

                  String sqlGaji = """
                      INSERT INTO gaji_pensiunan
                      (id_pensiunan, tanggal, jumlah_gaji, status_perhitungan)
                      VALUES (?, CURDATE(), ?, 'SELESAI')
                  """;

                  PreparedStatement psGaji = conn.prepareStatement(sqlGaji);
                  psGaji.setInt(1, idPensiunan);
                  psGaji.setDouble(2, jumlah);

                  psGaji.executeUpdate();
              }

              conn.commit();
              return true;

          } catch (SQLException e) {
              try {
                  if (conn != null) conn.rollback();
              } catch (SQLException ignored) {}
              e.printStackTrace();
              return false;

          } finally {
              try {
                  if (conn != null) conn.setAutoCommit(true);
              } catch (SQLException ignored) {}
          }
      }

}