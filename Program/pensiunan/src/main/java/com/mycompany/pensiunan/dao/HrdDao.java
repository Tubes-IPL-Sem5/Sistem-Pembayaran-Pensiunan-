/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pensiunan.dao;

import com.mycompany.pensiunan.config.Koneksi;
import com.mycompany.pensiunan.model.HrdStatistik;

import java.sql.*;

public class HrdDao {

    public HrdStatistik getStatistik() {

        String sql = """
            SELECT
                COUNT(*) AS total,
                SUM(YEAR(tanggal_pensiun) = YEAR(CURDATE())) AS tahun_ini,
                AVG(masa_kerja) AS rata_masa_kerja
            FROM akun_pensiunan
        """;

        try (Connection conn = Koneksi.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                return new HrdStatistik(
                        rs.getInt("total"),
                        rs.getInt("tahun_ini"),
                        rs.getDouble("rata_masa_kerja")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new HrdStatistik(0, 0, 0);
    }
}
