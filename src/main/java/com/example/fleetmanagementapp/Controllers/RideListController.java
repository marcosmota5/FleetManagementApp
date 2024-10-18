package com.example.fleetmanagementapp.Controllers;

import com.example.fleetmanagementapp.Helpers.StageUtils;
import com.example.fleetmanagementapp.Models.Company;
import com.example.fleetmanagementapp.Models.RideHistory;
import com.example.fleetmanagementapp.Models.User;
import com.example.fleetmanagementapp.Models.Vehicle;
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

public class RideListController {

    @FXML
    private AnchorPane anpRideList;

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
    private TableColumn<RideHistory, String> tbcComments;

    @FXML
    private TableColumn<RideHistory, LocalDate> tbcEndDate;

    @FXML
    private TableColumn<RideHistory, String> tbcEndLocation;

    @FXML
    private TableColumn<RideHistory, Double> tbcFuelConsumed;

    @FXML
    private TableColumn<RideHistory, Double> tbcKilometersDriven;

    @FXML
    private TableColumn<RideHistory, LocalDate> tbcStartDate;

    @FXML
    private TableColumn<RideHistory, String> tbcStartLocation;

    @FXML
    private TableColumn<RideHistory, User> tbcUser;

    @FXML
    private TableColumn<RideHistory, Vehicle> tbcVehicle;

    @FXML
    private TableView<RideHistory> tvwRides;

    private User currentUser;
    private Company currentCompany;
    private ObservableList<RideHistory> rideList;


    @FXML
    void add(ActionEvent event) {
        try {

            lblErrorMessage.setVisible(false);

            RideHistory newRide = new RideHistory();

            // Get the stage from the node
            Stage stage = (Stage)anpRideList.getScene().getWindow();

            // Open the Vehicle Detail window and pass the vehicle data to the controller
            StageUtils.openModalWindow(
                    "ride-detail-view.fxml",
                    stage,    // The owner stage
                    "Add New Ride",                        // The window title
                    (RideDetailController controller) -> {  // Lambda expression for the controller
                        controller.setTargetRide(currentUser, currentCompany, newRide);
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
        RideHistory selectedRide = tvwRides.getSelectionModel().getSelectedItem();

        // If the selected vehicle is null, return
        if (selectedRide == null) {
            return;
        }

        try {

            // Get the stage from the node
            Stage stage = (Stage)anpRideList.getScene().getWindow();

            // Open the Vehicle Detail window and pass the vehicle data to the controller
            StageUtils.openModalWindow(
                    "ride-detail-view.fxml",
                    stage,    // The owner stage
                    "Edit Ride",                        // The window title
                    (RideDetailController controller) -> {  // Lambda expression for the controller
                        controller.setTargetRide(currentUser, currentCompany, selectedRide);
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
        RideHistory selectedRide = tvwRides.getSelectionModel().getSelectedItem();

        // If the selected vehicle is null, return
        if (selectedRide == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deletion Confirmation");
        alert.setHeaderText("Are you sure you want to delete the ride " + selectedRide.toString() + "?");
        alert.setContentText("Click 'Yes' to confirm the deletion or 'No' to cancel.");

        // Show the dialog and wait for user response
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    RideHistory.deleteRide(selectedRide.getId());

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
        tvwRides.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
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

            rideList = FXCollections.observableArrayList(RideHistory.getAllRidesById(currentCompany.getId()));

            // Set cell value factories to extract values from RideHistory objects
            tbcStartDate.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStartDate()));
            tbcStartLocation.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartLocation()));
            tbcEndDate.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEndDate()));
            tbcEndLocation.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEndLocation()));
            tbcVehicle.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getVehicle()));
            tbcKilometersDriven.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getKilometersDriven()));
            tbcFuelConsumed.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFuelConsumed()));
            tbcComments.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getComments()));
            tbcUser.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getUser()));

            // Ensure ongoingRideList is populated with RideHistory objects
            tvwRides.setItems(FXCollections.observableArrayList(rideList));
        } catch (Exception e) {
            lblErrorMessage.setVisible(true);
            lblErrorMessage.setText(e.getMessage());
        }
    }
}