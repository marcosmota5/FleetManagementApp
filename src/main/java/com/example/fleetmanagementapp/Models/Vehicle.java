package com.example.fleetmanagementapp.Models;

import java.time.LocalDate;
import java.util.Date;

public class Vehicle {

    // Fields
    private int id;
    private String licensePlate;
    private String type;
    private String brand;
    private String model;
    private int year;
    private String fuelType;
    private double mileage;
    private double fuelLevel;
    private String status;
    private LocalDate createdOn;
    private Company company;

    // Getter and setter for id
    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for license plate
    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    // Getter and Setter for type
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // Getter and Setter for brand
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    // Getter and Setter for model
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    // Getter and setter for year
    public int getYear() {
        return year;
    }

    private void setYear(int year) {
        this.year = year;
    }

    // Getter and Setter for fuel type
    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    // Getter and Setter for fuel type
    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    // Getter and Setter for fuel fuel level
    public double getFuelLevel() {
        return fuelLevel;
    }

    public void setFuelLevel(double fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    // Getter and Setter for status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Getter and Setter for created on
    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    // Getter and setter for id
    public Company getCompany() {
        return company;
    }

    private void setCompany(Company company) { this.company = company; }

    @Override
    public String toString() {
        // Display the user information in the ListView (for example, firstName and lastName)
        return brand + " " + model + " (" + licensePlate + ")";
    }
}