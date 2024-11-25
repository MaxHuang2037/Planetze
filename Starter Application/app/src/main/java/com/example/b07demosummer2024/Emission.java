package com.example.b07demosummer2024;

import java.util.Date;

public class Emission {

    private final int category_key;
    private final int question_key;
    private String response;
    private int value;
    private int emission;
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

    private final String[] categories = {
            "Transportation Activities",
            "Food Consumption Activities",
            "Consumption and Shopping Activities"
    };

    public Emission(int category_key, int question_key, int value, Date date) {

        this.category_key = category_key;
        this.question_key = question_key;
        this.value = value;
        this.date = date;
    }

    public String getQuestion() {
        return questions[question_key];
    }

    public String getCateogry() {
        return categories[category_key];
    }

}
