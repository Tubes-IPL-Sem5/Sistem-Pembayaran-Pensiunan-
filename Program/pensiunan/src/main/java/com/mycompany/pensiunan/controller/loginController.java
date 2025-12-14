package com.mycompany.pensiunan.controller;

import com.mycompany.pensiunan.service.AuthService; // Tambahkan import Service
import com.mycompany.pensiunan.model.User; 

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private final AuthService authService = new AuthService(); // Inisialisasi Service

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isBlank() || password.isBlank()) {
            showAlert("Peringatan", "Username dan Password tidak boleh kosong.");
            return;
        }

        // Panggil AuthService untuk validasi
        User user = authService.authenticate(username, password);

        if (user.isAuthenticated()) {
            System.out.println("Login BERHASIL. Role: " + user.getPeran());
            
            // Redirect/Navigasi sesuai role
            navigateToDashboard(user.getPeran());

        } else {
            // Tampilkan pesan kegagalan
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
        System.out.println("Role: " + role.toUpperCase());
        
        
        switch (role.toUpperCase()) {
            case "PENSIUNAN":
                break;
            case "HRD":
                break;
            case "KEUANGAN":
                break;
        }
    }
}