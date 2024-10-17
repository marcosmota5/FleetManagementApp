package com.example.fleetmanagementapp.Controllers;

import com.example.fleetmanagementapp.Helpers.AppPreferences;
import com.example.fleetmanagementapp.Helpers.PasswordHelper;
import com.example.fleetmanagementapp.Helpers.SceneSwitcher;
import com.example.fleetmanagementapp.Models.Company;
import com.example.fleetmanagementapp.Models.Profile;
import com.example.fleetmanagementapp.Models.User;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import net.synedra.validatorfx.TooltipWrapper;
import net.synedra.validatorfx.Validator;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class RegisterController {

    @FXML
    private ToggleGroup tgpSex;

    @FXML
    private AnchorPane anpRegister;

    @FXML
    private Button btnHideConfirmPassword;

    @FXML
    private Button btnHidePassword;

    @FXML
    private HBox hbxRegisterButton;

    @FXML
    private Button btnRegister;

    @FXML
    private Button btnShowConfirmPassword;

    @FXML
    private Button btnShowPassword;

    @FXML
    private ComboBox<Profile> cmbProfile;

    @FXML
    private DatePicker dtpBirthDate;

    @FXML
    private Label lblBirthDate;

    @FXML
    private Button lblCancel;

    @FXML
    private Label lblCompanies;

    @FXML
    private Label lblConfirmPassword;

    @FXML
    private Label lblEmail;

    @FXML
    private Label lblErrorMessage;

    @FXML
    private Label lblFirstName;

    @FXML
    private Label lblLastName;

    @FXML
    private Label lblLogin;

    @FXML
    private Label lblPassword;

    @FXML
    private Label lblPersonalInfo;

    @FXML
    private Label lblPersonalInfo1;

    @FXML
    private Label lblPhoneNumber;

    @FXML
    private Label lblProfile;

    @FXML
    private Label lblSex;

    @FXML
    private Label lblUserInfo;

    @FXML
    private ListView<Company> lsvCompanies;

    @FXML
    private PasswordField pswConfirmPassword;

    @FXML
    private PasswordField pswPassword;

    @FXML
    private RadioButton rbnSexMale;

    @FXML
    private RadioButton rbnSexFemale;

    @FXML
    private TextField txtConfirmPasswordVisible;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtLastName;

    @FXML
    private TextField txtLogin;

    @FXML
    private TextField txtPasswordVisible;

    @FXML
    private TextField txtPhoneNumber;

    // Validator class
    private Validator validator = new Validator();

    private ObservableList<Profile> profileList;

    @FXML
    public void initialize() {

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

        // Add a listener to the textProperty() to detect text changes
        pswConfirmPassword.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                txtConfirmPasswordVisible.setText(newValue);
            }
        });

        // Add a listener to the textProperty() to detect text changes
        txtConfirmPasswordVisible.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                pswConfirmPassword.setText(newValue);
            }
        });

        // Create a check for the validator to validate the first name property
        validator.createCheck()
                .dependsOn("firstName", txtFirstName.textProperty())
                .withMethod(c -> {
                    String firstName = c.get("firstName");

                    if (firstName.isEmpty()) {
                        c.error("Please inform your first name");
                    }

                    if (firstName.length() > 100) {
                        c.error("The first name field must have less than 100 characters");
                    }
                })
                .decorates(txtFirstName)
                .immediate();

        // Create a check for the validator to validate the last name property
        validator.createCheck()
                .dependsOn("lastName", txtLastName.textProperty())
                .withMethod(c -> {
                    String lastName = c.get("lastName");

                    if (lastName.isEmpty()) {
                        c.error("Please inform your last name");
                    }

                    if (lastName.length() > 100) {
                        c.error("The last name field must have less than 100 characters");
                    }
                })
                .decorates(txtLastName)
                .immediate();

        // Create a check for the validator to validate the sex property
        validator.createCheck()
                .dependsOn("sex", tgpSex.selectedToggleProperty())
                .withMethod(c -> {
                    Toggle selectedSex = c.get("sex");

                    if (selectedSex == null) {
                        c.error("Please select a gender");
                    }
                })
                .decorates(rbnSexMale)
                .immediate();

        // Create a check for the validator to validate the birth date property
        validator.createCheck()
                .dependsOn("birthDate", dtpBirthDate.valueProperty())
                .withMethod(c -> {
                    LocalDate birthDate = c.get("birthDate");

                    // Validate if the date has been selected
                    if (birthDate == null) {
                        c.error("Please select your birth date");
                        return;
                    }

                    // Validate if the selected date is in the future
                    if (birthDate.isAfter(LocalDate.now())) {
                        c.error("Birth date cannot be in the future");
                    }

                    // Validate if the user is at least 18 years old
                    if (birthDate.isAfter(LocalDate.now().minusYears(10))) {
                        c.error("You must be at least 10 years old");
                    }
                })
                .decorates(dtpBirthDate)
                .immediate();

        // Create a check for the validator to validate the phone number property
        validator.createCheck()
                .dependsOn("phoneNumber", txtPhoneNumber.textProperty())
                .withMethod(c -> {
                    String phoneNumber = c.get("phoneNumber");

                    if (phoneNumber.length() > 20) {
                        c.error("The phone number field must have less than 20 characters");
                    }
                })
                .decorates(txtPhoneNumber)
                .immediate();

        // Create a check for the validator to validate the login property
        validator.createCheck()
                .dependsOn("login", txtLogin.textProperty())
                .withMethod(c -> {
                    String login = c.get("login");

                    if (login.isEmpty()) {
                        c.error("Please inform your login");
                    }

                    if (login.length() > 50) {
                        c.error("The login field must have less than 50 characters");
                    }
                })
                .decorates(txtLogin)
                .immediate();

        // Create a check for the validator to validate the email property
        validator.createCheck()
                .dependsOn("email", txtEmail.textProperty())
                .withMethod(c -> {
                    String email = c.get("email");

                    if (email.isEmpty()) {
                        c.error("Please inform your email");
                    }

                    if (email.length() > 255) {
                        c.error("The email field must have less than 255 characters");
                    }
                })
                .decorates(txtEmail)
                .immediate();

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

                    if (!PasswordHelper.isStrong(password)) {
                        c.error("The password is not strong enough. It must have at least: \n\n8 characters\nOne number\nOne uppercase letters\nOne lowercase letters\nOne special character");
                    }
                })
                .decorates(pswPassword)
                .immediate();

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

                    if (!PasswordHelper.isStrong(password)) {
                        c.error("The password is not strong enough. It must have at least: \n\n8 characters\nOne number\nOne uppercase letters\nOne lowercase letters\nOne special character");
                    }
                })
                .decorates(txtPasswordVisible)
                .immediate();

        // Create a check for the validator to validate the password property
        validator.createCheck()
                .dependsOn("confirmPassword", pswConfirmPassword.textProperty())
                .withMethod(c -> {
                    String password = c.get("confirmPassword");

                    if (password.isEmpty()) {
                        c.error("Please inform the password confirmation");
                    }

                    if (!password.equals(pswPassword.getText())) {
                        c.error("The password confirmation does not match with the password field");
                    }
                })
                .decorates(pswConfirmPassword)
                .immediate();

        // Create a check for the validator to validate the password visible property
        validator.createCheck()
                .dependsOn("confirmPasswordVisible", txtConfirmPasswordVisible.textProperty())
                .withMethod(c -> {
                    String password = c.get("confirmPasswordVisible");

                    if (password.isEmpty()) {
                        c.error("Please inform the password confirmation");
                    }

                    if (!password.equals(pswPassword.getText())) {
                        c.error("The password field does not match the password confirmation");
                    }
                })
                .decorates(txtConfirmPasswordVisible)
                .immediate();


        // Create a check for the validator to validate the profile property
        validator.createCheck()
                .dependsOn("profile", cmbProfile.valueProperty())
                .withMethod(c -> {
                    Profile selectedProfile = c.get("profile");

                    // Check if no profile is selected
                    if (selectedProfile == null) {
                        c.error("Please select a profile");
                    }
                })
                .decorates(cmbProfile)
                .immediate();


        // Create a check for the validator to validate the password property
        TooltipWrapper<Button> signUpWrapper = new TooltipWrapper<>(
                btnRegister,
                validator.containsErrorsProperty(),
                Bindings.concat("Cannot perform login:\n", validator.createStringBinding())
        );

        // Add the tooltip
        hbxRegisterButton.getChildren().add(signUpWrapper);

        // Get the list of users from the database
        try {
            profileList = FXCollections.observableArrayList(Profile.getAllProfiles());

            // Set the items in the combobox
            cmbProfile.setItems(profileList);
        } catch (Exception e) {
            lblErrorMessage.setVisible(true);
            lblErrorMessage.setText(e.getMessage());
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


    // Method to show the password
    public void showConfirmPassword(ActionEvent event)  {
        pswConfirmPassword.setVisible(false);
        txtConfirmPasswordVisible.setVisible(true);
        btnShowConfirmPassword.setVisible(false);
        btnHideConfirmPassword.setVisible(true);
    }

    // Method to hide the password
    public void hideConfirmPassword(ActionEvent event)  {
        pswConfirmPassword.setVisible(true);
        txtConfirmPasswordVisible.setVisible(false);
        btnShowConfirmPassword.setVisible(true);
        btnHideConfirmPassword.setVisible(false);
    }

    // Register the user
    public void registerUser(ActionEvent event) throws IOException {

        // Hide the error message
        lblErrorMessage.setVisible(false);

        // Call the validate method and, if it's false (meaning there are errors), return
        if (!validator.validate()) {
            return;
        }

        // Get the user id
        int userId = 0;

        // Try to get the execute the login and set the user
        try {
            userId = User.registerUser(txtFirstName.getText(), txtLastName.getText(),
                    tgpSex.getSelectedToggle().equals("Male") ? "M" : "F", dtpBirthDate.getValue(),
                    txtPhoneNumber.getText(), txtEmail.getText(), txtLogin.getText(), pswPassword.getText(),
                    cmbProfile.getValue().getId());
        } catch (Exception e) {
            lblErrorMessage.setVisible(true);
            lblErrorMessage.setText(e.getMessage());
        }

        // If the user id is greater than 0, the user was registered successfully, so shows a message and switch scene
        if (userId > 0) {

            // Create the alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registration Successful");
            alert.setHeaderText("User registered successfully");
            alert.setContentText("The user was registered with success. Please execute the login to use the application.");

            // Show the alert and wait for user to click "OK"
            alert.showAndWait();

            // Switch the scene
            SceneSwitcher.switchScene(anpRegister, "login-view.fxml");
        }
    }

    public void cancel(ActionEvent event) throws IOException {
        SceneSwitcher.switchScene(anpRegister, "login-view.fxml");
    }
}