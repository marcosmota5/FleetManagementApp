package com.example.fleetmanagementapp.Controllers;

import com.example.fleetmanagementapp.HelloApplication;
import com.example.fleetmanagementapp.Helpers.StageUtils;
import com.example.fleetmanagementapp.Models.Company;
import com.example.fleetmanagementapp.Models.Profile;
import com.example.fleetmanagementapp.Models.RideHistory;
import com.example.fleetmanagementapp.Models.User;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class ProfileListController {

    @FXML
    private AnchorPane anpProfileList;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnEdit;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblErrorMessage;

    @FXML
    private TableColumn<Profile, LocalDate> tbcCreatedOn;

    @FXML
    private TableColumn<Profile, String> tbcDescription;

    @FXML
    private TableColumn<Profile, String> tbcName;

    @FXML
    private TableColumn<Profile, Integer> tbcPriority;

    @FXML
    private TableView<Profile> tvwProfiles;

    private User currentUser;
    private ObservableList<Profile> profileList;

    @FXML
    void add(ActionEvent event) {
        try {

            lblErrorMessage.setVisible(false);

            Profile newProfile = new Profile();

            // Get the stage from the node
            Stage stage = (Stage)anpProfileList.getScene().getWindow();

            // Open the Profile Detail window and pass the vehicle data to the controller
            StageUtils.openModalWindow(
                    "profile-detail-view.fxml",
                    stage,    // The owner stage
                    "Add New Profile",                        // The window title
                    (ProfileDetailController controller) -> {  // Lambda expression for the controller
                        controller.setTargetProfile(currentUser, newProfile);
                    }
            );

            // Refresh the table
            refreshTable();
        } catch (IOException e) {
            lblErrorMessage.setVisible(true);
            lblErrorMessage.setText(e.getMessage());
        }
    }

    @FXML
    void edit(ActionEvent event) {

        lblErrorMessage.setVisible(false);

        // Get the selected profile
        Profile selectedProfile = tvwProfiles.getSelectionModel().getSelectedItem();

        // If the selected profile is null, return
        if (selectedProfile == null) {
            return;
        }

        try {

            // Get the stage from the node
            Stage stage = (Stage)anpProfileList.getScene().getWindow();

            // Open the Profile Detail window and pass the vehicle data to the controller
            StageUtils.openModalWindow(
                    "profile-detail-view.fxml",
                    stage,    // The owner stage
                    "Edit Profile",                        // The window title
                    (ProfileDetailController controller) -> {  // Lambda expression for the controller
                        controller.setTargetProfile(currentUser, selectedProfile);
                    }
            );

            // Refresh the table
            refreshTable();
        } catch (IOException e) {
            lblErrorMessage.setVisible(true);
            lblErrorMessage.setText(e.getMessage());
        }
    }

    @FXML
    void delete(ActionEvent event) {

        lblErrorMessage.setVisible(false);

        // Get the selected profile
        Profile selectedProfile = tvwProfiles.getSelectionModel().getSelectedItem();

        // If the selected profile is null, return
        if (selectedProfile == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deletion Confirmation");
        alert.setHeaderText("Are you sure you want to delete the profile " + selectedProfile.toString() + "?");
        alert.setContentText("Click 'Yes' to confirm the deletion or 'No' to cancel.");

        // Show the dialog and wait for user response
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Profile.deleteProfile(selectedProfile.getId());

                    // Refresh the table
                    refreshTable();

                } catch (Exception e) {
                    lblErrorMessage.setVisible(true);
                    lblErrorMessage.setText(e.getMessage());
                }
            }
        });

    }

    @FXML
    public void initialize() {

        // Add a listener to the ComboBox to listen for changes in the selected value
        tvwProfiles.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Call your function whenever the selected profile changes
            btnEdit.setDisable(newValue == null);
            btnDelete.setDisable(newValue == null);
        });
    }

    public void setValues(User user) {

        // Set the current user property
        currentUser = user;

        // Call the refresh table method
        refreshTable();
    }

    private void refreshTable(){

        // Get the list of users from the database
        try {

            profileList = FXCollections.observableArrayList(Profile.getLowerPriorityProfiles(currentUser.getProfile().getId()));

            // Set cell value factories to extract values from RideHistory objects
            tbcName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
            tbcPriority.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPriority()));
            tbcDescription.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
            tbcCreatedOn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCreatedOn()));

            // Ensure ongoingRideList is populated with RideHistory objects
            tvwProfiles.setItems(FXCollections.observableArrayList(profileList));
        } catch (Exception e) {
            lblErrorMessage.setVisible(true);
            lblErrorMessage.setText(e.getMessage());
        }
    }

}
