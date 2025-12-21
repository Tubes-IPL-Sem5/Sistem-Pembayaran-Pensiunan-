package com.mycompany.pensiunan.controller;

import com.mycompany.pensiunan.config.Koneksi; // Menggunakan Koneksi sesuai instruksi
import com.mycompany.pensiunan.model.Gaji;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class DasboardPensiunanController implements Initializable {

    @FXML private TableView<Gaji> tableGaji;
    @FXML private TableColumn<Gaji, String> colTanggal;
    @FXML private TableColumn<Gaji, String> colKeterangan;
    @FXML private TableColumn<Gaji, Double> colJumlah;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Menghubungkan kolom tabel dengan variabel di Gaji.java
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        colKeterangan.setCellValueFactory(new PropertyValueFactory<>("keterangan"));
        colJumlah.setCellValueFactory(new PropertyValueFactory<>("jumlah"));

        loadData();
    }

    private void loadData() {
        ObservableList<Gaji> listGaji = FXCollections.observableArrayList();
        try {
            // Ganti getKoneksi() menjadi getConnection()
            Connection conn = Koneksi.getConnection();
            String query = "SELECT tanggal, keterangan, jumlah FROM riwayat_gaji";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                listGaji.add(new Gaji(
                    rs.getString("tanggal"),
                    rs.getString("keterangan"),
                    rs.getDouble("jumlah")
                ));
            }
            tableGaji.setItems(listGaji);
        } catch (Exception e) {
            System.out.println("Error saat memuat data: " + e.getMessage());
        }
    }
}