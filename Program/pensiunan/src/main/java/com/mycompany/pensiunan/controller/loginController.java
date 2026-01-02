package com.mycompany.pensiunan.controller;

import com.mycompany.pensiunan.service.AuthService; // Tambahkan import Service
import com.mycompany.pensiunan.model.User; 

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import com.mycompany.pensiunan.util.Session;


public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private final AuthService authService = new AuthService(); // Inisialisasi Service

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Validasi input kosong 
        if (username.isBlank() || password.isBlank()) {
            showAlert("Peringatan", "Username dan Password tidak boleh kosong."); 
            return;
        }

        // Proses otentikasi via Service
        User user = authService.authenticate(username, password);

        if (user.isAuthenticated()) {
            System.out.println("Login BERHASIL. ");
            System.out.println("Nama: " +user.getNama());
            System.out.println("Id: "+user.getIdAkun());
            System.out.println("Role: " + user.getPeran()); 
            Session.setSession(
                user.getIdAkun(),
                user.getUsername(),
                user.getPeran(),
                user.getIdPensiunan(),
                user.getNama()
            );

            navigateToDashboard(user.getPeran()); 
        } else {
            // Tampilkan alert login gagal sesuai Skenario Eksepsi SRS 
            showAlert("Login Gagal", "Username atau Password salah. Silakan coba lagi."); 
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void navigateToDashboard(String role) {
        String fxmlPath = "";
        switch (role.toUpperCase()) {
            case "HRD":
                fxmlPath = "/com/mycompany/pensiunan/view/hrd/hrdView.fxml";
                break;
            case "KEUANGAN":
                 fxmlPath = "/com/mycompany/pensiunan/view/keuangan/keuanganView.fxml";
                break;
            case "PENSIUNAN":
                 fxmlPath = "/com/mycompany/pensiunan/view/pensiunan/pensiunanView.fxml";
                break;
        }

        if (!fxmlPath.isEmpty()) {
            try {
                javafx.stage.Stage stage = (javafx.stage.Stage) usernameField.getScene().getWindow();
                javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource(fxmlPath));
                stage.setScene(new javafx.scene.Scene(root));
                stage.setTitle("Dashboard " + role);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}