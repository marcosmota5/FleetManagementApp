package com.example.fleetmanagementapp;

import com.example.fleetmanagementapp.Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");

        User user = User.executeLogin("marcos.mota", "Marcos@123");

        welcomeText.setText(user.getProfile().getName());
    }


}