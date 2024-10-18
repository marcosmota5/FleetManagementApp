package com.example.fleetmanagementapp.Models;

import com.example.fleetmanagementapp.Data.DbConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Profile {

    // Fields
    private int id;
    private String name;
    private String description;
    private int priority;
    private LocalDate createdOn;

    // Getter and setter for id
    public int getId() { return id; }

    private void setId(int id) { this.id = id; }

    // Getter and Setter for name
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    // Getter and Setter for description
    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    // Getter and setter for priority
    public int getPriority() { return priority; }

    private void setPriority(int priority) { this.priority = priority; }

    // Getter and Setter for created on
    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    // Set the values of the current instance using a received result set
    public void setValuesByResultSet(ResultSet rs) throws SQLException {
        setId(rs.getInt("profile_id"));
        setName(rs.getString("profile_name"));
        setDescription(rs.getString("profile_description"));
        setPriority(rs.getInt("profile_priority"));
        setCreatedOn(rs.getDate("profile_created_on").toLocalDate());
    }

    // Get the profile data using its id
    public static Profile getProfileById(int id) throws Exception {

        // Create the connection
        Connection conn = DbConnection.connectToDatabase();

        // If the connection is null, throw an exception
        if (conn == null) {
            throw new SQLException("Failed to connect to the database.");
        }

        // Create the select query by using question marks for the parameters
        String query = "SELECT * FROM vw_profiles WHERE profile_id = ?";

        // Use PreparedStatement to prevent SQL injection and bind parameters
        PreparedStatement pstmt = conn.prepareStatement(query);

        // Set the parameters
        pstmt.setInt(1, id);

        // Execute the query
        ResultSet rs = pstmt.executeQuery();

        // If there are values, return the profile
        if (rs.next()) {
            // Create a new instance of profile
            Profile profile = new Profile();

            // Call the method that set the values
            profile.setValuesByResultSet(rs);

            // Return the user
            return profile;
        }

        // Return null as the profile was not found
        return null;
    }

    // Get all profiles
    public static List<Profile> getLowerPriorityProfiles(int priority) throws Exception {

        // Create the profiles list
        List<Profile> profiles = new ArrayList<>();

        // Create the connection
        Connection conn = DbConnection.connectToDatabase();

        // If the connection is null, throw an exception
        if (conn == null) {
            throw new SQLException("Failed to connect to the database.");
        }

        // SQL query to get profiles
        String query = "SELECT * FROM vw_profiles WHERE profile_priority > ?";

        // Create a PreparedStatement
        PreparedStatement pstmt = conn.prepareStatement(query);

        // Set the parameters
        pstmt.setInt(1, priority);

        // Execute the query
        ResultSet rs = pstmt.executeQuery();

        // Iterate through the result set and create profile objects
        while (rs.next()) {
            // Create a new instance of profile
            Profile profile = new Profile();

            // Call the method that set the values
            profile.setValuesByResultSet(rs);

            // Add the user to the list
            profiles.add(profile);
        }

        // Return the list of users
        return profiles;
    }

    // Get all profiles
    public static List<Profile> getAllProfiles() throws Exception {

        // Create the profiles list
        List<Profile> profiles = new ArrayList<>();

        // Create the connection
        Connection conn = DbConnection.connectToDatabase();

        // If the connection is null, throw an exception
        if (conn == null) {
            throw new SQLException("Failed to connect to the database.");
        }

        // SQL query to get all users
        String query = "SELECT * FROM vw_profiles WHERE profile_id > 0";

        // Create a PreparedStatement
        PreparedStatement pstmt = conn.prepareStatement(query);

        // Execute the query
        ResultSet rs = pstmt.executeQuery();

        // Iterate through the result set and create profile objects
        while (rs.next()) {
            // Create a new instance of profile
            Profile profile = new Profile();

            // Call the method that set the values
            profile.setValuesByResultSet(rs);

            // Add the user to the list
            profiles.add(profile);
        }

        // Return the list of users
        return profiles;
    }

    // Get all current priorities
    public static List<Integer> getPrioritiesByPriorityAndCondition(int priority, boolean isForNewItem) throws Exception {

        // Create the profiles list
        List<Integer> priorities = new ArrayList<>();

        // Create the connection
        Connection conn = DbConnection.connectToDatabase();

        // If the connection is null, throw an exception
        if (conn == null) {
            throw new SQLException("Failed to connect to the database.");
        }

        // SQL query to get the priorities
        String query = "(" +
                "SELECT profile_priority " +
                "FROM vw_profiles " +
                "WHERE profile_priority " + (isForNewItem ? ">" : ">=") + " ? " +
                "ORDER BY profile_priority ASC" +
                ")" +
                "UNION ALL" +
                "(" +
                "SELECT IFNULL(" +
                "(SELECT MAX(profile_priority) + 1 " +
                "FROM vw_profiles " +
                "WHERE profile_priority " + (isForNewItem ? ">" : ">=") + " ? )," +
                "(SELECT MAX(profile_priority) + 1 " +
                "FROM vw_profiles)" +
                ") AS profile_priority" +
                ")" +
                "ORDER BY profile_priority ASC";

        // Create a PreparedStatement
        PreparedStatement pstmt = conn.prepareStatement(query);

        // Set the parameters
        pstmt.setInt(1, priority);
        pstmt.setInt(2, priority);

        // Execute the query
        ResultSet rs = pstmt.executeQuery();

        // Iterate through the result set and create priority objects
        while (rs.next()) {
            // Add the user to the list
            priorities.add(rs.getInt("profile_priority"));
        }

        // Return the list of priorities
        return priorities;
    }

    // Save the profile in the database, if the profile id is 0, insert a new one, if it's not, update an existing one
    public static int saveProfile(int profileId, String name, int priority, String description) throws Exception {

        // Create the connection
        Connection conn = DbConnection.connectToDatabase();

        // If the connection is null, throw an exception
        if (conn == null) {
            throw new SQLException("Failed to connect to the database.");
        }

        // Create the select query by using question marks for the parameters
        String query = "{ CALL sp_upsert_profile(?, ?, ?, ?, ?) }";

        // Use PreparedStatement to prevent SQL injection and bind parameters
        CallableStatement cstmt = conn.prepareCall(query);

        // Bind parameters to the stored procedure
        cstmt.setInt(1, profileId);
        cstmt.setString(2, name);
        cstmt.setString(3, description);
        cstmt.setInt(4, priority);

        // Register the OUT parameter (user ID)
        cstmt.registerOutParameter(5, java.sql.Types.INTEGER);

        // Execute the query
        ResultSet rs = cstmt.executeQuery();

        // Retrieve and return the user ID from the OUT parameter
        return cstmt.getInt(5);
    }

    // Delete the profile in the database
    public static void deleteProfile(int profileId) throws Exception {

        // Create the connection
        Connection conn = DbConnection.connectToDatabase();

        // If the connection is null, throw an exception
        if (conn == null) {
            throw new SQLException("Failed to connect to the database.");
        }

        // Create the select query by using question marks for the parameters
        String query = "{ CALL sp_delete_profile(?) }";

        // Use PreparedStatement to prevent SQL injection and bind parameters
        CallableStatement cstmt = conn.prepareCall(query);

        // Bind parameters to the stored procedure
        cstmt.setInt(1, profileId);

        // Execute the query
        ResultSet rs = cstmt.executeQuery();
    }

    @Override
    public String toString() {
        // Display the user information in the ListView (for example, firstName and lastName)
        return name;
    }
}
