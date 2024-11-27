package com.example.b07demosummer2024;

import java.util.ArrayList;
import java.util.List;

public class EcoTracker {

    private String test;

    private List<Emission> emissions = new ArrayList<>();
    private final String[] categories = {
            "Transportation Activities",
            "Food Consumption Activities",
            "Consumption and Shopping Activities",
            "Energy"
    };

    public EcoTracker() {}

    public EcoTracker(List<Emission> emissions, String test) {
        this.emissions = emissions;
        this.test = test;
    }

    public void addEmission(Emission emission) {
        emissions.add(emission);
    }

    public void removeEmission(int index) {
        emissions.remove(index);
    }

    public List<Emission> getEmissions () {
        return emissions;
    }

    public void setEmissions (List<Emission> emissions) {
        this.emissions = emissions;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    /*

        get total emissions based on category enum

        0 -> Transportation Activities
        1 -> Food Consumption Activities
        2 -> Consumption and Shopping Activities
        3 -> Energy

     */
//    public double[] getTotalEmissions() {
//        double[] total_emission = {0, 0, 0, 0};
//        for (int i = 0; i < emissions.size(); i++) {
//            Emission emission = emissions.get(i);
//
//            total_emission[emission.getCategory()] = emission.getEmission();
//        }
//
//        return total_emission;
//    }

}
