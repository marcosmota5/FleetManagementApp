package com.example.fleetmanagementapp.Models;

import com.example.fleetmanagementapp.Data.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    // Get the profile data using its id
    public static Profile getProfileById(int id) throws Exception {

        // Create the connection
        Connection conn = DbConnection.connectToDatabase();

        // If the connection is null, throw an exception
        if (conn == null) {
            throw new SQLException("Failed to connect to the database.");
        }

        // Create the select query by using question marks for the parameters
        String query = "SELECT * FROM tb_profiles WHERE id = ?";

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

            // Set the values
            profile.setId(rs.getInt("id"));
            profile.setName(rs.getString("name"));
            profile.setDescription(rs.getString("description"));
            profile.setPriority(rs.getInt("priority"));
            profile.setCreatedOn(rs.getDate("created_on").toLocalDate());

            // Return the user
            return profile;
        }

        // Return null as the profile was not found
        return null;
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
        String query = "SELECT * FROM tb_profiles WHERE id > 0";

        // Create a PreparedStatement
        PreparedStatement pstmt = conn.prepareStatement(query);

        // Execute the query
        ResultSet rs = pstmt.executeQuery();

        // Iterate through the result set and create profile objects
        while (rs.next()) {
            // Create a new instance of profile
            Profile profile = new Profile();

            // Set the values
            profile.setId(rs.getInt("id"));
            profile.setName(rs.getString("name"));
            profile.setDescription(rs.getString("description"));
            profile.setPriority(rs.getInt("priority"));
            profile.setCreatedOn(rs.getDate("created_on").toLocalDate());

            // Add the user to the list
            profiles.add(profile);
        }

        // Return the list of users
        return profiles;
    }

    @Override
    public String toString() {
        // Display the user information in the ListView (for example, firstName and lastName)
        return name;
    }
}
