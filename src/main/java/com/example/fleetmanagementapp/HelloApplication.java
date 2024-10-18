package com.example.fleetmanagementapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    // Static variable to hold the primary stage reference
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        // Store the primary stage reference in the static variable
        primaryStage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 853, 531);

        // Set the title
        stage.setTitle("Fleet Management App");

        // Set the icon for the application (assuming the icon is in the resources folder)
        Image icon = new Image(HelloApplication.class.getResourceAsStream("/fleet_icon.png"));
        stage.getIcons().add(icon);

        // Set the scene and show the stage
        stage.setScene(scene);
        stage.show();
    }

    // Static method to return the primary stage
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch();
    }
}