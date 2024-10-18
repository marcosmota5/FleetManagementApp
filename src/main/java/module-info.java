module com.example.fleetmanagementapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;
    requires java.prefs;

    opens com.example.fleetmanagementapp to javafx.fxml;
    exports com.example.fleetmanagementapp;
    exports com.example.fleetmanagementapp.Controllers;
    opens com.example.fleetmanagementapp.Controllers to javafx.fxml;
}