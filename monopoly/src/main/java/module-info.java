module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;

    exports gameset.functionality to com.fasterxml.jackson.databind;
    opens gameset.functionality to com.fasterxml.jackson.databind;


    opens screens to javafx.fxml;
    exports screens;
    exports controllers;
    opens controllers to javafx.fxml;
}