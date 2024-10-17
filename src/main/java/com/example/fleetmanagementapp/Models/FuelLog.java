package com.example.fleetmanagementapp.Models;

import java.time.LocalDate;

public class FuelLog {

    // Fields
    private int id;
    private LocalDate fuelDate;
    private double litersFilled;
    private double fuelPricePerLiter;
    private double totalCost;
    private String comments;
    private Vehicle vehicle;
    private User user;

    // Getter and setter for id
    public int getId() { return id; }

    private void setId(int id) { this.id = id; }

    // Getter and Setter for fuel date
    public LocalDate getFuelDate() { return fuelDate; }

    public void setFuelDate(LocalDate fuelDate) { this.fuelDate = fuelDate; }

    // Getter and Setter for liters filled
    public double getLitersFilled() {
        return litersFilled;
    }

    public void setLitersFilled(double litersFilled) {
        this.litersFilled = litersFilled;
    }

    // Getter and Setter for fuel consumed
    public double getFuelPricePerLiter() {
        return fuelPricePerLiter;
    }

    public void setFuelPricePerLiter(double fuelPricePerLiter) {
        this.fuelPricePerLiter = fuelPricePerLiter;
    }

    // Getter and Setter for fuel consumed
    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
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

}
