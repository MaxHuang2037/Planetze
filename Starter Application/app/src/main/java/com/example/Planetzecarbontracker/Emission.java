package com.example.Planetzecarbontracker;

import java.util.Date;

public class Emission {

    private int category_key;
    private int question_key;
    private int value;
    private Date date;
    private final String[] questions = {

            "Drive Personal Vehicle (Gasoline)", "Drive Personal Vehicle (Diesel)",
            "Drive Personal Vehicle (Hybrid)", "Drive Personal Vehicle (Electric)",
            "Take Public Transportation (Bus)", "Take Public Transportation (Train)",
            "Take Public Transportation (Subway)", "Cycling or Walking",
            "Flight (Short-Haul)", "Flight (Long-Haul)",
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
        double tempValue = 0;

        //based on index of questions
        switch (question_key) {
            case 0:
                tempValue = value * 0.24;
                break;
            case 1:
                tempValue = value * 0.27;
                break;
            case 2:
                tempValue = value * 0.16;
                break;
            case 3:
                tempValue = value * 0.05;
                break;
            case 4:
            case 5:
            case 6:
                tempValue = value * 0.72; // assume 246 kg per hour divided by 365 days
                break;
            case 7:
                tempValue = 0;
                break;
            case 8:
                tempValue = value * 225;
                break;
            case 9:
                tempValue = value * 825; // assume 225 and 825 are values for each flight
                break;
            case 10:
                tempValue = value * 6.85;
                break;
            case 11:
                tempValue = value * 3.97;
                break;
            case 12:
                tempValue = value * 2.6;
                break;
            case 13:
                tempValue = value * 2.19; // assume daily consumption divided by 365 days
                break;
            case 14:
                tempValue = value * 2.74; // assume vegetarian diet
                break;
            case 15:
                tempValue = value * 5; // assume clothing item is 5kg per item
                break;
            case 16:
                tempValue = value * 300; // assume each electronic device is 300kg
                break;
            case 17:
            case 18:
            case 19:

                // Use housing values to approximate energy bill to emissions

                if (value < 50) {
                    return 34;
                } else if (value < 100) {
                    return 67;
                } else if (value < 150) {
                    return 100;
                } else if (value < 200){
                    return 142;
                }

                tempValue = 192;
                break;
        }

        return ((double) Math.round(tempValue * 100) / 100);
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

    public int getQuestion_key() {
        return question_key;
    }

    public void setQuestion_key(int question_key) {
        this.question_key = question_key;
    }

    public int getCategory_key() {
        return category_key;
    }

    public void setCategory_key(int category_key) {
        this.category_key = category_key;
    }
}
