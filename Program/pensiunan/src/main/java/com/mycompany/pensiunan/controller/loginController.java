package com.mycompany.pensiunan.controller;

import com.mycompany.pensiunan.service.AuthService;
import com.mycompany.pensiunan.model.User;
import com.mycompany.pensiunan.util.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private final AuthService authService = new AuthService();

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isBlank() || password.isBlank()) {
            showAlert("Peringatan", "Username dan Password tidak boleh kosong.");
            return;
        }
        
        if (username.length() < 8 || password.length() < 8) {
            showAlert(
                "Peringatan",
                "Username dan Password minimal 8 karakter."
            );
            return;
        }
        
        if (username.length() > 16 || password.length() > 16) {
            showAlert(
                "Peringatan",
                "Username dan Password tidak boleh melebihi 16 karakter."
            );
            return;
        }
        

        User user = authService.authenticate(username, password);

        if (user.isAuthenticated()) {
            System.out.println("Login BERHASIL. ");

            String namaTampilan = user.getNama();
            // Jika nama null (kosong), gunakan username sebagai cadangan
            if (namaTampilan == null || namaTampilan.trim().isEmpty()) {
                namaTampilan = user.getUsername();
            }

            System.out.println("Nama: " + namaTampilan);
            System.out.println("Role: " + user.getPeran());

            Session.setSession(
                    user.getIdAkun(),
                    user.getIdHrd(),
                    user.getIdKeuangan(),
                    user.getUsername(),
                    user.getPeran(),
                    user.getIdPensiunan(),
                    namaTampilan
            );

            navigateToDashboard(user.getPeran());
        } else {
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
    
    @FXML
    private void handleForgotPassword() {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource(
                    "/com/mycompany/pensiunan/view/login/forgotPasswordView.fxml"
                )
            );
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
}