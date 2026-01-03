module com.mycompany.pensiunan {
    // Module wajib untuk JavaFX dan Database
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    // Module tambahan (sesuai kode user sebelumnya)
    requires java.base;
    requires spring.security.crypto;
    requires java.desktop;
    requires java.logging;

    // EXPORTS: Agar package ini bisa dilihat oleh module lain
    exports com.mycompany.pensiunan;
    exports com.mycompany.pensiunan.config;
    exports com.mycompany.pensiunan.controller;
    exports com.mycompany.pensiunan.dao;
    exports com.mycompany.pensiunan.model;
    exports com.mycompany.pensiunan.service;
    exports com.mycompany.pensiunan.util;

    // OPENS: SANGAT PENTING UNTUK INTELLIJ & JAVAFX
    // 1. Agar FXML bisa akses Controller
    opens com.mycompany.pensiunan.controller to javafx.fxml;

    // 2. Agar TableView bisa membaca getter/setter di Model (SOLUSI TABEL KOSONG)
    opens com.mycompany.pensiunan.model to javafx.base;
}