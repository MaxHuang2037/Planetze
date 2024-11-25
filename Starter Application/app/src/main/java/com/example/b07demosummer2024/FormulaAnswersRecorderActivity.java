package com.example.b07demosummer2024;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FormulaAnswersRecorderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formula_answers_recorder);

        // Reference UI components
        TextView transportationAnswers = findViewById(R.id.transportationAnswers);
        TextView housingAnswers = findViewById(R.id.housingAnswers);
        TextView totalEmissions = findViewById(R.id.totalEmissions);
        Button backButton = findViewById(R.id.backButton);

        // Retrieve data passed from Intent
        Intent intent = getIntent();
        String transportationData = intent.getStringExtra("transportationData");
        String housingData = intent.getStringExtra("housingData");
        int totalCO2 = intent.getIntExtra("totalCO2", 0);

        // Display data with null safety
        if (transportationData != null && !transportationData.isEmpty()) {
            transportationAnswers.setText(transportationData);
        } else {
            transportationAnswers.setText("No data available for transportation.");
        }

        if (housingData != null && !housingData.isEmpty()) {
            housingAnswers.setText(housingData);
        } else {
            housingAnswers.setText("No data available for housing.");
        }

        totalEmissions.setText("Total CO2 Emissions: " + totalCO2 + " Kg");

        // Set Back button functionality
        backButton.setOnClickListener(v -> {
            finish(); // Close this activity and return to the previous screen
        });
    }
}
