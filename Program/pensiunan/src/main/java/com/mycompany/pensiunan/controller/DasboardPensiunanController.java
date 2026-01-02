package com.mycompany.pensiunan.controller;

import com.mycompany.pensiunan.config.Koneksi;
import com.mycompany.pensiunan.util.Session;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DasboardPensiunanController implements Initializable {

    @FXML private Label lblNama;
    @FXML private Label lblSisaGaji;
    @FXML private Button btnHistory;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadProfil();
        loadGajiTerakhir();
    }

    private void loadProfil() {
        lblNama.setText(Session.getNama());
    }

    private void loadGajiTerakhir() {
        String sql = """
            SELECT jumlah_gaji
            FROM gaji_pensiunan
            WHERE id_pensiunan = ?
            ORDER BY tanggal DESC
            LIMIT 1
        """;

        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, Session.getIdPensiunan());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                lblSisaGaji.setText("Rp. " + rs.getDouble("jumlah_gaji"));
            } else {
                lblSisaGaji.setText("Rp. 0");
            }

        } catch (Exception e) {
            lblSisaGaji.setText("Rp. 0");
        }
    }
}
