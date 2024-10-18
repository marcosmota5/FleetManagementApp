package com.example.fleetmanagementapp.Models;

import com.example.fleetmanagementapp.Data.DbConnection;

import java.sql.*;
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
    private Double kilometersDriven;
    private Double fuelConsumed;
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
    public Double getKilometersDriven() {
        return kilometersDriven;
    }

    public void setKilometersDriven(Double kilometersDriven) {
        this.kilometersDriven = kilometersDriven;
    }

    // Getter and Setter for fuel consumed
    public Double getFuelConsumed() {
        return fuelConsumed;
    }

    public void setFuelConsumed(Double fuelConsumed) {
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
        setEndDate(rs.getDate("ride_history_end_date") == null ? null : rs.getDate("ride_history_end_date").toLocalDate());
        setKilometersDriven(rs.getObject("ride_history_kilometers_driven") == null ? null : rs.getDouble("ride_history_kilometers_driven"));
        setFuelConsumed(rs.getObject("ride_history_fuel_consumed") == null ? null : rs.getDouble("ride_history_fuel_consumed"));
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

    // Get ongoing rides by company id
    public static List<RideHistory> getAllRidesById(int companyId) throws Exception {

        // Create the profiles list
        List<RideHistory> rides = new ArrayList<>();

        // Create the connection
        Connection conn = DbConnection.connectToDatabase();

        // If the connection is null, throw an exception
        if (conn == null) {
            throw new SQLException("Failed to connect to the database.");
        }

        // SQL query to get the data
        String query = "SELECT * FROM vw_ride_history WHERE company_id = ?";

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

    // Save the profile in the database, if the ride id is 0, insert a new one, if it's not, update an existing one
    public static int saveRide(int rideId, String startLocation, String endLocation, LocalDate startDate, LocalDate endDate,
                               Double kilometersDriven, Double fuelConsumed, String comments, int vehicleId, int userId)
            throws Exception {

        // Create the connection
        Connection conn = DbConnection.connectToDatabase();

        // If the connection is null, throw an exception
        if (conn == null) {
            throw new SQLException("Failed to connect to the database.");
        }

        // Create the select query by using question marks for the parameters
        String query = "{ CALL sp_upsert_ride_history(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";

        // Use PreparedStatement to prevent SQL injection and bind parameters
        CallableStatement cstmt = conn.prepareCall(query);

        // Convert LocalDate to java.sql.Date
        java.sql.Date sqlStartDate = java.sql.Date.valueOf(startDate);

        // Handle nullable fields (convert null if needed)
        java.sql.Date sqlEndDate = (endDate != null) ? java.sql.Date.valueOf(endDate) : null;
        String safeEndLocation = (endLocation != null && !endLocation.isEmpty()) ? endLocation : null;
        Double safeKilometersDriven = (kilometersDriven != null) ? kilometersDriven : null;
        Double safeFuelConsumed = (fuelConsumed != null) ? fuelConsumed : null;
        String safeComments = (comments != null && !comments.isEmpty()) ? comments : null;

        // Bind parameters to the stored procedure
        cstmt.setInt(1, rideId);
        cstmt.setInt(2, vehicleId);
        cstmt.setInt(3, userId);
        cstmt.setString(4, startLocation);
        cstmt.setString(5, safeEndLocation);
        cstmt.setDate(6, sqlStartDate);
        cstmt.setDate(7, sqlEndDate);
        if (safeKilometersDriven != null) {
            cstmt.setDouble(8, safeKilometersDriven);
        } else {
            cstmt.setNull(8, java.sql.Types.DOUBLE); // Set to NULL if it's empty
        }
        if (safeFuelConsumed != null) {
            cstmt.setDouble(9, safeFuelConsumed);
        } else {
            cstmt.setNull(9, java.sql.Types.DOUBLE); // Set to NULL if it's empty
        }
        cstmt.setString(10, safeComments);

        // Register the OUT parameter (user ID)
        cstmt.registerOutParameter(11, java.sql.Types.INTEGER);

        // Execute the query
        ResultSet rs = cstmt.executeQuery();

        // Retrieve and return the user ID from the OUT parameter
        return cstmt.getInt(11);
    }

    // Delete the ride in the database
    public static void deleteRide(int rideId) throws Exception {

        // Create the connection
        Connection conn = DbConnection.connectToDatabase();

        // If the connection is null, throw an exception
        if (conn == null) {
            throw new SQLException("Failed to connect to the database.");
        }

        // Create the select query by using question marks for the parameters
        String query = "{ CALL sp_delete_ride_history(?) }";

        // Use PreparedStatement to prevent SQL injection and bind parameters
        CallableStatement cstmt = conn.prepareCall(query);

        // Bind parameters to the stored procedure
        cstmt.setInt(1, rideId);

        // Execute the query
        ResultSet rs = cstmt.executeQuery();
    }

}
