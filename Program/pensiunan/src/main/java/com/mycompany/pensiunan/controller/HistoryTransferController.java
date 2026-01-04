package com.mycompany.pensiunan.controller;

import com.mycompany.pensiunan.dao.HistoryPembayaranDao;
import com.mycompany.pensiunan.model.HistoryPembayaran;
import com.mycompany.pensiunan.model.SummaryHistory;
import com.mycompany.pensiunan.util.Session;
import java.io.File;
import java.io.FileWriter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class HistoryTransferController implements Initializable {

    @FXML private VBox vboxHistoryList;
    @FXML private Label lblNama;
    @FXML private TextField txtSearch;
    @FXML private Label lblTotalDiterima;
    @FXML private Label lblRataRata;
    @FXML private Label lblBulanIni;
    @FXML private Button btnBack;
    @FXML private Button btnLogout;
    @FXML private Button btnReset;
    @FXML private ComboBox<String> cmbStatus;
    @FXML private ComboBox<String> cmbBulan;


    private final HistoryPembayaranDao historyDao = new HistoryPembayaranDao();

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd MMM yyyy");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblNama.setText(Session.getNama());
        initFilter();        
        loadHistory();      
        updateSummary();   
    }

    private void loadHistory() {
        vboxHistoryList.getChildren().clear();

        List<HistoryPembayaran> list =
                historyDao.getByPensiunan(Session.getIdPensiunan());

        for (HistoryPembayaran h : list) {
            vboxHistoryList.getChildren().add(
                    createHistoryRow(h)
            );
        }
    }

    
    private HBox createHistoryRow(HistoryPembayaran h) {
        LocalDate tanggal = h.getTanggal();
        double jumlah = h.getJumlah();
        String status = h.getStatus();

        Label lblTanggal = new Label(formatter.format(tanggal));
        lblTanggal.setPrefWidth(150);
        lblTanggal.getStyleClass().add("row-date");

        Label lblPeriode = new Label(
                tanggal.getMonth().name() + " " + tanggal.getYear()
        );
        lblPeriode.setPrefWidth(150);

        Label lblJumlah = new Label("Rp " + String.format("%,.0f", jumlah));
        lblJumlah.setPrefWidth(200);
        lblJumlah.getStyleClass().add("row-amount");

        Label lblStatus = new Label(status);
        lblStatus.setPrefWidth(150);
        lblStatus.getStyleClass().add(
                status.equals("BERHASIL") ? "status-success" :
                status.equals("PENDING")  ? "status-pending" :
                                            "status-failed"
        );

        Button btnDetail = new Button("Detail");
        btnDetail.setPrefWidth(100);
        btnDetail.getStyleClass().add("detail-button");

        // 🔑 EVENT DETAIL
        btnDetail.setOnAction(e -> showDetailPopup(h));

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        HBox row = new HBox(
                20,
                lblTanggal,
                lblPeriode,
                lblJumlah,
                lblStatus,
                spacer,
                btnDetail
        );

        row.setPadding(new javafx.geometry.Insets(18));
        row.getStyleClass().add("history-row");

        return row;
    }
    
    private void showDetailPopup(HistoryPembayaran h) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detail Pembayaran");
        alert.setHeaderText("Informasi Detail Pembayaran");

        ButtonType btnDownload = new ButtonType("Download");
        ButtonType btnTutup = new ButtonType("Tutup", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnDownload, btnTutup);

        String content = """
                Tanggal  : %s
                Periode  : %s
                Jumlah   : Rp %, .0f
                Status   : %s
                """.formatted(
                formatter.format(h.getTanggal()),
                h.getTanggal().getMonth().name() + " " + h.getTanggal().getYear(),
                h.getJumlah(),
                h.getStatus()
        );

        alert.setContentText(content);

        alert.showAndWait().ifPresent(res -> {
            if (res == btnDownload) {
                saveToTxtWithChooser(h);
            }
        });
    }


    private void saveToTxtWithChooser(HistoryPembayaran h) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Simpan Detail Pembayaran");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text File (*.txt)", "*.txt")
        );

        chooser.setInitialFileName(
                "Detail_Pembayaran_" +
                formatter.format(h.getTanggal()).replace(" ", "_") +
                ".txt"
        );

        File file = chooser.showSaveDialog(lblNama.getScene().getWindow());
        if (file == null) return;

        String data = """
                ===== DETAIL PEMBAYARAN GAJI PENSIUN =====

                Nama Pensiunan : %s
                Tanggal        : %s
                Periode        : %s
                Jumlah         : Rp %, .0f
                Status         : %s

                ========================================
                """.formatted(
                Session.getNama(),
                formatter.format(h.getTanggal()),
                h.getTanggal().getMonth().name() + " " + h.getTanggal().getYear(),
                h.getJumlah(),
                h.getStatus()
        );

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(data);

            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.setTitle("Berhasil");
            ok.setHeaderText(null);
            ok.setContentText("File berhasil disimpan:\n" + file.getAbsolutePath());
            ok.showAndWait();

        } catch (Exception e) {
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("Gagal");
            err.setHeaderText(null);
            err.setContentText("Gagal menyimpan file");
            err.showAndWait();
        }
    }

    private void initFilter() {
        cmbStatus.getItems().addAll("SEMUA", "BERHASIL", "PENDING", "GAGAL");
        cmbStatus.setValue("SEMUA");

        cmbBulan.getItems().addAll(
                "SEMUA", "JANUARI", "FEBRUARI", "MARET", "APRIL",
                "MEI", "JUNI", "JULI", "AGUSTUS",
                "SEPTEMBER", "OKTOBER", "NOVEMBER", "DESEMBER"
        );
        cmbBulan.setValue("SEMUA");

        cmbStatus.setOnAction(e -> applyFilter());
        cmbBulan.setOnAction(e -> applyFilter());
        btnReset.setOnAction(e -> resetFilter());
    }

    private void applyFilter() {
        String status = (String) cmbStatus.getValue();
        Integer bulan = cmbBulan.getValue().equals("SEMUA")
                ? null
                : cmbBulan.getSelectionModel().getSelectedIndex(); 



        List<HistoryPembayaran> list =
                historyDao.getFiltered(
                        Session.getIdPensiunan(),
                        status,
                        bulan
                );

        vboxHistoryList.getChildren().clear();
        for (HistoryPembayaran h : list) {
            vboxHistoryList.getChildren().add(
                    createHistoryRow(h)
            );
        }

        updateSummary();
    }

    private void updateSummary() {
        SummaryHistory s = historyDao.getSummary(Session.getIdPensiunan());

        lblTotalDiterima.setText(
                "Rp " + String.format("%,.0f", s.getTotal())
        );

        lblRataRata.setText(
                "Rp " + String.format("%,.0f", s.getRataRata())
        );

        lblBulanIni.setText(lblRataRata.getText());
    }

    private void resetFilter() {
        txtSearch.clear();
        cmbStatus.setValue("SEMUA");
        cmbBulan.setValue("SEMUA");
        loadHistory();
        updateSummary();
    }

    

    @FXML
    private void handleBack() {
        navigate("/com/mycompany/pensiunan/view/pensiunan/pensiunanView.fxml");
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
