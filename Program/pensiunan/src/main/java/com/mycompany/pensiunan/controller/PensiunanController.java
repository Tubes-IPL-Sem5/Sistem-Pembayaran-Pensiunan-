package com.mycompany.pensiunan.controller;

import com.mycompany.pensiunan.config.Koneksi;
import com.mycompany.pensiunan.util.Session;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PensiunanController implements Initializable {

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
    
    @FXML
    private void handleOpenHistory() {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource(
                    "/com/mycompany/pensiunan/view/pensiunan/historyTransferView.fxml"
                )
            );

            Stage stage = (Stage) btnHistory.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("History Transfer Gaji");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Logout");
        alert.setHeaderText(null);
        alert.setContentText("Apakah Anda yakin ingin logout?");

        ButtonType btnYa = new ButtonType("Ya", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnTidak = new ButtonType("Tidak", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnYa, btnTidak);

        alert.showAndWait().ifPresent(response -> {
            if (response == btnYa) {
                Session.clear();
                navigate("/com/mycompany/pensiunan/view/login/loginView.fxml");
            }
        });
    }

    
    private void navigate(String path) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(path));
            Stage stage = (Stage) lblNama.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
