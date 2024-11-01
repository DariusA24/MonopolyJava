module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;


    opens screens to javafx.fxml;
    exports screens;
}