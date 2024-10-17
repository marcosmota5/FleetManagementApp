package com.example.fleetmanagementapp.Controllers;

import com.example.fleetmanagementapp.Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class HomeController {


    @FXML
    private AnchorPane anpHome;

    @FXML
    private BarChart<String, Number> bctFuelEfficiencyByVehicle;

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
    private LineChart<String, Number> lctFuelExpensesOverTime;

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

    }

    private void updateCompanySpecificLists(int companyId){

        // Get the list of users from the database
        try {
            rideHistoryList = FXCollections.observableArrayList(RideHistory.getCompletedRidesById(companyId));
            ongoingRideList = FXCollections.observableArrayList(RideHistory.getOngoingRidesByCompanyId(companyId));
            fuelLogList = FXCollections.observableArrayList(FuelLog.getAllFuelLogByCompanyId(companyId));

            fillFuelEfficiencyChart();

            fillFuelExpensesChart();
        } catch (Exception e) {

        }
    }

    public void setCurrentUser(User user) {

        // Set the current user property
        currentUser = user;

        // Set the user labels
        lblUsername.setText(user.toString());
        lblProfile.setText(user.getProfile().toString());
    }

    public void fillFuelEfficiencyChart() {
        // Series for the bar chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Fuel Efficiency (Km/L)");

        // Calculate fuel efficiency for each vehicle in ride history
        for (RideHistory ride : rideHistoryList) {
            if (ride.getFuelConsumed() > 0 && ride.getKilometersDriven() > 0) {
                double fuelEfficiency = ride.getKilometersDriven() / ride.getFuelConsumed();
                String vehicleName = ride.getVehicle().toString();

                // Add data to the bar chart
                series.getData().add(new XYChart.Data<>(vehicleName, fuelEfficiency));
            }
        }

        // Add series to the bar chart
        bctFuelEfficiencyByVehicle.getData().clear();
        bctFuelEfficiencyByVehicle.getData().add(series);
    }

    public void fillFuelExpensesChart() {
        // Series for the line chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Fuel Expenses Over Time");

        // Create a map to accumulate fuel expenses by month
        Map<String, Double> expensesByMonth = new HashMap<>();

        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM yyyy");

        for (FuelLog log : fuelLogList) {
            String month = log.getFuelDate().format(monthFormatter); // Format date as 'Jan 2024'
            double cost = log.getTotalCost();

            expensesByMonth.put(month, expensesByMonth.getOrDefault(month, 0.0) + cost);
        }

        // Add data to the line chart
        for (Map.Entry<String, Double> entry : expensesByMonth.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        // Add series to the line chart
        lctFuelExpensesOverTime.getData().clear();
        lctFuelExpensesOverTime.getData().add(series);
    }
}
