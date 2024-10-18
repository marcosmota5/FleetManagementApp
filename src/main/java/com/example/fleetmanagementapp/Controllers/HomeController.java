package com.example.fleetmanagementapp.Controllers;

import com.example.fleetmanagementapp.HelloApplication;
import com.example.fleetmanagementapp.Helpers.ChartUtils;
import com.example.fleetmanagementapp.Helpers.SceneSwitcher;
import com.example.fleetmanagementapp.Helpers.StageUtils;
import com.example.fleetmanagementapp.Helpers.TableUtils;
import com.example.fleetmanagementapp.Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;

public class HomeController {


    @FXML
    private AnchorPane anpHome;

    @FXML
    private BarChart<String, Number> bctFuelEfficiencyByVehicle;

    @FXML
    private LineChart<String, Number> lctFuelExpensesOverTime;

    @FXML
    private CategoryAxis caxFuelEfficiencyCategory;

    @FXML
    private CategoryAxis caxFuelExpensesCategory;

    @FXML
    private NumberAxis naxFuelEfficiencyNumber;

    @FXML
    private NumberAxis naxFuelExpensesNumber;

    @FXML
    private Button btnLogout;

    @FXML
    private ComboBox<Company> cmbCompanies;

    @FXML
    private GridPane grdMain;

    @FXML
    private Label lblCurrentCompany;

    @FXML
    private Label lblOngoingRides;

    @FXML
    private Label lblProfile;

    @FXML
    private Label lblUsername;

    @FXML
    private MenuBar mbrMenu;

    @FXML
    private MenuItem mniAbout;

    @FXML
    private MenuItem mniAddNewFuelOccurrence;

    @FXML
    private MenuItem mniAddNewProfile;

    @FXML
    private MenuItem mniAddNewRide;

    @FXML
    private MenuItem mniAddNewUser;

    @FXML
    private MenuItem mniAddNewVehicle;

    @FXML
    private Menu mniHelp;

    @FXML
    private MenuItem mniViewFuelLog;

    @FXML
    private MenuItem mniViewProfiles;

    @FXML
    private MenuItem mniViewRides;

    @FXML
    private MenuItem mniViewUsers;

    @FXML
    private MenuItem mniViewVehicles;

    @FXML
    private Menu mnuFuel;

    @FXML
    private Menu mnuProfiles;

    @FXML
    private Menu mnuRides;

    @FXML
    private Menu mnuUsers;

    @FXML
    private Menu mnuVehicles;

    @FXML
    private Separator sprSeparator;

    @FXML
    private ToolBar tbrToolBar;

    @FXML
    private TableView<RideHistory> tvwOngoingRides;

    @FXML
    private TableColumn<RideHistory, String> tbcBrand;

    @FXML
    private TableColumn<RideHistory, String> tbcLicensePlate;

    @FXML
    private TableColumn<RideHistory, String> tbcModel;

    @FXML
    private TableColumn<RideHistory, LocalDate> tbcStartDate;

    @FXML
    private TableColumn<RideHistory, String> tbcStartLocation;

    @FXML
    private TableColumn<RideHistory, String> tbcUser;

    @FXML
    private VBox vbxOngoingRides;

    private User currentUser;
    private ObservableList<Company> companyList;
    private ObservableList<RideHistory> ongoingRideList;
    private ObservableList<RideHistory> rideHistoryList;
    private ObservableList<FuelLog> fuelLogList;

    @FXML
    public void initialize() {

        // Get the list of users from the database
        try {
            companyList = FXCollections.observableArrayList(Company.getAllCompanies());

            // Set the items in the combobox
            cmbCompanies.setItems(companyList);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Add a listener to the ComboBox to listen for changes in the selected value
        cmbCompanies.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Call your function whenever the selected company changes
            if (newValue != null) {
                updateCompanySpecificLists(newValue.getId());
            };
        });

        // Check if the ComboBox has items before setting the selection
        if (!cmbCompanies.getItems().isEmpty()) {
            // Set the first item of the ComboBox as the selected item
            cmbCompanies.getSelectionModel().select(0);
        }

        tvwOngoingRides.setRowFactory(tv -> {
            TableRow<RideHistory> row = new TableRow<>();

            // Set the event handler for mouse click
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    // Get the selected item (the RideHistory for the clicked row)
                    RideHistory selectedRide = row.getItem();

                    // Call your method with the selected item
                    try {
                        // Open the Profile Detail window and pass the vehicle data to the controller
                        StageUtils.openModalWindow(
                                "ride-detail-view.fxml",
                                HelloApplication.getPrimaryStage(),    // The owner stage (main window)
                                "Edit Ride",                        // The window title
                                (RideDetailController controller) -> {  // Lambda expression for the controller
                                    controller.setTargetRide(currentUser, cmbCompanies.getSelectionModel().getSelectedItem(), selectedRide);
                                }
                        );

                        // Refresh the charts and tables
                        updateCompanySpecificLists(cmbCompanies.getSelectionModel().getSelectedItem().getId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            return row;
        });
    }

    private void updateCompanySpecificLists(int companyId){

        // Get the list of users from the database
        try {

            // Get the lists from the database
            rideHistoryList = FXCollections.observableArrayList(RideHistory.getCompletedRidesById(companyId));
            ongoingRideList = FXCollections.observableArrayList(RideHistory.getOngoingRidesByCompanyId(companyId));
            fuelLogList = FXCollections.observableArrayList(FuelLog.getAllFuelLogByCompanyId(companyId));

            // Call the helper method to fill charts
            ChartUtils.fillFuelEfficiencyChart(rideHistoryList, bctFuelEfficiencyByVehicle, caxFuelEfficiencyCategory);
            ChartUtils.fillFuelExpensesChart(fuelLogList, lctFuelExpensesOverTime, caxFuelExpensesCategory);

            // Call the helper method to fill the table
            TableUtils.fillOngoingRidesTable(ongoingRideList, tvwOngoingRides, tbcStartDate, tbcStartLocation,
                    tbcBrand, tbcModel, tbcLicensePlate, tbcUser);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void setCurrentUser(User user) {

        // Set the current user property
        currentUser = user;

        // Set the user labels
        lblUsername.setText(user.toString());
        lblProfile.setText(user.getProfile().toString());
    }

    public void addNewProfile(ActionEvent event) throws IOException {
        try {

            Profile newProfile = new Profile();

            // Open the Profile Detail window and pass the profile data to the controller
            StageUtils.openModalWindow(
                    "profile-detail-view.fxml",
                    HelloApplication.getPrimaryStage(),    // The owner stage (main window)
                    "Add New Profile",                        // The window title
                    (ProfileDetailController controller) -> {  // Lambda expression for the controller
                        controller.setTargetProfile(currentUser, newProfile);
                    }
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listProfiles(ActionEvent event) throws IOException {
        try {
            // Open the profile list
            StageUtils.openModalWindow(
                    "profile-list-view.fxml",
                    HelloApplication.getPrimaryStage(),    // The owner stage (main window)
                    "View Profiles",                        // The window title
                    (ProfileListController controller) -> {  // Lambda expression for the controller
                        controller.setValues(currentUser);
                    }
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addNewVehicle(ActionEvent event) throws IOException {
        try {

            Vehicle newVehicle = new Vehicle();
            newVehicle.setCompany(cmbCompanies.getSelectionModel().getSelectedItem());

            // Open the Profile Detail window and pass the vehicle data to the controller
            StageUtils.openModalWindow(
                    "vehicle-detail-view.fxml",
                    HelloApplication.getPrimaryStage(),    // The owner stage (main window)
                    "Add New Vehicle",                        // The window title
                    (VehicleDetailController controller) -> {  // Lambda expression for the controller
                        controller.setTargetVehicle(currentUser, newVehicle);
                    }
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listVehicles(ActionEvent event) throws IOException {
        try {
            // Open the profile list
            StageUtils.openModalWindow(
                    "vehicle-list-view.fxml",
                    HelloApplication.getPrimaryStage(),    // The owner stage (main window)
                    "View Vehicles",                        // The window title
                    (VehicleListController controller) -> {  // Lambda expression for the controller
                        controller.setValues(currentUser, cmbCompanies.getSelectionModel().getSelectedItem());
                    }
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addNewRide(ActionEvent event) throws IOException {
        try {

            RideHistory newRide = new RideHistory();

            // Open the Profile Detail window and pass the vehicle data to the controller
            StageUtils.openModalWindow(
                    "ride-detail-view.fxml",
                    HelloApplication.getPrimaryStage(),    // The owner stage (main window)
                    "Add New Ride",                        // The window title
                    (RideDetailController controller) -> {  // Lambda expression for the controller
                        controller.setTargetRide(currentUser, cmbCompanies.getSelectionModel().getSelectedItem(), newRide);
                    }
            );

            // Refresh the charts and tables
            updateCompanySpecificLists(cmbCompanies.getSelectionModel().getSelectedItem().getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listRides(ActionEvent event) throws IOException {
        try {
            // Open the profile list
            StageUtils.openModalWindow(
                    "ride-list-view.fxml",
                    HelloApplication.getPrimaryStage(),    // The owner stage (main window)
                    "View Rides",                        // The window title
                    (RideListController controller) -> {  // Lambda expression for the controller
                        controller.setValues(currentUser, cmbCompanies.getSelectionModel().getSelectedItem());
                    }
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("Are you sure you want to log out?");
        alert.setContentText("Click 'Yes' to log out or 'No' to cancel.");

        // Show the dialog and wait for user response
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // User clicked on ok, so switch the scene
                try {

                    // The code below has a problem because the window getting smaller is cutting the login anchor pane
                    // Get the stage from anchor
                    //Stage stage = (Stage)anpHome.getScene().getWindow();

                    // Resize the stage
                    //stage.setWidth(853);
                    //stage.setHeight(531);


                    // Go to the login page
                    SceneSwitcher.switchScene(anpHome, "login-view.fxml");
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        });

    }

    public void refresh(ActionEvent event) {
        updateCompanySpecificLists(cmbCompanies.getSelectionModel().getSelectedItem().getId());
    }

    public void about(ActionEvent event) {
        // Create the alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Fleet Management App");
        alert.setContentText("Developed by Marcos Mota <marcosmota5@hotmail.com>");

        // Show the alert and wait for user to click "OK"
        alert.showAndWait();

    }
}
