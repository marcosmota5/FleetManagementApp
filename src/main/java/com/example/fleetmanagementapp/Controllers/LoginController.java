package com.example.fleetmanagementapp.Controllers;

import com.example.fleetmanagementapp.Helpers.SceneSwitcher;
import com.example.fleetmanagementapp.Helpers.AppPreferences;
import com.example.fleetmanagementapp.Models.User;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import net.synedra.validatorfx.TooltipWrapper;
import net.synedra.validatorfx.Validator;
import javafx.scene.control.Hyperlink;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class LoginController {

    @FXML
    private AnchorPane anpLogin;

    @FXML
    private AnchorPane anpExecuteLogin;

    @FXML
    private HBox hbxLoginButton;

    @FXML
    private TextField txtLoginOrEmail;

    @FXML
    private PasswordField pswPassword;

    @FXML
    private TextField txtPasswordVisible;

    @FXML
    private CheckBox chbRememberMe;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnShowPassword;

    @FXML
    private Button btnHidePassword;

    @FXML
    private Label lblErrorMessage;

    @FXML
    private RadioButton rbnThemeDark;

    @FXML
    private RadioButton rbnThemeLight;

    @FXML
    private ToggleGroup tgpTheme;

    @FXML
    private Hyperlink hlkGitHubRepository;

    // Validator class
    private Validator validator = new Validator();

    @FXML
    public void initialize() {

        // Load the saved login from preferences and display it
        String[] preferences = AppPreferences.loadPreferences();
        txtLoginOrEmail.setText(preferences[0]);
        chbRememberMe.setSelected(Boolean.parseBoolean(preferences[1]));
        if (preferences[2].equals("Dark")) {
            tgpTheme.selectToggle(rbnThemeDark);
        } else {
            tgpTheme.selectToggle(rbnThemeLight);
        }
        changeTheme(preferences[2]);

        // Add a listener to the textProperty() to detect text changes
        pswPassword.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                txtPasswordVisible.setText(newValue);
            }
        });

        // Add a listener to the textProperty() to detect text changes
        txtPasswordVisible.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                pswPassword.setText(newValue);
            }
        });

        // Add a listener to the ToggleGroup to detect changes
        tgpTheme.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) { // Check if a toggle is selected
                RadioButton selectedRadioButton = (RadioButton) newValue;
                String selectedValue = selectedRadioButton.getText();
                changeTheme(selectedValue);
            }
        });

        // Create a check for the validator to validate the login or email property
        validator.createCheck()
                .dependsOn("loginOrEmail", txtLoginOrEmail.textProperty())
                .withMethod(c -> {
                    String loginOrEmail = c.get("loginOrEmail");

                    if (loginOrEmail.isEmpty()) {
                        c.error("Please inform your login or email");
                    }

                    if (loginOrEmail.length() > 255) {
                        c.error("The login or email field must have less than 255 characters");
                    }
                })
                .decorates(txtLoginOrEmail)
                .immediate();
        ;

        // Create a check for the validator to validate the password property
        validator.createCheck()
                .dependsOn("password", pswPassword.textProperty())
                .withMethod(c -> {
                    String password = c.get("password");

                    if (password.isEmpty()) {
                        c.error("Please inform your password");
                    }

                    if (password.length() > 64) {
                        c.error("The password field must have less than 64 characters");
                    }
                })
                .decorates(pswPassword)
                .immediate();
        ;

        // Create a check for the validator to validate the password visible property
        validator.createCheck()
                .dependsOn("passwordVisible", txtPasswordVisible.textProperty())
                .withMethod(c -> {
                    String password = c.get("passwordVisible");

                    if (password.isEmpty()) {
                        c.error("Please inform your password");
                    }

                    if (password.length() > 64) {
                        c.error("The password field must have less than 64 characters");
                    }
                })
                .decorates(txtPasswordVisible)
                .immediate();
        ;

        // Create a check for the validator to validate the password property
        TooltipWrapper<Button> signUpWrapper = new TooltipWrapper<>(
                btnLogin,
                validator.containsErrorsProperty(),
                Bindings.concat("Cannot perform login:\n", validator.createStringBinding())
        );

        // Add the tooltip
        hbxLoginButton.getChildren().add(signUpWrapper);
    }

    private void changeTheme(String theme){
        if (theme.equals("Dark")) {
            Application.setUserAgentStylesheet(getClass().getResource("/primer-dark.css").toExternalForm());
        } else {
            Application.setUserAgentStylesheet(getClass().getResource("/primer-light.css").toExternalForm());
        }
    }

    // Method to show the password
    public void showPassword(ActionEvent event)  {
        pswPassword.setVisible(false);
        txtPasswordVisible.setVisible(true);
        btnShowPassword.setVisible(false);
        btnHidePassword.setVisible(true);
    }

    // Method to hide the password
    public void hidePassword(ActionEvent event)  {
        pswPassword.setVisible(true);
        txtPasswordVisible.setVisible(false);
        btnShowPassword.setVisible(true);
        btnHidePassword.setVisible(false);
    }

    // Method to execute the login considering the fields
    public void executeLogin(ActionEvent event) throws IOException {

        // Hide the error message
        lblErrorMessage.setVisible(false);

        // Call the validate method and, if it's false (meaning there are errors), return
        if (!validator.validate()) {
            return;
        }

        // Get the user
        User user = null;

        // Try to get the execute the login and set the user
        try {
            user = User.executeLogin(txtLoginOrEmail.getText(), pswPassword.getText());
        } catch (Exception e) {
            lblErrorMessage.setVisible(true);
            lblErrorMessage.setText(e.getMessage());
        }

        // If the user is not null, switch scene and return
        if (user != null) {

            // If the remember me checkbox is selected, save the preference, otherwise clear the preferences
            if (chbRememberMe.isSelected()) {
                AppPreferences.savePreferences(txtLoginOrEmail.getText(), true,
                        ((RadioButton)tgpTheme.getSelectedToggle()).getText());
            } else {
                AppPreferences.clearPreferences();
            }

            // Get the stage from anchor
            Stage stage = (Stage)anpLogin.getScene().getWindow();

            // Resize the stage
            stage.setWidth(1296);
            stage.setHeight(807);

            // Switch the scene
            SceneSwitcher.switchScene(anpLogin, "home-view.fxml", user);
        }
    }

    public void registerUser(ActionEvent event) throws IOException {
        // Switch the scene
        SceneSwitcher.switchScene(anpLogin, "register-view.fxml");
    }

    public void openGitHubRepository(ActionEvent event) throws IOException {
        try {
            Desktop.getDesktop().browse(new URL("https://github.com/marcosmota5/FleetManagementApp").toURI());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}