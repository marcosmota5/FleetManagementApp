package com.example.fleetmanagementapp.Helpers;

import com.example.fleetmanagementapp.Controllers.HomeController;
import com.example.fleetmanagementapp.HelloApplication;
import com.example.fleetmanagementapp.Models.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Objects;

public class SceneSwitcher {

    public static void switchScene(AnchorPane currentAnchorPane, String fxml) throws IOException  {
        AnchorPane nextAnchorPane=FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource(fxml)));
        currentAnchorPane.getChildren().removeAll();
        currentAnchorPane.getChildren().setAll(nextAnchorPane);
    }

    public static void switchScene(AnchorPane currentAnchorPane, String fxml, User user) throws IOException  {
        // Use FXMLLoader to load the FXML file
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(HelloApplication.class.getResource(fxml)));

        // Load the new AnchorPane from the FXML file
        AnchorPane nextAnchorPane = loader.load();

        // Get the controller of the HomeController
        Object controller = loader.getController();

        // Pass the user instance if it's HomeController
        if (controller instanceof HomeController) {
            ((HomeController)controller).setCurrentUser(user); // Pass the user to HomeController
        }

        // Replace the current AnchorPane's children with the new one
        currentAnchorPane.getChildren().removeAll();
        currentAnchorPane.getChildren().setAll(nextAnchorPane);
    }
}