package com.example.Planetzecarbontracker;

import java.util.Date;

public class Emission {

    private int category_key;
    private int question_key;
//    private String response;
    private int value;
    private Date date;
    private final String[] questions = {

            "Drive Personal Vehicle (gasoline)", "Drive Personal Vehicle (Diesel)",
            "Drive Personal Vehicle (hybrid)", "Drive Personal Vehicle (Electric)",
            "Take Public Transportation (bus)", "Take Public Transportation (train)",
            "Take Public Transportation (subway)", "Cycling or Walking",
            "Flight (Short-Haul)", "Flight (Long-Haul",
            "Meal (beef)", "Meal (pork)",
            "Meal (chicken)", "Meal (fish)", "Meal (plant-based)",
            "Buy New Clothes",
            "Buy Electronics", "Energy Bills (Electricity)", "Energy Bills (Gas)",
            "Energy Bills (Water)"

    };

    public Emission() {};

    public Emission(int category_key, int question_key, int value, Date date) {

        this.category_key = category_key;
        this.question_key = question_key;
        this.value = value;
        this.date = date;
    }

    public double getEmission() {

        //based on index of questions
        switch (question_key) {
            case 0:
                return value * 0.24;
            case 1:
                return value * 0.27;
            case 2:
                return value * 0.16;
            case 3:
                return value * 0.05;
            case 4:
            case 5:
                return value * 0.72; // assume 246 kg per hour divided by 365 days
            case 6:
                return 0;
            case 7:
                return value * 225;
            case 8:
                return value * 825; // assume 225 and 825 are values for each flight
            case 9:
                return value * 6.85;
            case 10:
                return value * 3.97;
            case 11:
                return value * 2.6;
            case 12:
                return value * 2.19; // assume daily consumption divided by 365 days
            case 13:
                return value * 2.74; // assume vegetarian diet
            case 14:
                return value * 5; // assume clothing item is 5kg per item
            case 15:
                return value * 300; // assume each electronic device is 300kg
            case 16:
            case 17:
            case 18:

                // Use housing values to approximate energy bill to emissions

                if (value < 50) {
                    return 200 * value;
                } else if (value < 100) {
                    return 400 * value;
                } else if (value < 150) {
                    return 1200 * value;
                } else if (value < 200){
                    return 1700 * value;
                }

                return 2300 * value;
        }

        return 0;
    }

    public int getCategory () {
        return category_key;
    }


    public String getQuestion() {
        return questions[question_key];
    }


    public void setCategory(int category) {
        this.category_key = category;
    }

    public void setQuestionKey(int questionKey) {
        this.question_key = questionKey;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
