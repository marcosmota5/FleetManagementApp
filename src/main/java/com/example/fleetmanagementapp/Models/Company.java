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

public class Company {
    // Fields
    private int id;
    private String code;
    private String name;
    private LocalDate createdOn;

    // Getter and setter for id
    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for code
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for created on
    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    // Set the values of the current instance using a received result set
    public void setValuesByResultSet(ResultSet rs) throws SQLException {
        setId(rs.getInt("company_id"));
        setCode(rs.getString("company_code"));
        setName(rs.getString("company_name"));
        setCreatedOn(rs.getDate("company_created_on").toLocalDate());
    }

    // Get the company data using its id
    public static Company getCompanyById(int id) throws Exception {

        // Create the connection
        Connection conn = DbConnection.connectToDatabase();

        // If the connection is null, throw an exception
        if (conn == null) {
            throw new SQLException("Failed to connect to the database.");
        }

        // Create the select query by using question marks for the parameters
        String query = "SELECT * FROM vw_companies WHERE company_id = ?";

        // Use PreparedStatement to prevent SQL injection and bind parameters
        PreparedStatement pstmt = conn.prepareStatement(query);

        // Set the parameters
        pstmt.setInt(1, id);

        // Execute the query
        ResultSet rs = pstmt.executeQuery();

        // If there are values, return the profile
        if (rs.next()) {
            // Create a new instance of profile
            Company company = new Company();

            // Call the method that set the values
            company.setValuesByResultSet(rs);

            // Return the user
            return company;
        }

        // Return null as the profile was not found
        return null;
    }

    // Get all profiles
    public static List<Company> getAllCompanies() throws Exception {

        // Create the company list
        List<Company> companies = new ArrayList<>();

        // Create the connection
        Connection conn = DbConnection.connectToDatabase();

        // If the connection is null, throw an exception
        if (conn == null) {
            throw new SQLException("Failed to connect to the database.");
        }

        // SQL query to get all users
        String query = "SELECT * FROM vw_companies WHERE company_id > 0";

        // Create a PreparedStatement
        PreparedStatement pstmt = conn.prepareStatement(query);

        // Execute the query
        ResultSet rs = pstmt.executeQuery();

        // Iterate through the result set and create profile objects
        while (rs.next()) {
            // Create a new instance of profile
            Company company = new Company();

            // Call the method that set the values
            company.setValuesByResultSet(rs);

            // Add the user to the list
            companies.add(company);
        }

        // Return the list of users
        return companies;
    }

    @Override
    public String toString() {
        // Display the user information in the ListView
        return name;
    }
}
