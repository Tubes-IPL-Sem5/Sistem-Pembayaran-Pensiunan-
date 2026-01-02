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

    
}
