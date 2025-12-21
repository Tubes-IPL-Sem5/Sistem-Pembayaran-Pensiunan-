package com.mycompany.pensiunan;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Ganti baris di bawah ini agar mengarah ke DashboardView.fxml kamu
        Scene scene = new Scene(
            FXMLLoader.load(
                getClass().getResource(
                    "/fxml/DashboardView.fxml" 
                )
            )
        );
        stage.setScene(scene); 
        
        stage.setWidth(1000);   
        stage.setHeight(700); 

        stage.setTitle("Dashboard Pensiunan - Sistem Pembayaran");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}