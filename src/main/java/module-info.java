module com.example.marketplace {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires java.smartcardio;

    opens com.example.marketplace to javafx.fxml;
    opens com.example.marketplace.controller to javafx.fxml;
    opens com.example.marketplace.model to javafx.base;

    exports com.example.marketplace;
    exports com.example.marketplace.controller;
    exports com.example.marketplace.model;
}