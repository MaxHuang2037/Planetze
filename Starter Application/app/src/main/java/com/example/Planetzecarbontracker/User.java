package com.example.Planetzecarbontracker;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class User {

    private String uid;
    private String name;
    private String email;
    private boolean firstTime;
    private List<Double> totalEmissionsByCategory;
    private EcoTracker ecoTracker;

    private HabitTracker habitTracker;

    public User() {}

    public User(String uid, String name, String email, boolean firstTime) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.firstTime = firstTime;
        this.totalEmissionsByCategory = Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0);
        this.ecoTracker = new EcoTracker(new ArrayList<>());
        this.habitTracker = new HabitTracker();
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
    public EcoTracker getEcoTracker () {
        if (ecoTracker == null) {
            ecoTracker = new EcoTracker();
        }
        return ecoTracker;
    }
    public void setEcoTracker (EcoTracker ecoTracker) {
        this.ecoTracker = ecoTracker;
    }
    public double calculateTotalEmissionsByDateRange(List<Emission> emissions){
        double total = 0;
        for(Emission emission : emissions){
            total += emission.getEmission();
        }
        return total;
    }

    public HabitTracker getHabitTracker() {
        return habitTracker;
    }

    public void setHabitTracker(HabitTracker habitTracker) {
        this.habitTracker = habitTracker;
    }
}
