package com.example.fleetmanagementapp.Controllers;

import com.example.fleetmanagementapp.Helpers.StageUtils;
import com.example.fleetmanagementapp.Helpers.StringUtils;
import com.example.fleetmanagementapp.Models.Company;
import com.example.fleetmanagementapp.Models.Vehicle;
import com.example.fleetmanagementapp.Models.User;
import com.example.fleetmanagementapp.Models.Vehicle;
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
import java.util.ArrayList;
import java.util.List;

public class VehicleDetailController {

    @FXML
    private AnchorPane anpVehicleDetal;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSave;

    @FXML
    private ComboBox<String> cmbFueltType;

    @FXML
    private ComboBox<String> cmbStatus;

    @FXML
    private ComboBox<String> cmbType;

    @FXML
    private HBox hbxSaveButton;

    @FXML
    private Label lblBrand;

    @FXML
    private Label lblErrorMessage;

    @FXML
    private Label lblFuelLevel;

    @FXML
    private Label lblFuelType;

    @FXML
    private Label lblLicensePlate;

    @FXML
    private Label lblMileage;

    @FXML
    private Label lblModel;

    @FXML
    private Label lblStatus;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblType;

    @FXML
    private Label lblYear;

    @FXML
    private TextField txtFuelLevel;

    @FXML
    private TextField txtMileage;

    @FXML
    private ComboBox<Integer> cmbYear;

    @FXML
    private TextField txtBrand;

    @FXML
    private TextField txtLicensePlate;

    @FXML
    private TextField txtModel;

    // Validator class
    private Validator validator = new Validator();

    private User currentUser;
    private Vehicle targetVehicle;
    private ObservableList<String> typeList;
    private ObservableList<String> fuelTypeList;
    private ObservableList<String> statusList;

    @FXML
    public void initialize() {

        // Create a check for the validator to validate the type property
        validator.createCheck()
                .dependsOn("type", cmbType.valueProperty())
                .withMethod(c -> {
                    String selectedType = c.get("type");

                    // Check if no vehicle is selected
                    if (selectedType == null) {
                        c.error("Please select a type");
                    }
                })
                .decorates(cmbType)
                .immediate();

        // Create a check for the validator to validate the brand property
        validator.createCheck()
                .dependsOn("brand", txtBrand.textProperty())
                .withMethod(c -> {
                    String brand = c.get("brand");

                    if (brand.isEmpty()) {
                        c.error("Please inform the brand name");
                    }

                    if (brand.length() > 100) {
                        c.error("The brand field must have less than 100 characters");
                    }
                })
                .decorates(txtBrand)
                .immediate();

        // Create a check for the validator to validate the model property
        validator.createCheck()
                .dependsOn("model", txtModel.textProperty())
                .withMethod(c -> {
                    String model = c.get("model");

                    if (model.isEmpty()) {
                        c.error("Please inform the model name");
                    }

                    if (model.length() > 100) {
                        c.error("The model field must have less than 100 characters");
                    }
                })
                .decorates(txtModel)
                .immediate();

        // Create a check for the validator to validate the license plate property
        validator.createCheck()
                .dependsOn("licensePlate", txtLicensePlate.textProperty())
                .withMethod(c -> {
                    String licensePlate = c.get("licensePlate");

                    if (licensePlate.isEmpty()) {
                        c.error("Please inform the license plate");
                    }

                    if (licensePlate.length() > 10) {
                        c.error("The model field must not have more than 10 characters");
                    }
                })
                .decorates(txtLicensePlate)
                .immediate();

        // Create a check for the validator to validate the year property
        validator.createCheck()
                .dependsOn("year", cmbYear.valueProperty())
                .withMethod(c -> {
                    Integer selectedYear = c.get("year");

                    // Check if no vehicle is selected
                    if (selectedYear == null) {
                        c.error("Please select a year");
                    }
                })
                .decorates(cmbYear)
                .immediate();

        // Create a check for the validator to validate the fuel type property
        validator.createCheck()
                .dependsOn("fuelType", cmbFueltType.valueProperty())
                .withMethod(c -> {
                    String selectedFuelType = c.get("fuelType");

                    // Check if no vehicle is selected
                    if (selectedFuelType == null) {
                        c.error("Please select a fuel type");
                    }
                })
                .decorates(cmbFueltType)
                .immediate();

        // Create a check for the validator to validate the mileage property
        validator.createCheck()
                .dependsOn("mileage", txtMileage.textProperty())
                .withMethod(c -> {
                    String mileage = c.get("mileage");

                    // Check if no vehicle is selected
                    if (mileage.isEmpty()) {
                        c.error("Please inform the mileage");
                    }


                    // Check if the mileage is not a number
                    if (!StringUtils.isNumeric(mileage)) {
                        c.error("The mileage must be a number");
                    }
                })
                .decorates(txtMileage)
                .immediate();

        // Create a check for the validator to validate the fuel level property
        validator.createCheck()
                .dependsOn("fuelLevel", txtFuelLevel.textProperty())
                .withMethod(c -> {
                    String fuelLevel = c.get("fuelLevel");

                    // Check if no vehicle is selected
                    if (fuelLevel.isEmpty()) {
                        c.error("Please inform the fuel level");
                    }


                    // Check if the mileage is not a number
                    if (!StringUtils.isNumeric(fuelLevel)) {
                        c.error("The fuel level must be a number");
                    }
                })
                .decorates(txtFuelLevel)
                .immediate();

        // Create a check for the validator to validate the status property
        validator.createCheck()
                .dependsOn("status", cmbStatus.valueProperty())
                .withMethod(c -> {
                    String selectedStatus = c.get("status");

                    // Check if no vehicle is selected
                    if (selectedStatus == null) {
                        c.error("Please select a status");
                    }
                })
                .decorates(cmbStatus)
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

    public void setTargetVehicle(User user, Vehicle vehicle) {

        // Set the current user property
        currentUser = user;

        // If the vehicle id is 0, just the target as it because it's a new one
        targetVehicle = vehicle;

        // Create the lists
        typeList = FXCollections.observableArrayList("Car", "Motorcycle", "Truck");
        fuelTypeList = FXCollections.observableArrayList("Petrol", "Diesel", "Electric");
        statusList = FXCollections.observableArrayList("Available", "Rented", "Maintenance", "Retired");

        // Fill the year list starting from the most recent
        for (int i = LocalDate.now().getYear(); i >= 1900; i--) {
            cmbYear.getItems().add(i);
        }

        // Set the lists
        cmbType.setItems(typeList);
        cmbFueltType.setItems(fuelTypeList);
        cmbStatus.setItems(statusList);

        // Set the values
        if (targetVehicle.getId() > 0) {
            cmbType.setValue(vehicle.getType());
            txtBrand.setText(vehicle.getBrand());
            txtModel.setText(vehicle.getModel());
            txtLicensePlate.setText(vehicle.getLicensePlate());
            cmbYear.setValue(vehicle.getYear());
            cmbFueltType.setValue(vehicle.getFuelType());
            txtMileage.setText(Double.toString(vehicle.getMileage()));
            txtFuelLevel.setText(Double.toString(vehicle.getFuelLevel()));
            cmbStatus.setValue(vehicle.getStatus());
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
        int vehicleId = 0;

        // Try to get the execute the login and set the user
        try {
            vehicleId = Vehicle.saveVehicle(targetVehicle.getId(), txtLicensePlate.getText(), cmbType.getValue(),
                    txtBrand.getText(), txtModel.getText(), cmbYear.getValue(), cmbFueltType.getValue(),
                    Double.parseDouble(txtMileage.getText()), Double.parseDouble(txtFuelLevel.getText()), cmbStatus.getValue(), targetVehicle.getCompany().getId());
        } catch (Exception e) {
            lblErrorMessage.setVisible(true);
            lblErrorMessage.setText(e.getMessage());
        }

        // If the vehicle id is greater than 0, the vehicle was saved, so shows a message and switch scene
        if (vehicleId > 0) {

            // Create the alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Operation Completed");
            alert.setHeaderText("Vehicle Saved Successfully");
            alert.setContentText("The vehicle was saved in the database.");

            // Show the alert and wait for user to click "OK"
            alert.showAndWait();

            // Close the stage
            StageUtils.closeStage(anpVehicleDetal);
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        // Close the stage
        StageUtils.closeStage(anpVehicleDetal);
    }

}