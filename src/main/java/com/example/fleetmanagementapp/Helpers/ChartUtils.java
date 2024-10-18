package com.example.fleetmanagementapp.Helpers;

import com.example.fleetmanagementapp.Models.FuelLog;
import com.example.fleetmanagementapp.Models.RideHistory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.time.format.DateTimeFormatter;
import java.util.*;

public class ChartUtils {

    // List of colors for bars
    private static final List<String> colors = Arrays.asList(
            // Blues
            "#3498db",  // Light Blue
            "#2980b9",  // Dark Blue
            "#5DADE2",  // Sky Blue
            "#1F618D",  // Navy Blue

            // Greens
            "#2ecc71",  // Green
            "#27ae60",  // Dark Green
            "#58D68D",  // Light Green
            "#145A32",  // Forest Green

            // Reds
            "#e74c3c",  // Red
            "#c0392b",  // Dark Red
            "#F1948A",  // Light Red
            "#922B21",  // Deep Red

            // Oranges/Yellows
            "#f39c12",  // Orange
            "#e67e22",  // Dark Orange
            "#F5B041",  // Light Orange
            "#9C640C",  // Brownish Yellow

            // Grays
            "#7f8c8d",  // Gray
            "#95a5a6",  // Light Gray
            "#34495e",  // Dark Gray
            "#BDC3C7"   // Silver
    );

    public static void fillFuelEfficiencyChart(ObservableList<RideHistory> list, BarChart<String, Number> chart,
                                               CategoryAxis category) {

        // Create a set to store unique vehicle names (categories)
        Set<String> vehicleNames = new HashSet<>();

        // Series for the bar chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        int colorIndex = 0;

        // Calculate fuel efficiency for each vehicle in ride history
        for (RideHistory ride : list) {
            if (ride.getFuelConsumed() > 0 && ride.getKilometersDriven() > 0) {
                double fuelEfficiency = ride.getKilometersDriven() / ride.getFuelConsumed();
                String vehicleName = ride.getVehicle().getModel();

                // Add the vehicle name to the set (categories)
                vehicleNames.add(vehicleName);

                // Create the data point
                XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(vehicleName, fuelEfficiency);

                // Get the current color
                String color = colors.get(colorIndex % colors.size());

                // Add data label
                dataPoint.nodeProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {

                        // Add hover effect programmatically
                        newValue.setOnMouseEntered(event -> newValue.setStyle("-fx-bar-fill: #2C3E50;"));  // Change to red on hover
                        newValue.setOnMouseExited(event -> newValue.setStyle("-fx-bar-fill: " + color + ";"));   // Revert to default on exit

                        Label dataLabel = new Label(String.format("%.2f", fuelEfficiency));
                        dataLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: black;");
                        StackPane stackPane = (StackPane) newValue;
                        stackPane.getChildren().add(dataLabel);  // Attach label to bar
                        StackPane.setAlignment(dataLabel, Pos.TOP_CENTER);  // Position label at top
                        StackPane.setMargin(dataLabel, new Insets(-20, 0, 0, 0));
                        newValue.setStyle("-fx-bar-fill: " + color + ";");
                    }
                });

                // Increment the color index
                colorIndex++;

                // Add data to the bar chart
                series.getData().add(dataPoint);
            }
        }

        // Set categories on the CategoryAxis
        category.setCategories(FXCollections.observableArrayList(vehicleNames));

        // Add series to the bar chart
        chart.getData().clear();
        chart.getData().add(series);

        // Hide the legend
        chart.setLegendVisible(false);
    }


    public static void fillFuelExpensesChart(ObservableList<FuelLog> list, LineChart<String, Number> chart,
                                             CategoryAxis category) {

        // Create a set to store unique months
        Set<String> months = new HashSet<>();

        // Series for the line chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // Create a map to accumulate fuel expenses by month
        Map<String, Double> expensesByMonth = new HashMap<>();
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM yyyy");

        for (FuelLog log : list) {
            String month = log.getFuelDate().format(monthFormatter); // Format date as 'Jan 2024'
            double cost = log.getTotalCost();

            // Add the month name to the set (categories)
            months.add(month);

            // Accumulate fuel expenses by month
            expensesByMonth.put(month, expensesByMonth.getOrDefault(month, 0.0) + cost);
        }

        // Add data to the line chart
        for (Map.Entry<String, Double> entry : expensesByMonth.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        // Add data points to the line chart and add labels
        for (Map.Entry<String, Double> entry : expensesByMonth.entrySet()) {
            XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(entry.getKey(), entry.getValue());

            // Add data label at the point of the value
            dataPoint.nodeProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    Label dataLabel = new Label(String.format("%.2f", entry.getValue()));
                    dataLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: black;");
                    StackPane stackPane = (StackPane) newValue;
                    stackPane.getChildren().add(dataLabel);  // Attach the label to the point
                    StackPane.setAlignment(dataLabel, Pos.BOTTOM_CENTER);  // Align it at the top
                    StackPane.setMargin(dataLabel, new Insets(0, 0, 0, 0));  // Move it above the point

                    // Optional hover effect on the data point (change point's color)
                    stackPane.setOnMouseEntered(event -> stackPane.setStyle("-fx-background-color: #2C3E50, white;"));  // Dark blue on hover
                    stackPane.setOnMouseExited(event -> stackPane.setStyle("-fx-background-color: #3498db, white;"));  // Reset to default color
                }
            });

            series.getData().add(dataPoint);  // Add the data point to the series
        }

        // Set categories on the CategoryAxis
        category.setCategories(FXCollections.observableArrayList(months));

        // Change the line color to blue using CSS
        series.nodeProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                newValue.setStyle("-fx-stroke: #3498db; -fx-stroke-width: 1px;");  // Blue color with 2px line width
            }
        });

        // Add series to the line chart
        chart.getData().clear();
        chart.getData().add(series);

        // Hide the legend
        chart.setLegendVisible(false);
    }
}
