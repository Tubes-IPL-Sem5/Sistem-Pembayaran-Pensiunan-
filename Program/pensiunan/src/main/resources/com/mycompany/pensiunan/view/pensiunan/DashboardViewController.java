package fxml;

import com.mycompany.pensiunan.config.Config;
import com.mycompany.pensiunan.model.Gaji;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class DashboardViewController implements Initializable {

    // Variabel ini harus sama dengan fx:id yang kamu isi di Scene Builder
    @FXML private TableView<Gaji> tableGaji;
    @FXML private TableColumn<Gaji, String> colTanggal;
    @FXML private TableColumn<Gaji, String> colKeterangan;
    @FXML private TableColumn<Gaji, Double> colJumlah;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 1. Hubungkan kolom tabel dengan variabel di model Gaji.java
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        colKeterangan.setCellValueFactory(new PropertyValueFactory<>("keterangan"));
        colJumlah.setCellValueFactory(new PropertyValueFactory<>("jumlah"));

        // 2. Ambil data dari database
        loadData();
    }

    private void loadData() {
        ObservableList<Gaji> list = FXCollections.observableArrayList();
        try (Connection conn = Config.connectDB()) {
            // Pastikan tabel 'riwayat_gaji' sudah ada di database 'db_pembayarangaji'
            String query = "SELECT * FROM riwayat_gaji";
            ResultSet rs = conn.createStatement().executeQuery(query);

            while (rs.next()) {
                list.add(new Gaji(
                    rs.getString("tanggal"),
                    rs.getString("keterangan"),
                    rs.getDouble("jumlah")
                ));
            }
            tableGaji.setItems(list);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Gagal memuat data: " + e.getMessage());
        }
    }
}