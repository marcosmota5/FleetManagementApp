package com.example.fleetmanagementapp.Data;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnection {

    // Declare the connection details
    private final static String CONNECTION_STRING = "jdbc:mysql://localhost:3306/db_fleet_management";
    private final static String USER = "fleet_management";
    private final static String PASSWORD = "d2u_LMMfqsX4bvxg";

    // Create and return the connection based on the constants
    public static Connection connectToDatabase() {
        try {
            return DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}