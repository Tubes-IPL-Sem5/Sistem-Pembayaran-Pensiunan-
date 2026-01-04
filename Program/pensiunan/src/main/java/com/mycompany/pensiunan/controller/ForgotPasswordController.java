/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pensiunan.controller;

import com.mycompany.pensiunan.service.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ForgotPasswordController {

    @FXML
    private TextField usernameField;

    private final AuthService authService = new AuthService();

    @FXML
    private void handleSendReset() {
        String username = usernameField.getText();

        if (username == null || username.isBlank()) {
            showAlert("Error", "Username tidak boleh kosong");
            return;
        }
        
        
        if (username.length() < 8 ) {
            showAlert(
                "Peringatan",
                "Username dan Password minimal 8 karakter."
            );
            return;
        }

        boolean exists = authService.checkUsernameExists(username);

        if (!exists) {
            showAlert("Gagal", "Username tidak ditemukan");
            return;
        }

        showAlert(
            "Reset Password",
            "Akun ditemukan.\nSilakan cek email anda untuk mengubah password."
        );
    }

    @FXML
    private void handleBackToLogin() {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource(
                    "/com/mycompany/pensiunan/view/login/loginView.fxml"
                )
            );
            Stage stage = (Stage) usernameField.getScene().getWindow();
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
