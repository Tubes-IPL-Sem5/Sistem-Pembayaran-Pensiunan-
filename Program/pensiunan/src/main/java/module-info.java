module com.mycompany.pensiunan {
    requires javafx.controls;
    requires javafx.fxml;

    exports com.mycompany.pensiunan;

    // WAJIB untuk FXMLLoader
    opens com.mycompany.pensiunan.controller to javafx.fxml;
    opens com.mycompany.pensiunan to javafx.fxml;
}
