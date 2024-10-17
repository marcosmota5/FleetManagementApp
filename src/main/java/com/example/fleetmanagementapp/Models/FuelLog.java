package com.example.fleetmanagementapp.Models;

import com.example.fleetmanagementapp.Data.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FuelLog {

    // Fields
    private int id;
    private LocalDate fuelDate;
    private double litersFilled;
    private double fuelPricePerLiter;
    private double totalCost;
    private String comments;
    private Vehicle vehicle;
    private User user;

    // Getter and setter for id
    public int getId() { return id; }

    private void setId(int id) { this.id = id; }

    // Getter and Setter for fuel date
    public LocalDate getFuelDate() { return fuelDate; }

    public void setFuelDate(LocalDate fuelDate) { this.fuelDate = fuelDate; }

    // Getter and Setter for liters filled
    public double getLitersFilled() {
        return litersFilled;
    }

    public void setLitersFilled(double litersFilled) {
        this.litersFilled = litersFilled;
    }

    // Getter and Setter for fuel consumed
    public double getFuelPricePerLiter() {
        return fuelPricePerLiter;
    }

    public void setFuelPricePerLiter(double fuelPricePerLiter) {
        this.fuelPricePerLiter = fuelPricePerLiter;
    }

    // Getter and Setter for fuel consumed
    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    // Getter and Setter for comments
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    // Getter and Setter for vehicle
    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    // Getter and Setter for user
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Set the values of the current instance using a received result set
    public void setValuesByResultSet(ResultSet rs) throws SQLException {
        setId(rs.getInt("fuel_log_id"));
        setFuelDate(rs.getDate("fuel_log_fuel_date").toLocalDate());
        setLitersFilled(rs.getDouble("fuel_log_liters_filled"));
        setFuelPricePerLiter(rs.getDouble("fuel_log_fuel_price_per_liter"));
        setTotalCost(rs.getDouble("fuel_log_total_cost"));
        setComments(rs.getString("fuel_log_comments"));

        // Create a new instance of vehicle
        Vehicle vehicle = new Vehicle();

        // Call the method that set the values
        vehicle.setValuesByResultSet(rs);

        // Set the vehicle
        setVehicle(vehicle);

        // Create a new instance of user
        User user = new User();

        // Call the method that set the values
        user.setValuesByResultSet(rs);

        // Set the user
        setUser(user);
    }


    // Get ongoing rides by company id
    public static List<FuelLog> getAllFuelLogByCompanyId(int companyId) throws Exception {

        // Create the profiles list
        List<FuelLog> fuelLog = new ArrayList<>();

        // Create the connection
        Connection conn = DbConnection.connectToDatabase();

        // If the connection is null, throw an exception
        if (conn == null) {
            throw new SQLException("Failed to connect to the database.");
        }

        // SQL query to get the data
        String query = "SELECT * FROM vw_fuel_log WHERE company_id = ?";

        // Create a PreparedStatement
        PreparedStatement pstmt = conn.prepareStatement(query);

        // Set the parameters
        pstmt.setInt(1, companyId);

        // Execute the query
        ResultSet rs = pstmt.executeQuery();

        // Iterate through the result set and create profile objects
        while (rs.next()) {
            // Create a new instance of profile
            FuelLog fuel = new FuelLog();

            // Call the method that set the values
            fuel.setValuesByResultSet(rs);

            // Add the user to the list
            fuelLog.add(fuel);
        }

        // Return the list of users
        return fuelLog;
    }

}
