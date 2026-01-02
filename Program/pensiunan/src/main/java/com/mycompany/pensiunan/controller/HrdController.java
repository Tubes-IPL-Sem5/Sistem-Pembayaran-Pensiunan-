package com.mycompany.pensiunan.controller;

import com.mycompany.pensiunan.dao.PensiunanDao;
import com.mycompany.pensiunan.model.Pensiunan;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class HrdController {
    @FXML private TableView<Pensiunan> tabelPensiunan;
    @FXML private TableColumn<Pensiunan, String> colNip, colNama, colGolongan, colTglPensiun;
    @FXML private TableColumn<Pensiunan, Integer> colMasaKerja;
    
    private final PensiunanDao pensiunanDao = new PensiunanDao();

    @FXML
    public void initialize() {
        // Mapping kolom Tabel ke Model Pensiunan
        colNip.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNip()));
        colNama.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNama()));
        colGolongan.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGolongan()));
        colMasaKerja.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getMasaKerja()).asObject());
        colTglPensiun.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTanggalPensiun().toString()));

        loadData();
    }

    private void loadData() {
        tabelPensiunan.setItems(FXCollections.observableArrayList(pensiunanDao.getAllPensiunan()));
    }

    @FXML
    private void handleTambah() {
        // Implementasi Pop-up Form Tambah Data Pensiunan
        System.out.println("Membuka form tambah sesuai SRS 3.3");
    }
}