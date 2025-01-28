module org.example.demo {
    requires json.simple;
    requires javafx.fxml;
    requires javafx.controls;
    opens screens to javafx.fxml;
    exports screens;
    opens controllers to javafx.fxml;
}