package com.mycompany.pensiunan.controller;

import com.mycompany.pensiunan.dao.PensiunanDao;
import com.mycompany.pensiunan.model.HrdStatistik;
import com.mycompany.pensiunan.model.Pensiunan;
import com.mycompany.pensiunan.service.AuthService;
import com.mycompany.pensiunan.service.HrdService;
import com.mycompany.pensiunan.util.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.layout.HBox;

public class HrdController implements Initializable {

    @FXML private VBox formInputPensiunan;

    @FXML private TextField txtNip;
    @FXML private TextField txtNama;
    @FXML private TextField txtGolongan;
    @FXML private TextField txtMasaKerja;
    @FXML private TextField txtUsername;
    @FXML private TextField txtSearch;
    @FXML private PasswordField txtPassword;
    @FXML private DatePicker dpTanggalPensiun;
    @FXML private VBox vboxListPensiunan;
    @FXML private Button btnLogout;
    @FXML private Label lblTotalPensiunan;
    @FXML private Label lblPensiunTahunIni;
    @FXML private Label lblRataMasaKerja;

    private final PensiunanDao pensiunanDao = new PensiunanDao();
    private List<Pensiunan> allPensiunan = new ArrayList<>();
    private final AuthService authService = new AuthService();
    private final HrdService hrdService = new HrdService();


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // form default disembunyikan
        formInputPensiunan.setVisible(false);
        formInputPensiunan.setManaged(false);
        loadPensiunan();
        initSearch();
        loadStatistik();
    }
    
    private void loadStatistik() {
        HrdStatistik s = hrdService.getStatistikDashboard();

        lblTotalPensiunan.setText(String.valueOf(s.getTotalPensiunan()));
        lblPensiunTahunIni.setText(String.valueOf(s.getPensiunTahunIni()));
        lblRataMasaKerja.setText(
                Math.round(s.getRataMasaKerja()) + " Tahun"
        );
    }


    private void loadPensiunan() {
        allPensiunan = pensiunanDao.getAllPensiunan();
        renderPensiunan(allPensiunan);
        vboxListPensiunan.getChildren().clear();

        for (Pensiunan p : pensiunanDao.getAllPensiunan()) {
            vboxListPensiunan.getChildren().add(createRow(p));
        }
    }
    
    private void renderPensiunan(List<Pensiunan> list) {
        vboxListPensiunan.getChildren().clear();

        for (Pensiunan p : list) {
            vboxListPensiunan.getChildren().add(createRow(p));
        }
    }
    
    private void initSearch() {
        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> {
            applySearch(newVal);
        });
    }

    
    private void applySearch(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            renderPensiunan(allPensiunan);
            return;
        }

        String key = keyword.toLowerCase();

        List<Pensiunan> filtered = allPensiunan.stream()
            .filter(p ->
                p.getNama().toLowerCase().contains(key) ||
                p.getNip().toLowerCase().contains(key) ||
                p.getGolongan().toLowerCase().contains(key)
            )
            .toList();

        renderPensiunan(filtered);
    }


    
    private HBox createRow(Pensiunan p) {
        Label lblNama = new Label(p.getNama());
        lblNama.setPrefWidth(220);
        lblNama.getStyleClass().add("row-name");

        Label lblNip = new Label(p.getNip());
        lblNip.setPrefWidth(150);
        lblNip.getStyleClass().add("row-text");

        Label lblTanggal = new Label(
            p.getTanggalPensiun().format(
                java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy")
            )
        );
        lblTanggal.setPrefWidth(180);
        lblTanggal.getStyleClass().add("row-text");

        Label lblGolongan = new Label(p.getGolongan());
        lblGolongan.setPrefWidth(120);
        lblGolongan.getStyleClass().add("row-text");

        Button btnStatus = new Button("Pensiun");
        btnStatus.setPrefWidth(200);
        btnStatus.getStyleClass().add("status-btn-inactive");

        HBox row = new HBox(
            lblNama,
            lblNip,
            lblTanggal,
            lblGolongan,
            btnStatus
        );

        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        row.setSpacing(20);
        row.setPadding(new javafx.geometry.Insets(18, 20, 18, 20));
        row.getStyleClass().add("table-row");

        return row;
    }


    
    @FXML
    private void tampilkanForm() {
        formInputPensiunan.setVisible(true);
        formInputPensiunan.setManaged(true);
    }

    @FXML
    private void sembunyikanForm() {
        formInputPensiunan.setVisible(false);
        formInputPensiunan.setManaged(false);
        clearForm();
    }

    @FXML
    private void handleTambah() {
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        String nip = txtNip.getText().toString();
        String golongan = txtGolongan.getText().toString();
        LocalDate tanggal = dpTanggalPensiun.getValue();
        
        if (txtNip.getText().isBlank() ||
            txtNama.getText().isBlank() ||
            txtGolongan.getText().isBlank() ||
            txtMasaKerja.getText().isBlank() ||
            txtUsername.getText().isBlank() ||
            txtPassword.getText().isBlank() ||
            dpTanggalPensiun.getValue() == null) {

            showAlert(Alert.AlertType.WARNING,
                    "Validasi",
                    "Semua field wajib diisi");
            return;
        }
        
        if (username.length() < 8 || password.length() < 8) {
            showAlert(Alert.AlertType.WARNING,
                "Peringatan",
                "Username dan Password minimal 8 karakter."
            );
            return;
        }
        
        if (username.length() > 16 || password.length() > 16) {
            showAlert(Alert.AlertType.WARNING,
                "Peringatan",
                "Username dan Password tidak boleh melebihi 16 karakter."
            );
            return;
        }
        
        if (nip.length() > 20) {
            showAlert(Alert.AlertType.WARNING,
                "Peringatan",
                "Nip tidak boleh lebih dari 20 karakter."
            );
            return;
        }
        
        if (golongan.length() > 4) {
            showAlert(Alert.AlertType.WARNING,
                "Peringatan",
                "Maksimal golongan adalah 4"
            );
            return;
        }
        
        dpTanggalPensiun.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (date.isAfter(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #eeeeee;");
                }
            }
        });
        
        if (tanggal.isAfter(LocalDate.now())) {
            showAlert(Alert.AlertType.WARNING,
                "Peringatan",
                "Tanggal pensiun tidak boleh lebih dari tanggal hari ini."
            );
            return;
        }

        int masaKerja;
        try {
            masaKerja = Integer.parseInt(txtMasaKerja.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR,
                    "Validasi",
                    "Masa kerja harus berupa angka");
            return;
        }

        if (txtUsername.getText().length() < 8 ||
            txtPassword.getText().length() < 8) {

            showAlert(Alert.AlertType.WARNING,
                    "Validasi",
                    "Username dan Password minimal 8 karakter");
            return;
        }

        // ===== BUAT MODEL =====
        Pensiunan p = new Pensiunan(
                txtNip.getText(),
                txtNama.getText(),
                txtGolongan.getText(),
                masaKerja,
                dpTanggalPensiun.getValue()
        );

        System.out.println("id hrd"+Session.getIdHrd());

        boolean success = hrdService.tambahPensiunan(
                Session.getIdHrd(),
                txtNip.getText(),
                txtNama.getText(),
                txtGolongan.getText(),
                masaKerja,
                dpTanggalPensiun.getValue(),
                txtUsername.getText(),
                txtPassword.getText()   // raw → di-hash di Service
        );


        if (success) {
            showAlert(Alert.AlertType.INFORMATION,
                    "Berhasil",
                    "Data pensiunan berhasil disimpan");
            sembunyikanForm();
            loadPensiunan();
        } else {
            showAlert(Alert.AlertType.ERROR,
                    "Gagal",
                    "Gagal menyimpan data pensiunan");
        }

    }

    @FXML
    private void handleLogout() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmasi Logout");
        confirm.setHeaderText(null);
        confirm.setContentText("Apakah Anda yakin ingin logout?");

        confirm.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK) {
                Session.clear();
                navigate("/com/mycompany/pensiunan/view/login/loginView.fxml");
            }
        });
    }

    private void clearForm() {
        txtNip.clear();
        txtNama.clear();
        txtGolongan.clear();
        txtMasaKerja.clear();
        txtUsername.clear();
        txtPassword.clear();
        dpTanggalPensiun.setValue(null);
    }

    private void navigate(String path) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(path));
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
