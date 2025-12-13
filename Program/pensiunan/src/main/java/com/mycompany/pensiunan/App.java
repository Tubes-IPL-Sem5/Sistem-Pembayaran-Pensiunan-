package com.mycompany.pensiunan;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(
            FXMLLoader.load(
                getClass().getResource(
                    "/com/mycompany/pensiunan/view/login/loginView.fxml"
                )
            )
        );
        stage.setScene(scene); 
        
        stage.setWidth(1000);   
        stage.setHeight(700); 

        stage.setTitle("Sistem Pembayaran Pensiunan");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
