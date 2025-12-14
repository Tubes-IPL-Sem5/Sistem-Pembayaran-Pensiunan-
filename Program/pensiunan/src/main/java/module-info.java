module com.mycompany.pensiunan {
    
    requires java.sql; 
    requires spring.security.crypto; 
    requires javafx.controls;
    requires javafx.fxml;
    exports com.mycompany.pensiunan;
    exports com.mycompany.pensiunan.model; 
    exports com.mycompany.pensiunan.service; 
    exports com.mycompany.pensiunan.dao; 
    exports com.mycompany.pensiunan.controller;

    // WAJIB untuk FXMLLoader
    opens com.mycompany.pensiunan.controller to javafx.fxml;
    opens com.mycompany.pensiunan to javafx.fxml;
    // Tambahkan opens untuk semua package yang diakses oleh FXML atau Reflection
}