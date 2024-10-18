package com.example.fleetmanagementapp.Helpers;

import com.example.fleetmanagementapp.Models.RideHistory;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDate;

public class TableUtils {

    public static void fillOngoingRidesTable(ObservableList<RideHistory> ongoingRideList,
                                             TableView<RideHistory> tvwOngoingRides,
                                             TableColumn<RideHistory, LocalDate> tbcStartDate,
                                             TableColumn<RideHistory, String> tbcStartLocation,
                                             TableColumn<RideHistory, String> tbcBrand,
                                             TableColumn<RideHistory, String> tbcModel,
                                             TableColumn<RideHistory, String> tbcLicensePlate,
                                             TableColumn<RideHistory, String> tbcUser){

        // Set cell value factories to extract values from RideHistory objects
        tbcStartDate.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStartDate()));
        tbcStartLocation.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartLocation()));

        // Vehicle-related columns
        tbcBrand.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVehicle().getBrand()));
        tbcModel.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVehicle().getModel()));
        tbcLicensePlate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVehicle().getLicensePlate()));

        // User column, combining first and last name
        tbcUser.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser().getLogin()));

        // Ensure ongoingRideList is populated with RideHistory objects
        tvwOngoingRides.setItems(FXCollections.observableArrayList(ongoingRideList));
    }
}
