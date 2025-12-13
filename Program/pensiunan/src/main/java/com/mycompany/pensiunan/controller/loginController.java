package com.mycompany.pensiunan.controller;

import com.mycompany.pensiunan.controller.loginController;
import java.io.IOException;
import javafx.fxml.FXML;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class loginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isBlank() || password.isBlank()) {
            return;
        }

        // panggil AuthService
        // redirect sesuai role
    }
}
