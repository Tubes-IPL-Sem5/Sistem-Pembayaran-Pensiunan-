package com.mycompany.Keuangan;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class HelloController {
    @FXML private HBox paneMenu;
    @FXML private VBox paneTransfer, paneDataGaji;
    @FXML private TableView<Keuangan> tablePensiunan, tableDataGaji;
    @FXML private TableColumn<Keuangan, String> colNama, colNip, colLalu, colAkan, colStatus;
    @FXML private TableColumn<Keuangan, String> colGajiNama, colGajiNip, colGajiTgl, colGajiLalu;
    @FXML private TableColumn<Keuangan, Integer> colGajiGol;

    private final ObservableList<Keuangan> listPensiun = FXCollections.observableArrayList(
            new Keuangan("Michael Evan", "3025783", "Selasa 30 Juni 2019", "2.500.000", 3),
            new Keuangan("Jack Lang", "3039758", "Senin 23 Maret 2020", "2.000.000", 1),
            new Keuangan("David Link", "3092873", "Rabu 24 Januari 2017", "2.750.000", 4)
    );

    @FXML
    public void initialize() {
        // Mapping kolom tabel
        colNama.setCellValueFactory(c -> c.getValue().namaProperty());
        colNip.setCellValueFactory(c -> c.getValue().nipProperty());
        colLalu.setCellValueFactory(c -> c.getValue().nominalLaluProperty());
        colAkan.setCellValueFactory(c -> c.getValue().nominalAkanProperty());
        colStatus.setCellValueFactory(c -> c.getValue().statusProperty());
        tablePensiunan.setItems(listPensiun);

        colGajiNama.setCellValueFactory(c -> c.getValue().namaProperty());
        colGajiNip.setCellValueFactory(c -> c.getValue().nipProperty());
        colGajiTgl.setCellValueFactory(c -> c.getValue().tanggalProperty());
        colGajiLalu.setCellValueFactory(c -> c.getValue().nominalLaluProperty());
        colGajiGol.setCellValueFactory(c -> c.getValue().golonganProperty().asObject());
        tableDataGaji.setItems(listPensiun);
    }

    @FXML private void showTransferDana() { setPanelVisible(paneTransfer); }
    @FXML private void showDataGaji() { setPanelVisible(paneDataGaji); }
    @FXML private void backToDashboard() { setPanelVisible(paneMenu); }

    private void setPanelVisible(javafx.scene.Node target) {
        paneMenu.setVisible(false);
        paneTransfer.setVisible(false);
        paneDataGaji.setVisible(false);
        target.setVisible(true);
    }

    @FXML
    private void handleProsesPembayaran() {

    }
}