package com.mycompany.pensiunan.controller;

import com.mycompany.pensiunan.dao.KeuanganDao;
import com.mycompany.pensiunan.model.Keuangan;
import com.mycompany.pensiunan.util.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.Optional;

public class KeuanganController {

    // --- Komponen FXML ---
    @FXML private Label lblNamaUser;
    @FXML private HBox paneMenu;
    @FXML private VBox paneTransfer, paneDataGaji;

    // Tabel Transfer
    @FXML private TableView<Keuangan> tablePensiunan;
    @FXML private TableColumn<Keuangan, String> colNama, colNip, colLalu, colAkan, colStatus;

    // Tabel Gaji
    @FXML private TableView<Keuangan> tableDataGaji;
    @FXML private TableColumn<Keuangan, String> colGajiNama, colGajiNip, colGajiTgl, colGajiLalu;
    @FXML private TableColumn<Keuangan, Number> colGajiGol;

    // --- Inisialisasi DAO & List ---
    private final KeuanganDao keuanganDao = new KeuanganDao();
    private ObservableList<Keuangan> listKeuangan = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        System.out.println("DEBUG CONTROLLER: Memulai initialize()...");

        // 1. Set Nama User
        if (Session.getNama() != null && lblNamaUser != null) {
            lblNamaUser.setText(Session.getNama());
        }

        // 2. Setup Kolom Tabel
        setupTableTransfer();
        setupTableGaji();

        // 3. Load Data
        loadDataFromDatabase();
    }

    private void setupTableTransfer() {
        colNama.setCellValueFactory(c -> c.getValue().namaProperty());
        colNip.setCellValueFactory(c -> c.getValue().nipProperty());
        colLalu.setCellValueFactory(c -> c.getValue().nominalLaluProperty());
        colAkan.setCellValueFactory(c -> c.getValue().nominalAkanProperty());
        colStatus.setCellValueFactory(c -> c.getValue().statusProperty());

        // Custom Warna Status
        colStatus.setCellFactory(column -> new TableCell<Keuangan, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.equalsIgnoreCase("Ditolak") || item.equalsIgnoreCase("DITOLAK")) {
                        setStyle("-fx-text-fill: white; -fx-background-color: #d32f2f; -fx-alignment: CENTER; -fx-background-radius: 5;");
                    } else if (item.equalsIgnoreCase("Disetujui") || item.equalsIgnoreCase("BERHASIL")) {
                        setStyle("-fx-text-fill: white; -fx-background-color: #388e3c; -fx-alignment: CENTER; -fx-background-radius: 5;");
                    } else {
                        setStyle("-fx-text-fill: black; -fx-background-color: #fbc02d; -fx-alignment: CENTER; -fx-background-radius: 5;");
                    }
                }
            }
        });
    }

    private void setupTableGaji() {
        colGajiNama.setCellValueFactory(c -> c.getValue().namaProperty());
        colGajiNip.setCellValueFactory(c -> c.getValue().nipProperty());
        colGajiTgl.setCellValueFactory(c -> c.getValue().tanggalProperty());
        colGajiLalu.setCellValueFactory(c -> c.getValue().nominalLaluProperty());
        colGajiGol.setCellValueFactory(c -> c.getValue().golonganProperty());
    }

    private void loadDataFromDatabase() {
        System.out.println("DEBUG CONTROLLER: Memanggil DAO untuk ambil data...");
        listKeuangan.clear();
        listKeuangan.addAll(keuanganDao.getAllTransaksi());

        tablePensiunan.setItems(listKeuangan);
        tableDataGaji.setItems(listKeuangan);

        tablePensiunan.refresh();
        tableDataGaji.refresh();
    }

    // --- TOMBOL PROSES PEMBAYARAN ---
    @FXML
    private void handleProsesPembayaran() {
        Keuangan terpilih = tablePensiunan.getSelectionModel().getSelectedItem();
        if (terpilih == null) {
            showAlert("Pilih Data", "Silakan pilih data pensiunan terlebih dahulu.");
            return;
        }

        // 1. Logika Bisnis: Hitung Gaji berdasarkan Golongan
        double hitung = 1500000 + (terpilih.getGolongan() * 500000);

        // Update tampilan sementara
        terpilih.setNominalAkan(String.format("%,.0f", hitung).replace(',', '.'));

        // 2. Konfirmasi User
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Pembayaran");
        alert.setHeaderText("Proses Pembayaran untuk: " + terpilih.getNama());
        alert.setContentText("Pilih tindakan untuk data ini.\nData akan disimpan permanen ke database.");

        ButtonType btnSetuju = new ButtonType("Setuju", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnTolak = new ButtonType("Tolak", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnSetuju, btnTolak);

        Optional<ButtonType> result = alert.showAndWait();

        // 3. Tentukan Status & Nominal
        String statusFinal = "";
        double nominalFinal = 0;

        if (result.isPresent() && result.get() == btnSetuju) {
            statusFinal = "BERHASIL"; // Status untuk Database
            nominalFinal = hitung;
        } else {
            statusFinal = "DITOLAK"; // Status untuk Database
            nominalFinal = 0; // Jika ditolak, nominal masuk 0 (atau bisa diisi 'hitung' jika ingin mencatat history request)
        }

        // --- PROSES SIMPAN KE DATABASE (BAIK SETUJU MAUPUN TOLAK) ---
        int idStaffLogin = Session.getIdAkun(); // Ambil siapa yang login

        // Panggil fungsi SIMPAN di DAO
        boolean sukses = keuanganDao.simpanPembayaran(
                terpilih.getIdPensiunan(),
                idStaffLogin,
                nominalFinal,
                statusFinal // Kirim status (BERHASIL / DITOLAK)
        );

        if (sukses) {
            // Update Tampilan Tabel (UI)
            if(statusFinal.equals("BERHASIL")) {
                terpilih.setStatus("Disetujui");
            } else {
                terpilih.setStatus("Ditolak");
            }

            tablePensiunan.refresh();
            showAlert("Sukses", "Data berhasil disimpan ke database dengan status: " + statusFinal);
        } else {
            showAlert("Gagal", "Terjadi kesalahan saat menyimpan data.");
        }
    }

    // --- Navigasi Menu ---
    @FXML private void showTransferDana() { setPanelVisible(paneTransfer); }
    @FXML private void showDataGaji() { setPanelVisible(paneDataGaji); }
    @FXML private void backToDashboard() { setPanelVisible(paneMenu); }

    private void setPanelVisible(javafx.scene.Node target) {
        paneMenu.setVisible(false);
        paneTransfer.setVisible(false);
        paneDataGaji.setVisible(false);
        target.setVisible(true);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}