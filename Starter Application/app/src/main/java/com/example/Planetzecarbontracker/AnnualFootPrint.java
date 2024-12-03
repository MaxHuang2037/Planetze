package com.example.Planetzecarbontracker;

import android.annotation.SuppressLint;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AnnualFootPrint extends Fragment {
    private double transportationEmissions;
    private double foodEmissions;
    private double housingEmissions;
    private double consumptionEmissions;

    private TextView transportationTextView;
    private TextView foodTextView;
    private TextView housingTextView;
    private TextView consumptionTextView;
    private TextView totalTextView;

    private User user;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference userRef;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_annual_footprint, container, false);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        userRef = db.getReference("users").child(mAuth.getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(com.example.Planetzecarbontracker.User.class);
                displayResults(user.getTotalEmissionsByCategory());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
            }
        });

        transportationTextView = view.findViewById(R.id.transportation_emissions);
        foodTextView = view.findViewById(R.id.food_emissions);
        housingTextView = view.findViewById(R.id.housing_emissions);
        consumptionTextView = view.findViewById(R.id.consumption_emissions);
        totalTextView = view.findViewById(R.id.total_emissions);
        Button nextButton = view.findViewById(R.id.next_button);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new CountrySelect());
            }
        });

        return view;
    }

    private void displayResults(List<Double> totalEmissionsByCategory) {
        transportationTextView.setText("Transportation: " + totalEmissionsByCategory.get(0) + " kg CO2e");
        foodTextView.setText("Food: " + totalEmissionsByCategory.get(1) + " kg CO2e");
        housingTextView.setText("Housing: " + totalEmissionsByCategory.get(2) + " kg CO2e");
        consumptionTextView.setText("Consumption: " + totalEmissionsByCategory.get(3) + " kg CO2e");

        totalTextView.setText("Total: " + totalEmissionsByCategory.get(4) / 1000 + " tons of CO2e per year");
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}