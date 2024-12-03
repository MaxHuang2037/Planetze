package com.example.Planetzecarbontracker;

import com.google.firebase.database.Exclude;

import java.util.Calendar;

public class HabitTracker {

    private final String[][] habits = {
            {"Increase time spent walking", "Increase time spent cycling"},
            {"Have a plant based meal", "Have a plant based diet"},
            {"Limit clothing purchases", "Limit electronic purchases"},
            {"Track your energy bills"}
    };

    private final String[][] habit_descriptions = {
            {"Walk at least 1 hour per day", "Cycle for at least 1 hour per day"},
            {"Have at least 1 plant based meal per day", "Have only plant based meals (2 min per day)"},
            {"Purchase less than 3 clothing items per month", "Purchase less than 3 electronics per year"},
            {"Track your monthly electricity bill to be more cautious of your energy usage"}
    };
    private boolean[] tracking = {false, false, false, false, false, false, false};
    public HabitTracker() {}

    //Habits have too different requirements, a case by case check is required
    @Exclude
    public boolean isComplete (EcoTracker eco_tracker, int index) {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DATE);

        switch (index) {
            case 0: // Walk
                int walkTime = 0;
                for (Emission e : eco_tracker.getEmissionsByDate(year, month, day)) {
                    if (e.getCategory() == 0 && e.getQuestion_key() == 7) {
                        walkTime += e.getValue();
                    }
                }
                return walkTime >= 1;

            case 1: // Cycle
                int cycleTime = 0;
                for (Emission e : eco_tracker.getEmissionsByDate(year, month, day)) {
                    if (e.getCategory() == 0 && e.getQuestion_key() == 7) {
                        cycleTime += e.getValue();
                    }
                }
                return cycleTime >= 1;

            case 2: // Plant based meal
                int plantMeals = 0;
                for (Emission e : eco_tracker.getEmissionsByDate(year, month, day)) {
                    if (e.getCategory() == 1 && e.getQuestion_key() == 14) {
                        plantMeals += e.getValue();
                    }
                }
                return plantMeals >= 1;

            case 3: // Plant based only
                int totalMeals = 0;
                int plantBasedMeals = 0;
                for (Emission e : eco_tracker.getEmissionsByDate(year, month, day)) {
                    if (e.getCategory() == 1) {
                        totalMeals += e.getValue();
                        if (e.getQuestion_key() == 14) {
                            plantBasedMeals += e.getValue();
                        }
                    }
                }
                return totalMeals > 0 && plantBasedMeals == totalMeals;

            case 4: // Clothes
                int clothingPurchases = 0;
                for (Emission e : eco_tracker.getEmissionsByMonth(year, month)) {
                    if (e.getCategory() == 2 && e.getQuestion_key() == 15) {
                        clothingPurchases += e.getValue();
                    }
                }
                return clothingPurchases < 3;

            case 5: // Electronics
                int electronicsPurchases = 0;
                for (Emission e : eco_tracker.getEmissionsByYear(year)) {
                    if (e.getCategory() == 2 && e.getQuestion_key() == 16) {
                        electronicsPurchases += e.getValue();
                    }
                }
                return electronicsPurchases < 3;

            case 6: // Electricity
                int electricityBills = 0;
                for (Emission e : eco_tracker.getEmissionsByMonth(year, month)) {
                    if (e.getCategory() == 3 && e.getQuestion_key() == 17) {
                        electricityBills += e.getValue();
                    }
                }
                return electricityBills > 0;

            default:
                return false;
        }
    }

    public void toggleTracking(int index) {
        tracking[index] = !tracking[index];
    }

    public boolean[] getTracking() {
        return tracking;
    }

    public void setTracking(boolean[] tracking) {
        this.tracking = tracking;
    }
}
