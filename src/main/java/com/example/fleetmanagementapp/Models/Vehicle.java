package com.example.fleetmanagementapp.Models;

import com.example.fleetmanagementapp.Data.DbConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public void setCompany(Company company) { this.company = company; }

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

    // Get all profiles
    public static List<Vehicle> getAllVehiclesByCompanyId(int companyId) throws Exception {

        // Create the vehicles list
        List<Vehicle> vehicles = new ArrayList<>();

        // Create the connection
        Connection conn = DbConnection.connectToDatabase();

        // If the connection is null, throw an exception
        if (conn == null) {
            throw new SQLException("Failed to connect to the database.");
        }

        // SQL query to get all users
        String query = "SELECT * FROM vw_vehicles WHERE company_id = ?";

        // Create a PreparedStatement
        PreparedStatement pstmt = conn.prepareStatement(query);

        // Set the parameters
        pstmt.setInt(1, companyId);

        // Execute the query
        ResultSet rs = pstmt.executeQuery();

        // Iterate through the result set and create profile objects
        while (rs.next()) {
            // Create a new instance of profile
            Vehicle vehicle = new Vehicle();

            // Call the method that set the values
            vehicle.setValuesByResultSet(rs);

            // Add the user to the list
            vehicles.add(vehicle);
        }

        // Return the list of users
        return vehicles;
    }

    // Save the profile in the database, if the profile id is 0, insert a new one, if it's not, update an existing one
    public static int saveVehicle(int vehicleId, String licensePlate, String type, String brand, String model, int year,
                                  String fuelType, double mileage, double fuelLevel, String status, int companyId) throws Exception {

        // Create the connection
        Connection conn = DbConnection.connectToDatabase();

        // If the connection is null, throw an exception
        if (conn == null) {
            throw new SQLException("Failed to connect to the database.");
        }

        // Create the select query by using question marks for the parameters
        String query = "{ CALL sp_upsert_vehicle(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";

        // Use PreparedStatement to prevent SQL injection and bind parameters
        CallableStatement cstmt = conn.prepareCall(query);

        // Bind parameters to the stored procedure
        cstmt.setInt(1, vehicleId);
        cstmt.setString(2, licensePlate);
        cstmt.setString(3, type);
        cstmt.setString(4, brand);
        cstmt.setString(5, model);
        cstmt.setInt(6, year);
        cstmt.setString(7, fuelType);
        cstmt.setDouble(8, mileage);
        cstmt.setDouble(9, fuelLevel);
        cstmt.setString(10, status);
        cstmt.setInt(11, companyId);

        // Register the OUT parameter (user ID)
        cstmt.registerOutParameter(12, java.sql.Types.INTEGER);

        // Execute the query
        ResultSet rs = cstmt.executeQuery();

        // Retrieve and return the user ID from the OUT parameter
        return cstmt.getInt(12);
    }

    // Delete the profile in the database
    public static void deleteVehicle(int vehicleId) throws Exception {

        // Create the connection
        Connection conn = DbConnection.connectToDatabase();

        // If the connection is null, throw an exception
        if (conn == null) {
            throw new SQLException("Failed to connect to the database.");
        }

        // Create the select query by using question marks for the parameters
        String query = "{ CALL sp_delete_vehicle(?) }";

        // Use PreparedStatement to prevent SQL injection and bind parameters
        CallableStatement cstmt = conn.prepareCall(query);

        // Bind parameters to the stored procedure
        cstmt.setInt(1, vehicleId);

        // Execute the query
        ResultSet rs = cstmt.executeQuery();
    }

    @Override
    public String toString() {
        // Display the user information in the ListView (for example, firstName and lastName)
        return brand + " " + model + " (" + licensePlate + ")";
    }
}