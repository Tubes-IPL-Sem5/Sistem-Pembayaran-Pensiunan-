package com.mycompany.pensiunan.controller;

import com.mycompany.pensiunan.dao.KeuanganDao;
import com.mycompany.pensiunan.model.Keuangan;
import com.mycompany.pensiunan.util.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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
        System.out.println("DEBUG: KeuanganController initialize berjalan...");

        // 1. Set Nama User dari Session
        if (Session.getNama() != null && lblNamaUser != null) {
            lblNamaUser.setText(Session.getNama());
        }

        // 2. Setup Kolom Tabel
        setupTableTransfer();
        setupTableGaji();

        // 3. Load Data dari Database
        loadDataFromDatabase();
    }

    // --- SETUP TABEL TRANSFER (Dengan Warna Status) ---
    private void setupTableTransfer() {
        colNama.setCellValueFactory(c -> c.getValue().namaProperty());
        colNip.setCellValueFactory(c -> c.getValue().nipProperty());
        colLalu.setCellValueFactory(c -> c.getValue().nominalLaluProperty());
        colAkan.setCellValueFactory(c -> c.getValue().nominalAkanProperty());
        colStatus.setCellValueFactory(c -> c.getValue().statusProperty());

        // Custom Warna Cell Status
        colStatus.setCellFactory(column -> new TableCell<Keuangan, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    // Logika pewarnaan
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

    // --- SETUP TABEL RIWAYAT GAJI ---
    private void setupTableGaji() {
        colGajiNama.setCellValueFactory(c -> c.getValue().namaProperty());
        colGajiNip.setCellValueFactory(c -> c.getValue().nipProperty());
        colGajiTgl.setCellValueFactory(c -> c.getValue().tanggalProperty());
        colGajiLalu.setCellValueFactory(c -> c.getValue().nominalLaluProperty());
        colGajiGol.setCellValueFactory(c -> c.getValue().golonganProperty());
    }

    // --- LOAD DATA DARI DB ---
    private void loadDataFromDatabase() {
        listKeuangan.clear();
        listKeuangan.addAll(keuanganDao.getAllTransaksi());
        tablePensiunan.setItems(listKeuangan);
        tableDataGaji.setItems(listKeuangan);
        tablePensiunan.refresh();
        tableDataGaji.refresh();
    }

    // --- LOGIKA PROSES PEMBAYARAN ---
    @FXML
    private void handleProsesPembayaran() {
        Keuangan terpilih = tablePensiunan.getSelectionModel().getSelectedItem();
        if (terpilih == null) {
            showAlert("Pilih Data", "Silakan pilih data pensiunan di tabel terlebih dahulu.");
            return;
        }

        // Hitung Gaji (Rumus dummy: 1.5jt + Golongan * 500rb)
        double hitung = 1500000 + (terpilih.getGolongan() * 500000);
        terpilih.setNominalAkan(String.format("%,.0f", hitung).replace(',', '.'));

        // Konfirmasi User
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Pembayaran");
        alert.setHeaderText("Proses Pembayaran untuk: " + terpilih.getNama());
        alert.setContentText("Apakah Anda ingin MENYETUJUI pembayaran ini?\n(Pilih Tolak untuk membatalkan pengajuan)");

        ButtonType btnSetuju = new ButtonType("Setuju", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnTolak = new ButtonType("Tolak", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btnSetuju, btnTolak);

        Optional<ButtonType> result = alert.showAndWait();

        String statusFinal;
        double nominalFinal;

        if (result.isPresent() && result.get() == btnSetuju) {
            statusFinal = "BERHASIL";
            nominalFinal = hitung;
        } else {
            statusFinal = "DITOLAK";
            nominalFinal = 0;
        }

        // Simpan ke Database
        int idStaffLogin = Session.getIdAkun();
        boolean sukses = keuanganDao.simpanPembayaran(
                terpilih.getIdPensiunan(),
                idStaffLogin,
                nominalFinal,
                statusFinal
        );

        if (sukses) {
            // Update tampilan lokal agar responsif
            if(statusFinal.equals("BERHASIL")) terpilih.setStatus("Disetujui");
            else terpilih.setStatus("Ditolak");

            tablePensiunan.refresh();
            showAlert("Sukses", "Data berhasil disimpan dengan status: " + statusFinal);
        } else {
            showAlert("Gagal", "Terjadi kesalahan saat menyimpan data ke database.");
        }
    }

    // --- NAVIGASI DASHBOARD ---
    @FXML private void showTransferDana() { setPanelVisible(paneTransfer); }
    @FXML private void showDataGaji() { setPanelVisible(paneDataGaji); }
    @FXML private void backToDashboard() { setPanelVisible(paneMenu); }

    private void setPanelVisible(Node target) {
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

    // =============================================================
    // FITUR LOGOUT
    // =============================================================
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            System.out.println("LOGOUT: Memulai proses logout...");

            // PATH YANG BENAR (Sesuai Screenshot Folder Resources)
            String pathLogin = "/com/mycompany/pensiunan/view/login/loginView.fxml";

            URL fileUrl = getClass().getResource(pathLogin);

            if (fileUrl == null) {
                // Error Handling agar tidak Force Close
                System.err.println("FATAL ERROR: File loginView.fxml tidak ditemukan di: " + pathLogin);
                showAlert("Error Sistem", "Gagal Logout!\nFile 'loginView.fxml' tidak ditemukan.\nCek apakah file ada di folder:\n" + pathLogin);
                return;
            }

            // Load halaman Login
            FXMLLoader loader = new FXMLLoader(fileUrl);
            Parent root = loader.load();

            // Tampilkan Stage Login
            Stage loginStage = new Stage();
            loginStage.setTitle("Sistem Pembayaran Pensiunan - Login");
            loginStage.setScene(new Scene(root));
            loginStage.show();

            // Tutup halaman Dashboard saat ini
            Node source = (Node) event.getSource();
            Stage currentStage = (Stage) source.getScene().getWindow();
            currentStage.close();

            System.out.println("LOGOUT: Berhasil.");

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error Exception", "Terjadi kesalahan saat memuat halaman Login:\n" + e.getMessage());
        }
    }
}