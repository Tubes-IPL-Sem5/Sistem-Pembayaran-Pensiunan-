package com.mycompany.pensiunan.dao;

import com.mycompany.pensiunan.config.Koneksi;
import com.mycompany.pensiunan.model.HistoryPembayaran;
import com.mycompany.pensiunan.model.SummaryHistory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoryPembayaranDao {

    public List<HistoryPembayaran> getByPensiunan(int idPensiunan) {

        List<HistoryPembayaran> list = new ArrayList<>();

        String sql = """
            SELECT tanggal, jumlah, status
            FROM history_pembayaran
            WHERE id_pensiunan = ?
            ORDER BY tanggal DESC
        """;

        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, idPensiunan);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new HistoryPembayaran(
                        rs.getDate("tanggal").toLocalDate(),
                        rs.getDouble("jumlah"),
                        rs.getString("status")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    
    public List<HistoryPembayaran> getFiltered(
        int idPensiunan,
        String status,
        Integer bulan
    ) {
        List<HistoryPembayaran> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT tanggal, jumlah, status
            FROM history_pembayaran
            WHERE id_pensiunan = ?
        """);

        if (!"SEMUA".equals(status)) {
            sql.append(" AND status = ?");
        }

        if (bulan != null) {
            sql.append(" AND MONTH(tanggal) = ?");
        }

        sql.append(" ORDER BY tanggal DESC");

        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {

            int idx = 1;
            ps.setInt(idx++, idPensiunan);

            if (!"SEMUA".equals(status)) {
                ps.setString(idx++, status);
            }

            if (bulan != null) {
                ps.setInt(idx++, bulan);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new HistoryPembayaran(
                        rs.getDate("tanggal").toLocalDate(),
                        rs.getDouble("jumlah"),
                        rs.getString("status")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public SummaryHistory getSummary(int idPensiunan) {

    String sql = """
        SELECT
            SUM(CASE WHEN status='BERHASIL' THEN jumlah ELSE 0 END) AS total,
            COUNT(CASE WHEN status='BERHASIL' THEN 1 END) AS count_success,
            AVG(CASE WHEN status='BERHASIL' THEN jumlah END) AS rata_rata
        FROM history_pembayaran
        WHERE id_pensiunan = ?
    """;

    try (Connection c = Koneksi.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setInt(1, idPensiunan);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new SummaryHistory(
                    rs.getDouble("total"),
                    rs.getInt("count_success"),
                    rs.getDouble("rata_rata")
            );
        }
        
    } catch (Exception e) {
        e.printStackTrace();
    }

    return new SummaryHistory(0, 0, 0);
}

}
