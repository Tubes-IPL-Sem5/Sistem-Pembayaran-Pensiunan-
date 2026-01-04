package com.mycompany.pensiunan.controller;

import com.mycompany.pensiunan.dao.GajiDao;
import com.mycompany.pensiunan.dao.KeuanganDao;
import com.mycompany.pensiunan.model.Gaji;
import com.mycompany.pensiunan.model.Keuangan;
import com.mycompany.pensiunan.util.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.Optional;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class KeuanganController {

    @FXML private Label lblNamaUser;
    @FXML private VBox paneMenu;
    @FXML private VBox paneTransfer, paneDataGaji;

    // Tabel Transfer
    @FXML private TableView<Keuangan> tablePensiunan;
    @FXML private TableColumn<Keuangan, String> colNama, colNip, colLalu, colAkan, colStatus;

    // Tabel Gaji
    @FXML private TableView<Gaji> tableDataGaji;
    @FXML private TableColumn<Gaji, String> colGajiNama;
    @FXML private TableColumn<Gaji, String> colGajiNip;
    @FXML private TableColumn<Gaji, String> colGajiTgl;
    @FXML private TableColumn<Gaji, Number> colGajiLalu;
    @FXML private TableColumn<Gaji, String> colGajiGol;
    
    @FXML private TextField txtSearchGaji;

    @FXML private Button btnFilterAllGaji;
    @FXML private Button btnFilterI;
    @FXML private Button btnFilterII;
    @FXML private Button btnFilterIII;
    @FXML private Button btnFilterIV;
    @FXML private TextField txtSearchTransfer;

    private FilteredList<Keuangan> filteredTransfer;
    private FilteredList<Gaji> filteredGaji;


    // --- Inisialisasi DAO & List ---
    private final KeuanganDao keuanganDao = new KeuanganDao();
    private final GajiDao gajiDao = new GajiDao();
    
    // Transfer
    private ObservableList<Keuangan> listTransfer = FXCollections.observableArrayList();

    // Gaji
    private ObservableList<Gaji> listGaji = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        if (Session.getNama() != null && lblNamaUser != null) {
            lblNamaUser.setText(Session.getNama());
        }
        setupTableTransfer();
        setupTableGaji();

        loadTransferData();
        loadRiwayatGaji();
        initSearchTransfer(); 
        initFilterAndSearch();
    }

    private void initFilterAndSearch() {
        filteredGaji = new FilteredList<>(listGaji, g -> true);

        // SEARCH (nama / nip)
        txtSearchGaji.textProperty().addListener((obs, oldVal, newVal) -> {
            String keyword = newVal.toLowerCase().trim();

            filteredGaji.setPredicate(gaji -> {
                if (keyword.isEmpty()) return true;

                return gaji.getNama().toLowerCase().contains(keyword)
                    || gaji.getNip().toLowerCase().contains(keyword);
            });
        });

        SortedList<Gaji> sorted = new SortedList<>(filteredGaji);
        sorted.comparatorProperty().bind(tableDataGaji.comparatorProperty());

        tableDataGaji.setItems(sorted);
    }
    
    private void initSearchTransfer() {
        filteredTransfer = new FilteredList<>(listTransfer, k -> true);

        txtSearchTransfer.textProperty().addListener((obs, oldVal, newVal) -> {
            String keyword = newVal == null ? "" : newVal.toLowerCase().trim();

            filteredTransfer.setPredicate(k -> {
                if (keyword.isEmpty()) return true;

                return k.namaProperty().get().toLowerCase().contains(keyword)
                    || k.nipProperty().get().toLowerCase().contains(keyword);
            });
        });

        SortedList<Keuangan> sorted = new SortedList<>(filteredTransfer);
        sorted.comparatorProperty().bind(tablePensiunan.comparatorProperty());

        tablePensiunan.setItems(sorted);
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
        colGajiNama.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNama()));
        colGajiNip.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNip()));
        colGajiTgl.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTanggal()));
        colGajiLalu.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getJumlah()));
        colGajiGol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getGolongan()));
    }

    
    private void filterByGolongan(String golongan) {
        filteredGaji.setPredicate(gaji -> {
            boolean matchSearch =
                txtSearchGaji.getText() == null
                || txtSearchGaji.getText().isEmpty()
                || gaji.getNama().toLowerCase().contains(txtSearchGaji.getText().toLowerCase())
                || gaji.getNip().toLowerCase().contains(txtSearchGaji.getText().toLowerCase());

            boolean matchGolongan =
                golongan == null || gaji.getGolongan().equals(golongan);

            return matchSearch && matchGolongan;
        });
    }


    private void loadTransferData() {
        listTransfer.setAll(keuanganDao.getAllTransaksi());
        tablePensiunan.setItems(listTransfer);
    }

    private void loadRiwayatGaji() {
        listGaji.setAll(gajiDao.getRiwayatPembayaran());
        tableDataGaji.setItems(listGaji);
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
        double hitung = 1500000 + (terpilih.getGolongan() * 1000000);

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
            statusFinal = "BERHASIL"; 
            nominalFinal = hitung;
        } else {
            statusFinal = "GAGAL"; 
            nominalFinal = 0;
        }

        int idStaffLogin = Session.getIdKeuangan();

        boolean sukses = keuanganDao.simpanPembayaran(
                terpilih.getIdPensiunan(),
                idStaffLogin,
                nominalFinal,
                statusFinal 
        );

        if (sukses) {
            terpilih.setStatus(
                statusFinal.equals("BERHASIL") ? "Disetujui" : "Ditolak"
            );
            tablePensiunan.refresh();
            showAlert("Sukses", "Data berhasil disimpan ke database dengan status: " + statusFinal);
            loadRiwayatGaji();
        } else {
            showAlert("Gagal", "Terjadi kesalahan saat menyimpan data.");
        }
    }

    // --- Navigasi Menu ---
    @FXML private void showTransferDana() {
        loadTransferData(); 
        initSearchTransfer(); 
        setPanelVisible(paneTransfer); 
    }
    
    @FXML private void showDataGaji() { 
        loadRiwayatGaji();
        initFilterAndSearch(); 
        setPanelVisible(paneDataGaji); 
    }
    
    @FXML private void handleBack() { 
        setPanelVisible(paneMenu); 
    }

    private void setPanelVisible(javafx.scene.Node target) {
        paneMenu.setVisible(false);
        paneTransfer.setVisible(false);
        paneDataGaji.setVisible(false);
        target.setVisible(true);
    }
    
    @FXML
    private void filterSemua() {
        filterByGolongan(null);
    }

    @FXML
    private void filterGolI() {
        filterByGolongan("1");
    }

    @FXML
    private void filterGolII() {
        filterByGolongan("2");
    }

    @FXML
    private void filterGolIII() {
        filterByGolongan("3");
    }

    @FXML
    private void filterGolIV() {
        filterByGolongan("4");
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
            Stage stage = (Stage) lblNamaUser.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}