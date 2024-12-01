
package com.example.Planetzecarbontracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ComparePage extends Fragment {
    private TextView comparisonResultTextView;
    private double userFootprint;
    private String selectedCountry;
    private double selectedCountryFootprint;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_compare_page, container, false);

        comparisonResultTextView = view.findViewById(R.id.comparison_result);

        // Retrieve the user footprint and selected country details from the arguments
        if (getArguments() != null) {
            userFootprint = getArguments().getDouble("userFootprint", 0.0);
            selectedCountry = getArguments().getString("selectedCountry");
            selectedCountryFootprint = getCountryFootprint(selectedCountry);
        }

        // Display comparison result
        displayComparisonResult();

        return view;
    }

    private double getCountryFootprint(String country) {
        switch (country) {
            case "Canada":
                return 15.0;
            case "USA":
                return 16.5;
            case "UK":
                return 9.0;
            case "Germany":
                return 9.7;
            case "India":
                return 1.8;
            case "China":
                return 7.4;
            case "Brazil":
                return 2.2;
            default:
                return 10.0; // Default average if the country is not specifically listed
        }
    }

    private void displayComparisonResult() {
        String comparisonResult;
        if (userFootprint < selectedCountryFootprint) {
            comparisonResult = "Your carbon footprint is below the average of " + selectedCountry + " (" + selectedCountryFootprint + " kg CO2e per year).";
        } else {
            comparisonResult = "Your carbon footprint is above the average of " + selectedCountry + " (" + selectedCountryFootprint + " kg CO2e per year).";
        }
        comparisonResultTextView.setText(comparisonResult);
    }
}