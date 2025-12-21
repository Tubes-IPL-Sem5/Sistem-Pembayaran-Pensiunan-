package com.mycompany.pensiunan.controller;

import com.mycompany.pensiunan.config.Config;
import com.mycompany.pensiunan.model.Gaji;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.*;

public class DashboardController {
    @FXML private TableView<Gaji> tableGaji;
    @FXML private TableColumn<Gaji, String> colTanggal;
    @FXML private TableColumn<Gaji, String> colKeterangan;
    @FXML private TableColumn<Gaji, Double> colJumlah;

    public void initialize() {
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        colKeterangan.setCellValueFactory(new PropertyValueFactory<>("keterangan"));
        colJumlah.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
        loadData();
    }

    private void loadData() {
        ObservableList<Gaji> list = FXCollections.observableArrayList();
        try (Connection conn = Config.connectDB()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM riwayat_gaji");
            while (rs.next()) {
                list.add(new Gaji(rs.getString("tanggal"), rs.getString("keterangan"), rs.getDouble("jumlah")));
            }
            tableGaji.setItems(list);
        } catch (Exception e) { e.printStackTrace(); }
    }
}