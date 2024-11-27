package com.example.b07demosummer2024;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FormulaAnswersRecorderActivity extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.formula_answers_recorder, container, false);

        // Reference UI components
        TextView transportationAnswers = view.findViewById(R.id.transportationAnswers);
        TextView housingAnswers = view.findViewById(R.id.housingAnswers);
        TextView foodAnswers = view.findViewById(R.id.foodAnswers);
        TextView consumptionAnswers = view.findViewById(R.id.consumptionAnswers);
        TextView totalEmissions = view.findViewById(R.id.totalEmissions);
        Button backButton = view.findViewById(R.id.backButton);

        // Retrieve data passed via Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            String transportationData = bundle.getString("transportationData", "No data available for transportation.");
            String housingData = bundle.getString("housingData", "No data available for housing.");
            String foodData = bundle.getString("foodData", "No data available for food.");
            String consumptionData = bundle.getString("consumptionData", "No data available for consumption.");
            int totalCO2 = bundle.getInt("totalCO2", 0);

            // Display data
            transportationAnswers.setText(transportationData);
            housingAnswers.setText(housingData);
            foodAnswers.setText(foodData);
            consumptionAnswers.setText(consumptionData);
            totalEmissions.setText("Total CO2 Emissions: " + (totalCO2 / 1000.0) + " Tons");
        } else {
            transportationAnswers.setText("No data available for transportation.");
            housingAnswers.setText("No data available for housing.");
            foodAnswers.setText("No data available for food.");
            consumptionAnswers.setText("No data available for consumption.");
            totalEmissions.setText("Total CO2 Emissions: 0 Tons");
        }

        // Set Back button functionality
        backButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }
}
