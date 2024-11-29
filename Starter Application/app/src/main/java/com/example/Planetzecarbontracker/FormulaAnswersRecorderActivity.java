package com.example.Planetzecarbontracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class FormulaAnswersRecorderActivity extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.formula_answers_recorder, container, false);

        // Reference UI components
        TextView transportationAnswers = view.findViewById(R.id.transportationAnswers);
        TextView foodAnswers = view.findViewById(R.id.foodAnswers);
        TextView housingAnswers = view.findViewById(R.id.housingAnswers);
        TextView consumptionAnswers = view.findViewById(R.id.consumptionAnswers);
        Button backButton = view.findViewById(R.id.backButton);

        // Retrieve data passed via Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            String transportationData = bundle.getString("transportationData", "No data available for transportation.");
            String housingData = bundle.getString("housingData", "No data available for housing.");
            String foodData = bundle.getString("foodData", "No data available for food.");
            String consumptionData = bundle.getString("consumptionData", "No data available for consumption.");

            // Display data
            transportationAnswers.setText(transportationData);
            housingAnswers.setText(housingData);
            foodAnswers.setText(foodData);
            consumptionAnswers.setText(consumptionData);
        } else {
            transportationAnswers.setText("No data available for transportation.");
            housingAnswers.setText("No data available for housing.");
            foodAnswers.setText("No data available for food.");
            consumptionAnswers.setText("No data available for consumption.");
        }

        // Set Back button functionality
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change to emissions overview page
                loadFragment(new AnnualFootPrint());
            }
        });

        return view;
    }
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
