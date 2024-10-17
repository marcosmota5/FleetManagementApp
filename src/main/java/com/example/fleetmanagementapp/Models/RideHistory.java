package com.example.fleetmanagementapp.Models;

import com.example.fleetmanagementapp.Data.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RideHistory {

    // Fields
    private int id;
    private String startLocation;
    private String endLocation;
    private LocalDate startDate;
    private LocalDate endDate;
    private double kilometersDriven;
    private double fuelConsumed;
    private String comments;
    private Vehicle vehicle;
    private User user;

    // Getter and setter for id
    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for start location
    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    // Getter and Setter for end location
    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    // Getter and Setter for start date
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    // Getter and Setter for end date
    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    // Getter and Setter for kilometers driven
    public double getKilometersDriven() {
        return kilometersDriven;
    }

    public void setKilometersDriven(double kilometersDriven) {
        this.kilometersDriven = kilometersDriven;
    }

    // Getter and Setter for fuel consumed
    public double getFuelConsumed() {
        return fuelConsumed;
    }

    public void setFuelConsumed(double fuelConsumed) {
        this.fuelConsumed = fuelConsumed;
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
        setId(rs.getInt("ride_history_id"));
        setStartLocation(rs.getString("ride_history_start_location"));
        setEndLocation(rs.getString("ride_history_end_location"));
        setStartDate(rs.getDate("ride_history_start_date").toLocalDate());
        setEndDate(rs.getDate("ride_history_end_date").toLocalDate());
        setKilometersDriven(rs.getDouble("ride_history_kilometers_driven"));
        setFuelConsumed(rs.getDouble("ride_history_fuel_consumed"));
        setComments(rs.getString("ride_history_comments"));

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
    public static List<RideHistory> getOngoingRidesByCompanyId(int companyId) throws Exception {

        // Create the profiles list
        List<RideHistory> rides = new ArrayList<>();

        // Create the connection
        Connection conn = DbConnection.connectToDatabase();

        // If the connection is null, throw an exception
        if (conn == null) {
            throw new SQLException("Failed to connect to the database.");
        }

        // SQL query to get the data
        String query = "SELECT * FROM vw_ride_history WHERE company_id = ? AND ride_history_end_date IS NULL";

        // Create a PreparedStatement
        PreparedStatement pstmt = conn.prepareStatement(query);

        // Set the parameters
        pstmt.setInt(1, companyId);

        // Execute the query
        ResultSet rs = pstmt.executeQuery();

        // Iterate through the result set and create profile objects
        while (rs.next()) {
            // Create a new instance of profile
            RideHistory ride = new RideHistory();

            // Call the method that set the values
            ride.setValuesByResultSet(rs);

            // Add the user to the list
            rides.add(ride);
        }

        // Return the list of users
        return rides;
    }

    // Get ongoing rides by company id
    public static List<RideHistory> getCompletedRidesById(int companyId) throws Exception {

        // Create the profiles list
        List<RideHistory> rides = new ArrayList<>();

        // Create the connection
        Connection conn = DbConnection.connectToDatabase();

        // If the connection is null, throw an exception
        if (conn == null) {
            throw new SQLException("Failed to connect to the database.");
        }

        // SQL query to get the data
        String query = "SELECT * FROM vw_ride_history WHERE company_id = ? AND ride_history_end_date IS NOT NULL";

        // Create a PreparedStatement
        PreparedStatement pstmt = conn.prepareStatement(query);

        // Set the parameters
        pstmt.setInt(1, companyId);

        // Execute the query
        ResultSet rs = pstmt.executeQuery();

        // Iterate through the result set and create profile objects
        while (rs.next()) {
            // Create a new instance of profile
            RideHistory ride = new RideHistory();

            // Call the method that set the values
            ride.setValuesByResultSet(rs);

            // Add the user to the list
            rides.add(ride);
        }

        // Return the list of users
        return rides;
    }

}
