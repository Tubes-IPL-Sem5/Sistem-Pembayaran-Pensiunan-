module com.mycompany.pensiunan {
    requires java.sql; 
    requires spring.security.crypto; 
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.mycompany.pensiunan.controller to javafx.fxml;
    opens com.mycompany.pensiunan.model to javafx.base; // Penting untuk TableView
    
    exports com.mycompany.pensiunan;
    exports com.mycompany.pensiunan.controller;
    exports com.mycompany.pensiunan.model;
}