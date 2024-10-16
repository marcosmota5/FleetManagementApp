package com.example.fleetmanagementapp.Models;

import com.example.fleetmanagementapp.Data.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Profile {

    // Fields
    private int id;
    private String name;
    private String description;
    private int priority;
    private Date createdOn;

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
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    // Get the profile data using its id
    public static Profile getProfileById(int id) {
        try (Connection conn = DbConnection.connectToDatabase()) {
            if (conn == null) {
                System.out.println("Failed to connect to the database.");
                return null;
            }

            // Create the select query by using question marks for the parameters
            String query = "SELECT * FROM tb_profiles WHERE id = ?";

            // Use PreparedStatement to prevent SQL injection and bind parameters
            PreparedStatement pstmt = conn.prepareStatement(query);

            // Set the parameters
            pstmt.setInt(1, id);

            // Execute the query
            ResultSet rs = pstmt.executeQuery();

            // If there are values, return the user
            if (rs.next()) {
                // Create a new instance of user
                Profile profile = new Profile();

                // Set the values
                profile.setId(rs.getInt("id"));
                profile.setName(rs.getString("name"));
                profile.setDescription(rs.getString("description"));
                profile.setPriority(rs.getInt("priority"));
                profile.setCreatedOn(rs.getDate("created_on"));

                // Return the user
                return profile;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return null as the user was not found
        return null;
    }

}
