package com.example.fleetmanagementapp.Controllers;

import com.example.fleetmanagementapp.HelloApplication;
import com.example.fleetmanagementapp.Helpers.StageUtils;
import com.example.fleetmanagementapp.Models.Company;
import com.example.fleetmanagementapp.Models.Vehicle;
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

public class VehicleListController {

    @FXML
    private AnchorPane anpVehicleList;

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
    private TableColumn<Vehicle, String> tbcBrand;

    @FXML
    private TableColumn<Vehicle, LocalDate> tbcCreatedOn;

    @FXML
    private TableColumn<Vehicle, Double> tbcFuelLevel;

    @FXML
    private TableColumn<Vehicle, String> tbcFuelType;

    @FXML
    private TableColumn<Vehicle, String> tbcLicensePlate;

    @FXML
    private TableColumn<Vehicle, Double> tbcMileage;

    @FXML
    private TableColumn<Vehicle, String> tbcModel;

    @FXML
    private TableColumn<Vehicle, String> tbcStatus;

    @FXML
    private TableColumn<Vehicle, String> tbcType;

    @FXML
    private TableColumn<Vehicle, Integer> tbcYear;

    @FXML
    private TableView<Vehicle> tvwVehicles;

    private User currentUser;
    private Company currentCompany;
    private ObservableList<Vehicle> vehicleList;

    @FXML
    void add(ActionEvent event) {
        try {

            lblErrorMessage.setVisible(false);

            Vehicle newVehicle = new Vehicle();
            newVehicle.setCompany(currentCompany);

            // Get the stage from the node
            Stage stage = (Stage)anpVehicleList.getScene().getWindow();

            // Open the Vehicle Detail window and pass the vehicle data to the controller
            StageUtils.openModalWindow(
                    "vehicle-detail-view.fxml",
                    stage,    // The owner stage
                    "Add New Vehicle",                        // The window title
                    (VehicleDetailController controller) -> {  // Lambda expression for the controller
                        controller.setTargetVehicle(currentUser, newVehicle);
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

        // Get the selected vehicle
        Vehicle selectedVehicle = tvwVehicles.getSelectionModel().getSelectedItem();

        // If the selected vehicle is null, return
        if (selectedVehicle == null) {
            return;
        }

        try {

            // Get the stage from the node
            Stage stage = (Stage)anpVehicleList.getScene().getWindow();

            // Open the Vehicle Detail window and pass the vehicle data to the controller
            StageUtils.openModalWindow(
                    "vehicle-detail-view.fxml",
                    stage,    // The owner stage
                    "Edit Vehicle",                        // The window title
                    (VehicleDetailController controller) -> {  // Lambda expression for the controller
                        controller.setTargetVehicle(currentUser, selectedVehicle);
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

        // Get the selected vehicle
        Vehicle selectedVehicle = tvwVehicles.getSelectionModel().getSelectedItem();

        // If the selected vehicle is null, return
        if (selectedVehicle == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deletion Confirmation");
        alert.setHeaderText("Are you sure you want to delete the vehicle " + selectedVehicle.toString() + "?");
        alert.setContentText("Click 'Yes' to confirm the deletion or 'No' to cancel.");

        // Show the dialog and wait for user response
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Vehicle.deleteVehicle(selectedVehicle.getId());

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
        tvwVehicles.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Call your function whenever the selected vehicle changes
            btnEdit.setDisable(newValue == null);
            btnDelete.setDisable(newValue == null);
        });
    }

    public void setValues(User user, Company company) {

        // Set the current user property
        currentUser = user;
        currentCompany = company;

        // Call the refresh table method
        refreshTable();
    }

    private void refreshTable(){

        // Get the list of users from the database
        try {

            vehicleList = FXCollections.observableArrayList(Vehicle.getAllVehiclesByCompanyId(currentCompany.getId()));

            // Set cell value factories to extract values from RideHistory objects
            tbcLicensePlate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLicensePlate()));
            tbcBrand.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getBrand()));
            tbcModel.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getModel()));
            tbcType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
            tbcFuelType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFuelType()));
            tbcStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
            tbcYear.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getYear()));
            tbcMileage.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getMileage()));
            tbcFuelLevel.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFuelLevel()));
            tbcCreatedOn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCreatedOn()));

            // Ensure ongoingRideList is populated with RideHistory objects
            tvwVehicles.setItems(FXCollections.observableArrayList(vehicleList));
        } catch (Exception e) {
            lblErrorMessage.setVisible(true);
            lblErrorMessage.setText(e.getMessage());
        }
    }
}