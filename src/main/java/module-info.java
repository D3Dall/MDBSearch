module org.floriotech {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires mongo.java.driver;
    exports org.floriotech;
    exports org.floriotech.controllers;
    opens org.floriotech.controllers to javafx.fxml;
}