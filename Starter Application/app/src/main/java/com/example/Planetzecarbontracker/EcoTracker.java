package com.example.Planetzecarbontracker;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    @Exclude
    public double[] getTotalEmissions() {
        double[] total_emission = {0, 0, 0, 0};
        for (int i = 0; i < emissions.size(); i++) {
            Emission emission = emissions.get(i);

            total_emission[emission.getCategory()] = emission.getEmission();
        }

        return total_emission;
    }

    public List<Emission> getEmissionsByMonth(int year, int month) {
        List<Emission> result = new ArrayList<>();

        for (Emission emission : emissions) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(emission.getDate());

            int emissionYear = calendar.get(Calendar.YEAR);
            int emissionMonth = calendar.get(Calendar.MONTH);
            if (emissionYear == year && emissionMonth == month - 1) {
                result.add(emission);
            }
        }

        return result;
    }

    public List<Emission> getEmissionsByRange(Date startDate, Date endDate) {
        List<Emission> result = new ArrayList<>();

        for (Emission emission : emissions) {
            Date emissionDate = emission.getDate();
            if (!emissionDate.before(startDate) && !emissionDate.after(endDate)) {
                result.add(emission);
            }
        }

        return result;
    }

    public List<Emission> getEmissionsByDate(Date targetDate) {
        List<Emission> result = new ArrayList<>();

        for (Emission emission : emissions) {
            if (emission.getDate().equals(targetDate)) {
                result.add(emission);
            }
        }

        return result;
    }

}
