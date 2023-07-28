module com.example.domaci2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.domaci2 to javafx.fxml;
    exports com.example.domaci2;
}