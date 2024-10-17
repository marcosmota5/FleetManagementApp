package com.example.fleetmanagementapp.Models;

import com.example.fleetmanagementapp.Data.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;

public class Vehicle {

    // Fields
    private int id;
    private String licensePlate;
    private String type;
    private String brand;
    private String model;
    private int year;
    private String fuelType;
    private double mileage;
    private double fuelLevel;
    private String status;
    private LocalDate createdOn;
    private Company company;

    // Getter and setter for id
    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for license plate
    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    // Getter and Setter for type
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // Getter and Setter for brand
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    // Getter and Setter for model
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    // Getter and setter for year
    public int getYear() {
        return year;
    }

    private void setYear(int year) {
        this.year = year;
    }

    // Getter and Setter for fuel type
    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    // Getter and Setter for fuel type
    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    // Getter and Setter for fuel fuel level
    public double getFuelLevel() {
        return fuelLevel;
    }

    public void setFuelLevel(double fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    // Getter and Setter for status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Getter and Setter for created on
    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    // Getter and setter for id
    public Company getCompany() {
        return company;
    }

    private void setCompany(Company company) { this.company = company; }

    // Set the values of the current instance using a received result set
    public void setValuesByResultSet(ResultSet rs) throws SQLException {
        setId(rs.getInt("vehicle_id"));
        setLicensePlate(rs.getString("vehicle_license_plate"));
        setType(rs.getString("vehicle_type"));
        setBrand(rs.getString("vehicle_brand"));
        setModel(rs.getString("vehicle_model"));
        setYear(rs.getInt("vehicle_year"));
        setFuelType(rs.getString("vehicle_fuel_type"));
        setMileage(rs.getDouble("vehicle_mileage"));
        setFuelLevel(rs.getDouble("vehicle_fuel_level"));
        setStatus(rs.getString("vehicle_status"));
        setCreatedOn(rs.getDate("vehicle_created_on").toLocalDate());

        // Create a new instance of company
        Company company = new Company();

        // Call the method that set the values
        company.setValuesByResultSet(rs);

        // Set the company
        setCompany(company);
    }

    // Get the profile data using its id
    public static Vehicle getVehicleById(int id) throws Exception {

        // Create the connection
        Connection conn = DbConnection.connectToDatabase();

        // If the connection is null, throw an exception
        if (conn == null) {
            throw new SQLException("Failed to connect to the database.");
        }

        // Create the select query by using question marks for the parameters
        String query = "SELECT * FROM vw_vehicles WHERE vehicle_id = ?";

        // Use PreparedStatement to prevent SQL injection and bind parameters
        PreparedStatement pstmt = conn.prepareStatement(query);

        // Set the parameters
        pstmt.setInt(1, id);

        // Execute the query
        ResultSet rs = pstmt.executeQuery();

        // If there are values, return the profile
        if (rs.next()) {
            // Create a new instance of profile
            Vehicle vehicle = new Vehicle();

            // Call the method that set the values
            vehicle.setValuesByResultSet(rs);

            // Return the user
            return vehicle;
        }

        // Return null as the profile was not found
        return null;
    }


    @Override
    public String toString() {
        // Display the user information in the ListView (for example, firstName and lastName)
        return brand + " " + model + " (" + licensePlate + ")";
    }
}