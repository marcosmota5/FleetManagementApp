package com.example.fleetmanagementapp.Controllers;

import com.example.fleetmanagementapp.Helpers.StageUtils;
import com.example.fleetmanagementapp.Helpers.StringUtils;
import com.example.fleetmanagementapp.Models.*;
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

import java.time.LocalDate;

public class RideDetailController {

    @FXML
    private AnchorPane anpRideDetail;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSave;

    @FXML
    private ComboBox<User> cmbUser;

    @FXML
    private ComboBox<Vehicle> cmbVehicle;

    @FXML
    private DatePicker dtpEndDate;

    @FXML
    private DatePicker dtpStartDate;

    @FXML
    private HBox hbxSaveButton;

    @FXML
    private Label lblComments;

    @FXML
    private Label lblEndDate;

    @FXML
    private Label lblEndLocation;

    @FXML
    private Label lblErrorMessage;

    @FXML
    private Label lblFuelConsumed;

    @FXML
    private Label lblKilometersDriven;

    @FXML
    private Label lblStartDate;

    @FXML
    private Label lblStartLocation;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblUser;

    @FXML
    private Label lblVehicle;

    @FXML
    private TextField txtComments;

    @FXML
    private TextField txtEndLocation;

    @FXML
    private TextField txtFuelConsumed;

    @FXML
    private TextField txtKilometersDriven;

    @FXML
    private TextField txtStartLocation;

    // Validator class
    private Validator validator = new Validator();

    private User currentUser;
    private RideHistory targetRide;
    private ObservableList<Vehicle> vehicleList;
    private ObservableList<User> userList;

    @FXML
    public void initialize() {

        // Create a check for the validator to validate the startDate property
        validator.createCheck()
                .dependsOn("startDate", dtpStartDate.valueProperty())
                .withMethod(c -> {
                    LocalDate startDate = c.get("startDate");

                    // Validate if the date has been selected
                    if (startDate == null) {
                        c.error("Please select the start date");
                        return;
                    }

                    // Validate if the selected date is in the future
                    if (startDate.isAfter(LocalDate.now())) {
                        c.error("Start date cannot be in the future");
                    }
                })
                .decorates(dtpStartDate)
                .immediate();

        // Create a check for the validator to validate the start location property
        validator.createCheck()
                .dependsOn("startLocation", txtStartLocation.textProperty())
                .withMethod(c -> {
                    String startLocation = c.get("startLocation");

                    if (startLocation.isEmpty()) {
                        c.error("Please inform the start location");
                    }

                    if (startLocation.length() > 100) {
                        c.error("The start location field must have less than 100 characters");
                    }
                })
                .decorates(txtStartLocation)
                .immediate();

        // Create a check for the validator to validate the start location property
        validator.createCheck()
                .dependsOn("endLocation", txtEndLocation.textProperty())
                .withMethod(c -> {
                    String endLocation = c.get("endLocation");

                    if (endLocation != null && endLocation.length() > 100) {
                        c.error("The start location field must have less than 100 characters");
                    }
                })
                .decorates(txtEndLocation)
                .immediate();

        // Create a check for the validator to validate the vehicle property
        validator.createCheck()
                .dependsOn("vehicle", cmbVehicle.valueProperty())
                .withMethod(c -> {
                    Vehicle selectedVehicle = c.get("vehicle");

                    // Check if no profile is selected
                    if (selectedVehicle == null) {
                        c.error("Please select a vehicle");
                    }
                })
                .decorates(cmbVehicle)
                .immediate();

        // Create a check for the validator to validate the kilometers driven property
        validator.createCheck()
                .dependsOn("kilometersDriven", txtKilometersDriven.textProperty())
                .withMethod(c -> {
                    String kilometersDriven = c.get("kilometersDriven");

                    // Check if the kilometers driven is not a number
                    if (!StringUtils.isNumericOrEmpty(kilometersDriven)) {
                        c.error("The kilometers driven must be empty or a number");
                    }
                })
                .decorates(txtKilometersDriven)
                .immediate();

        // Create a check for the validator to validate the fuel consumed property
        validator.createCheck()
                .dependsOn("fuelConsumed", txtFuelConsumed.textProperty())
                .withMethod(c -> {
                    String fuelConsumed = c.get("fuelConsumed");

                    // Check if the mileage is not a number
                    if (!StringUtils.isNumericOrEmpty(fuelConsumed)) {
                        c.error("The fuel consumed must be empty or a number");
                    }
                })
                .decorates(txtFuelConsumed)
                .immediate();

        // Create a check for the validator to validate the user property
        validator.createCheck()
                .dependsOn("user", cmbUser.valueProperty())
                .withMethod(c -> {
                    User selectedUser = c.get("user");

                    // Check if no user is selected
                    if (selectedUser == null) {
                        c.error("Please select an user");
                    }
                })
                .decorates(cmbUser)
                .immediate();

        // Create a check for the validator to validate the password property
        TooltipWrapper<Button> signUpWrapper = new TooltipWrapper<>(
                btnSave,
                validator.containsErrorsProperty(),
                Bindings.concat("Cannot save vehicle:\n", validator.createStringBinding())
        );

        // Add the tooltip
        hbxSaveButton.getChildren().add(signUpWrapper);
    }

    public void setTargetRide(User user, Company company, RideHistory ride) {

        // Set the current user property
        currentUser = user;

        // If the ride id is 0, just the target as it because it's a new one
        targetRide = ride;

        // Get the list of users from the database
        try {
            vehicleList = FXCollections.observableArrayList(Vehicle.getAllVehiclesByCompanyId(company.getId()));
            userList = FXCollections.observableArrayList(User.getAllUsers());

            // Set the items in the combobox
            cmbVehicle.setItems(vehicleList);
            cmbUser.setItems(userList);
        } catch (Exception e) {
            lblErrorMessage.setVisible(true);
            lblErrorMessage.setText(e.getMessage());
        }

        // Set the values
        if (targetRide.getId() > 0) {
            dtpStartDate.setValue(ride.getStartDate());
            txtStartLocation.setText(ride.getStartLocation());
            dtpEndDate.setValue(ride.getEndDate());
            txtEndLocation.setText(ride.getEndLocation());
            cmbVehicle.setValue(ride.getVehicle());

            if(ride.getKilometersDriven() != null) {
                txtKilometersDriven.setText(Double.toString(ride.getKilometersDriven()));
            }

            if(ride.getFuelConsumed() != null) {
                txtFuelConsumed.setText(Double.toString(ride.getFuelConsumed()));
            }

            cmbUser.setValue(ride.getUser());
            txtComments.setText(ride.getComments());
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

        // Declare the vehicle id
        int rideId = 0;

        // Try to get the execute the login and set the user
        try {
            rideId = RideHistory.saveRide(
                    targetRide.getId(),
                    txtStartLocation.getText(),
                    txtEndLocation.getText(),
                    dtpStartDate.getValue(),
                    dtpEndDate.getValue(),  // Handle null for date picker if no date is selected
                    txtKilometersDriven.getText() == null || txtKilometersDriven.getText().isEmpty() ? null : Double.parseDouble(txtKilometersDriven.getText()),
                    txtFuelConsumed.getText() == null || txtFuelConsumed.getText().isEmpty() ? null : Double.parseDouble(txtFuelConsumed.getText()),
                    txtComments.getText(),
                    cmbVehicle.getValue().getId(),
                    cmbUser.getValue().getId()
            );
        } catch (Exception e) {
            lblErrorMessage.setVisible(true);
            lblErrorMessage.setText(e.getMessage());
        }

        // If the ride id is greater than 0, the vehicle was saved, so shows a message and switch scene
        if (rideId > 0) {

            // Create the alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Operation Completed");
            alert.setHeaderText("Ride Saved Successfully");
            alert.setContentText("The ride was saved in the database.");

            // Show the alert and wait for user to click "OK"
            alert.showAndWait();

            // Close the stage
            StageUtils.closeStage(anpRideDetail);
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        // Close the stage
        StageUtils.closeStage(anpRideDetail);
    }
}
