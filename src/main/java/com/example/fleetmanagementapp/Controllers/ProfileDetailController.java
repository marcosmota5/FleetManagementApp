package com.example.fleetmanagementapp.Controllers;

import com.example.fleetmanagementapp.Helpers.SceneSwitcher;
import com.example.fleetmanagementapp.Helpers.StageUtils;
import com.example.fleetmanagementapp.Models.Profile;
import com.example.fleetmanagementapp.Models.User;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import net.synedra.validatorfx.TooltipWrapper;
import net.synedra.validatorfx.Validator;

public class ProfileDetailController {

    @FXML
    private AnchorPane anpProfileDetail;

    @FXML
    private Button btnSave;

    @FXML
    private HBox hbxSaveButton;

    @FXML
    private Button lblCancel;

    @FXML
    private Label lblDescription;

    @FXML
    private Label lblErrorMessage;

    @FXML
    private Label lblName;

    @FXML
    private Label lblPriority;

    @FXML
    private Label lblTitle;

    @FXML
    private ComboBox<Integer> cmbPriority;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtName;

    // Validator class
    private Validator validator = new Validator();

    private User currentUser;
    private Profile targetProfile;
    private ObservableList<Integer> priorityList;

    @FXML
    public void initialize() {
        // Create a check for the validator to validate the name property
        validator.createCheck()
                .dependsOn("name", txtName.textProperty())
                .withMethod(c -> {
                    String name = c.get("name");

                    if (name.isEmpty()) {
                        c.error("Please inform the profile name");
                    }

                    if (name.length() > 255) {
                        c.error("The name field must have less than 255 characters");
                    }
                })
                .decorates(txtName)
                .immediate();

        // Create a check for the validator to validate the priority property
        validator.createCheck()
                .dependsOn("priority", cmbPriority.valueProperty())
                .withMethod(c -> {
                    Integer selectedPriority = c.get("priority");

                    // Check if no profile is selected
                    if (selectedPriority == null) {
                        c.error("Please select a priority");
                    }
                })
                .decorates(cmbPriority)
                .immediate();

        // Create a check for the validator to validate the description property
        validator.createCheck()
                .dependsOn("description", txtDescription.textProperty())
                .withMethod(c -> {
                    String description = c.get("description");

                    if (description.length() > 500) {
                        c.error("The description field must have less than 500 characters");
                    }
                })
                .decorates(txtDescription)
                .immediate();

        // Create a check for the validator to validate the password property
        TooltipWrapper<Button> signUpWrapper = new TooltipWrapper<>(
                btnSave,
                validator.containsErrorsProperty(),
                Bindings.concat("Cannot save profile:\n", validator.createStringBinding())
        );

        // Add the tooltip
        hbxSaveButton.getChildren().add(signUpWrapper);

    }

    public void setTargetProfile(User user, Profile profile) {

        // Set the current user property
        currentUser = user;

        // If the profile id is 0, just the target as it because it's a new one
        //if (profile.getId() == 0) {
            targetProfile = profile;
        //}

        // Set the values
        if (targetProfile.getId() > 0) {
            txtName.setText(profile.getName());
            cmbPriority.setValue(profile.getPriority());
            txtDescription.setText(profile.getDescription());
        }


        // Get the list of users from the database
        try {

            // Declare the priority to get
            int priorityToGet = 0;

            // If it's a new profile, set the priority to get as the current user profile priority, otherwise set it
            // as the target profile priority
            if(targetProfile.getId() == 0) {
                priorityToGet = currentUser.getProfile().getPriority();
            }
            else {
                priorityToGet = targetProfile.getPriority();
            }

            priorityList = FXCollections.observableArrayList(Profile.getPrioritiesByPriorityAndCondition(priorityToGet,
                    targetProfile.getId() == 0));

            // Set the items in the combobox
            cmbPriority.setItems(priorityList);
        } catch (Exception e) {
            lblErrorMessage.setVisible(true);
            lblErrorMessage.setText(e.getMessage());
        }
    }

    @FXML
    void save(ActionEvent event) {

        // Hide the error message
        lblErrorMessage.setVisible(false);

        // Call the validate method and, if it's false (meaning there are errors), return
        if (!validator.validate()) {
            return;
        }

        // Declare the profile id
        int profileId = 0;

        // Try to get the execute the login and set the user
        try {
            profileId = Profile.saveProfile(targetProfile.getId(), txtName.getText(), cmbPriority.getValue(),
                    txtDescription.getText());
        } catch (Exception e) {
            lblErrorMessage.setVisible(true);
            lblErrorMessage.setText(e.getMessage());
        }

        // If the profile id is greater than 0, the profile was saved, so shows a message and switch scene
        if (profileId > 0) {

            // Create the alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Operation Completed");
            alert.setHeaderText("Profile Saved Successfully");
            alert.setContentText("The profile was saved in the database.");

            // Show the alert and wait for user to click "OK"
            alert.showAndWait();

            // Close the stage
            StageUtils.closeStage(anpProfileDetail);
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        // Close the stage
        StageUtils.closeStage(anpProfileDetail);
    }

}
