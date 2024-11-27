package com.example.Planetzecarbontracker;

import java.util.Arrays;
import java.util.List;

public class User {

    private String uid;
    private String name;
    private String email;
    private boolean firstTime;
    private List<Double> totalEmissionsByCategory;

    public User() {}

    public User(String uid, String name, String email, boolean firstTime) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.firstTime = firstTime;

        this.totalEmissionsByCategory = Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0);
    }

    // Getters and setters
    public String getId() { return uid; }
    public void setId(String uid) { this.uid = uid; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public boolean getFirstTime() { return firstTime; }
    public void setFirstTime(boolean firstTime) { this.firstTime = firstTime; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public void setTotalEmissionsByCategory(List<Double> totalEmissions) {this.totalEmissionsByCategory = totalEmissions;}
    public void addToTotalEmissions(int transportationE, int foodE, int housingE, int consumptionE, int totalE){

    }public List<Double> getTotalEmissionsByCategory(){ return totalEmissionsByCategory;}
}
