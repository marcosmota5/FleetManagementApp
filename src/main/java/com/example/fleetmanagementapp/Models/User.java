package com.example.fleetmanagementapp.Models;

import com.example.fleetmanagementapp.Data.DbConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class User {

    // Fields
    private int id;
    private String firstName;
    private String lastName;
    private String sex;
    private LocalDate birthDate;
    private String phoneNumber;
    private String email;
    private String login;
    private LocalDate createdOn;
    private Profile profile;
    List<Company> companies;

    // Getter and setter for id
    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for first name
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // Getter and Setter for last name
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Getter and Setter for sex
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    // Getter and Setter for birthdate
    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    // Getter and Setter for phone number
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // Getter and Setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) { this.email = email; }

    // Getter and Setter for login
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    // Getter and Setter for created on
    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    // Getter and Setter for profile
    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    // Getter and Setter for companies
    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    // Get the full name
    public String getFullName() { return firstName + " " + lastName; }

    // Set the values of the current instance using a received result set
    public void setValuesByResultSet(ResultSet rs) throws SQLException {
        setId(rs.getInt("user_id"));
        setFirstName(rs.getString("user_first_name"));
        setLastName(rs.getString("user_last_name"));
        setSex(rs.getString("user_sex"));
        setBirthDate(rs.getDate("user_birth_date").toLocalDate());
        setPhoneNumber(rs.getString("user_phone_number"));
        setEmail(rs.getString("user_email"));
        setLogin(rs.getString("user_login"));
        setCreatedOn(rs.getDate("user_created_on").toLocalDate());

        // Create a new instance of profile
        Profile profile = new Profile();

        // Call the method that set the values
        profile.setValuesByResultSet(rs);

        // Set the profile
        setProfile(profile);
    }

    // Methods

    // Get the current user data using its id
    public static User getUserById(int id) throws Exception {

        // Create the connection
        Connection conn = DbConnection.connectToDatabase();

        // If the connection is null, throw an exception
        if (conn == null) {
            throw new SQLException("Failed to connect to the database.");
        }

        // Create the select query by using question marks for the parameters
        String query = "SELECT * FROM vw_users WHERE user_id = ?";

        // Use PreparedStatement to prevent SQL injection and bind parameters
        PreparedStatement pstmt = conn.prepareStatement(query);

        // Set the parameters
        pstmt.setInt(1, id);

        // Execute the query
        ResultSet rs = pstmt.executeQuery();

        // If there are values, return the user
        if (rs.next()) {
            // Create a new instance of user
            User user = new User();

            // Call the method that set the values
            user.setValuesByResultSet(rs);

            // Return the user
            return user;
        }

        // Return null as the user was not found
        return null;
    }

    // Execute the login and return a user instance
    public static User executeLogin(String loginOrEmail, String password) throws Exception {

        // Create the connection
        Connection conn = DbConnection.connectToDatabase();

        // If the connection is null, throw an exception
        if (conn == null) {
            throw new SQLException("Failed to connect to the database.");
        }

        // Create the select query by using question marks for the parameters
        String query = "{ CALL sp_execute_login(?, ?, ?) }";

        // Use PreparedStatement to prevent SQL injection and bind parameters
        CallableStatement cstmt = conn.prepareCall(query);

        // Bind parameters to the stored procedure
        cstmt.setString(1, loginOrEmail);
        cstmt.setString(2, password);

        // Register the OUT parameter (user ID)
        cstmt.registerOutParameter(3, java.sql.Types.INTEGER);

        // Execute the query
        ResultSet rs = cstmt.executeQuery();

        // Retrieve the user ID from the OUT parameter
        int userId = cstmt.getInt(3);

        // Check if a valid user ID was returned
        if (userId > 0) {
            // Use the user ID to fetch the user details
            return User.getUserById(userId);
        } else {
            throw new SQLException("Invalid login credentials.");
        }
    }

    // Register a new user and return its id
    public static int registerUser(String firstName, String lastName, String sex, LocalDate birthDate, String phoneNumber,
                                   String email, String login, String password, int profileId) throws Exception {

        // Create the connection
        Connection conn = DbConnection.connectToDatabase();

        // If the connection is null, throw an exception
        if (conn == null) {
            throw new SQLException("Failed to connect to the database.");
        }

        // Create the select query by using question marks for the parameters
        String query = "{ CALL sp_insert_user(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";

        // Use PreparedStatement to prevent SQL injection and bind parameters
        CallableStatement cstmt = conn.prepareCall(query);

        // Convert LocalDate to java.sql.Date
        java.sql.Date sqlBirthDate = java.sql.Date.valueOf(birthDate);

        // Bind parameters to the stored procedure
        cstmt.setString(1, firstName);
        cstmt.setString(2, lastName);
        cstmt.setString(3, sex);
        cstmt.setDate(4, sqlBirthDate);
        cstmt.setString(5, phoneNumber);
        cstmt.setString(6, email);
        cstmt.setString(7, login);
        cstmt.setString(8, password);
        cstmt.setInt(9, profileId);

        // Register the OUT parameter (user ID)
        cstmt.registerOutParameter(10, java.sql.Types.INTEGER);

        // Execute the query
        ResultSet rs = cstmt.executeQuery();

        // Retrieve and return the user ID from the OUT parameter
        return cstmt.getInt(10);
    }

    @Override
    public String toString() {
        // Display the user information in the ListView (for example, firstName and lastName)
        return firstName + " " + lastName + " (" + login + ")";
    }
}