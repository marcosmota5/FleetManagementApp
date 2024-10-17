package com.example.fleetmanagementapp.Models;

import java.time.LocalDate;
import java.util.Date;

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

    public void setName(String lastName) {
        this.name = name;
    }

    // Getter and Setter for created on
    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public String toString() {
        // Display the user information in the ListView (for example, firstName and lastName)
        return name;
    }
}
