package com.mycompany.pensiunan.controller;

import com.mycompany.pensiunan.service.AuthService;
import com.mycompany.pensiunan.dao.PensiunanDao;
import com.mycompany.pensiunan.model.Pensiunan;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.time.LocalDate;
import javafx.scene.layout.GridPane;


public class HrdController {
    // Komponen Tabel
    @FXML private TableView<Pensiunan> tabelPensiunan;
    @FXML private TableColumn<Pensiunan, String> colNip, colNama, colGolongan, colTglPensiun;
    @FXML private TableColumn<Pensiunan, Integer> colMasaKerja;
    @FXML private GridPane formInputPensiunan;
    // Komponen Input Form 
    @FXML private TextField txtNip;
    @FXML private TextField txtNama;
    @FXML private TextField txtGolongan;
    @FXML private TextField txtMasaKerja;
    @FXML private DatePicker dpTanggalPensiun;
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    private final PensiunanDao pensiunanDao = new PensiunanDao();
    private final AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        // Mapping kolom Tabel (Gunakan SimpleObjectProperty untuk Integer agar stabil)
        colNip.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNip()));
        colNama.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNama()));
        colGolongan.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGolongan()));
        colMasaKerja.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getMasaKerja()));
        colTglPensiun.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTanggalPensiun().toString()));

        loadData();
    }

    private void loadData() {
        // Menampilkan daftar data pensiunan (SRS 3.1)
        tabelPensiunan.setItems(FXCollections.observableArrayList(pensiunanDao.getAllPensiunan()));
    }
    
    @FXML
    private void tampilkanForm() {
        // 2. Gunakan variabel yang sudah dideklarasikan di atas
        formInputPensiunan.setVisible(true);
        formInputPensiunan.setManaged(true);
    }
    
    @FXML
    private void sembunyikanForm() {
    // 3. Pastikan method ini juga menggunakan variabel yang sama
    formInputPensiunan.setVisible(false);
    formInputPensiunan.setManaged(false);
    clearFields(); 
    }
    
    @FXML
    private void handleTambah() {
        // 1. Validasi Input: Skenario Eksepsi 1 (Data wajib lengkap)
        if (isInputInvalid()) {
            showAlert(AlertType.WARNING, "Data Wajib Lengkap", "Mohon lengkapi semua data wajib sebelum menyimpan.");
            return;
        }

        try {
            // 2. Persiapkan Data 
            String nip = txtNip.getText();
            String nama = txtNama.getText();
            String golongan = txtGolongan.getText();
            int masaKerja = Integer.parseInt(txtMasaKerja.getText());
            LocalDate tglPensiun = dpTanggalPensiun.getValue();

            // 3. Persiapkan Kredensial untuk tabel akun_pengguna
            String username = txtUsername.getText();
            String rawPassword = txtPassword.getText();
            String hashedPass = authService.hashPassword(rawPassword); 

            Pensiunan pensiunanBaru = new Pensiunan(0, nip, nama, golongan, masaKerja, tglPensiun);

            // 4. Eksekusi ke Database (idHrd 1 diasumsikan petugas login)
            int currentHrdId = 1; 
            boolean berhasil = pensiunanDao.insertPensiunan(pensiunanBaru, username, hashedPass, currentHrdId);

            // 5. Respon UI 
            if (berhasil) {
                loadData(); 
                clearFields(); 
                sembunyikanForm();
                showAlert(AlertType.INFORMATION, "Berhasil", "Data pensiunan baru berhasil disimpan ke database.");
            } else {
                showAlert(AlertType.ERROR, "Gagal", "Simpan data gagal. NIP atau Username mungkin sudah terdaftar.");
            }

        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Format Salah", "Masa Kerja harus berupa angka bulat!");
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error Sistem", "Terjadi kesalahan: " + e.getMessage());
        }
    }

    private boolean isInputInvalid() {
        return txtNip.getText().isBlank() || txtNama.getText().isBlank() || 
               txtGolongan.getText().isBlank() || txtMasaKerja.getText().isBlank() ||
               txtUsername.getText().isBlank() || txtPassword.getText().isBlank() ||
               dpTanggalPensiun.getValue() == null;
    }

    private void clearFields() {
        txtNip.clear(); 
        txtNama.clear(); 
        txtGolongan.clear();
        txtMasaKerja.clear(); 
        txtUsername.clear(); 
        txtPassword.clear();
        dpTanggalPensiun.setValue(null);
    }

    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
}