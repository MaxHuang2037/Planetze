package com.example.Planetzecarbontracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
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

import java.util.HashMap;
import java.util.Map;

public class ComparePage extends Fragment {
    private TextView comparisonResultTextView;
    private double userFootprint;
    private String selectedCountry;
    private double selectedCountryFootprint;

    private static final Map<String, Double> countryFootprintMap = new HashMap<>();

    static {
        // Load country footprint data into the map
        countryFootprintMap.put("Afghanistan", 0.295);
        countryFootprintMap.put("Africa", 0.994);
        countryFootprintMap.put("Albania", 1.5);
        countryFootprintMap.put("Algeria", 3.6);
        countryFootprintMap.put("Andorra", 5.2);
        countryFootprintMap.put("Angola", 0.8);
        countryFootprintMap.put("Anguilla", 8.753);
        countryFootprintMap.put("Antigua and Barbuda", 6.42);
        countryFootprintMap.put("Argentina", 4.4);
        countryFootprintMap.put("Armenia", 2.31);
        countryFootprintMap.put("Aruba", 8.13);
        countryFootprintMap.put("Asia", 4.61);
        countryFootprintMap.put("Asia(excl. China and India", 4.02);
        countryFootprintMap.put("Australia", 15.8);
        countryFootprintMap.put("Austria", 7.5);
        countryFootprintMap.put("Azerbaijan", 3.7);
        countryFootprintMap.put("Bahamas", 6.9);
        countryFootprintMap.put("Bahrain", 25.2);
        countryFootprintMap.put("Bangladesh", 0.6);
        countryFootprintMap.put("Barbados", 3.9);
        countryFootprintMap.put("Belarus", 5.8);
        countryFootprintMap.put("Belgium", 8.6);
        countryFootprintMap.put("Belize", 1.3);
        countryFootprintMap.put("Benin", 0.5);
        countryFootprintMap.put("Bermuda", 6.94);
        countryFootprintMap.put("Bhutan", 1.1);
        countryFootprintMap.put("Bolivia", 1.8);
        countryFootprintMap.put("Bonaire Sint Eustatius and Saba", 4.08);
        countryFootprintMap.put("Bosnia and Herzegovina", 5.6);
        countryFootprintMap.put("Botswana", 2.9);
        countryFootprintMap.put("Brazil", 2.3);
        countryFootprintMap.put("British Virgin Island", 5.00);
        countryFootprintMap.put("Brunei", 23.7);
        countryFootprintMap.put("Bulgaria", 6.1);
        countryFootprintMap.put("Burkina Faso", 0.2);
        countryFootprintMap.put("Burundi", 0.0);
        countryFootprintMap.put("Cambodia", 0.4);
        countryFootprintMap.put("Cameroon", 0.4);
        countryFootprintMap.put("Canada", 15.0);
        countryFootprintMap.put("Cape Verde", 0.96);
        countryFootprintMap.put("Central African Republic", 0.04);
        countryFootprintMap.put("Chad", 0.13);
        countryFootprintMap.put("Chile", 4.5);
        countryFootprintMap.put("China", 7.4);
        countryFootprintMap.put("Colombia", 1.8);
        countryFootprintMap.put("Comoros", 0.49);
        countryFootprintMap.put("Congo", 0.3);
        countryFootprintMap.put("Cook Islands", 3.99);
        countryFootprintMap.put("Costa Rica", 1.7);
        countryFootprintMap.put("Cote d'Ivoire", 0.42);
        countryFootprintMap.put("Croatia", 4.2);
        countryFootprintMap.put("Cuba", 2.5);
        countryFootprintMap.put("Curaccao", 9.19);
        countryFootprintMap.put("Cyprus", 6.5);
        countryFootprintMap.put("Czechia", 8.8);
        countryFootprintMap.put("Democratic Republic of Congo", 0.04);
        countryFootprintMap.put("Denmark", 5.8);
        countryFootprintMap.put("Dominican Republic", 2.0);
        countryFootprintMap.put("Ecuador", 1.8);
        countryFootprintMap.put("Egypt", 2.7);
        countryFootprintMap.put("El Salvador", 1.1);
        countryFootprintMap.put("Estonia", 13.6);
        countryFootprintMap.put("Eswatini", 1.4);
        countryFootprintMap.put("Ethiopia", 0.1);
        countryFootprintMap.put("Finland", 8.5);
        countryFootprintMap.put("France", 5.5);
        countryFootprintMap.put("Germany", 9.7);
        countryFootprintMap.put("Ghana", 0.5);
        countryFootprintMap.put("Greece", 6.3);
        countryFootprintMap.put("Guatemala", 0.9);
        countryFootprintMap.put("Honduras", 1.0);
        countryFootprintMap.put("Hungary", 4.5);
        countryFootprintMap.put("Iceland", 6.7);
        countryFootprintMap.put("India", 1.8);
        countryFootprintMap.put("Indonesia", 2.3);
        countryFootprintMap.put("Iran", 7.7);
        countryFootprintMap.put("Iraq", 4.5);
        countryFootprintMap.put("Ireland", 8.4);
        countryFootprintMap.put("Israel", 7.9);
        countryFootprintMap.put("Italy", 5.9);
        countryFootprintMap.put("Jamaica", 2.5);
        countryFootprintMap.put("Japan", 9.0);
        countryFootprintMap.put("Jordan", 3.5);
        countryFootprintMap.put("Kazakhstan", 13.8);
        countryFootprintMap.put("Kenya", 0.3);
        countryFootprintMap.put("Kuwait", 25.7);
        countryFootprintMap.put("Latvia", 3.6);
        countryFootprintMap.put("Lebanon", 4.3);
        countryFootprintMap.put("Lithuania", 4.2);
        countryFootprintMap.put("Luxembourg", 15.3);
        countryFootprintMap.put("Madagascar", 0.1);
        countryFootprintMap.put("Malaysia", 8.1);
        countryFootprintMap.put("Maldives", 3.6);
        countryFootprintMap.put("Mali", 0.1);
        countryFootprintMap.put("Malta", 4.9);
        countryFootprintMap.put("Mexico", 3.6);
        countryFootprintMap.put("Monaco", 6.3);
        countryFootprintMap.put("Mongolia", 7.3);
        countryFootprintMap.put("Morocco", 1.8);
        countryFootprintMap.put("Mozambique", 0.3);
        countryFootprintMap.put("Myanmar", 0.6);
        countryFootprintMap.put("Nepal", 0.3);
        countryFootprintMap.put("Netherlands", 9.1);
        countryFootprintMap.put("New Zealand", 7.8);
        countryFootprintMap.put("Niger", 0.1);
        countryFootprintMap.put("Nigeria", 0.6);
        countryFootprintMap.put("Norway", 8.7);
        countryFootprintMap.put("Oman", 17.3);
        countryFootprintMap.put("Pakistan", 0.9);
        countryFootprintMap.put("Panama", 2.3);
        countryFootprintMap.put("Peru", 1.6);
        countryFootprintMap.put("Philippines", 1.2);
        countryFootprintMap.put("Poland", 8.3);
        countryFootprintMap.put("Portugal", 4.9);
        countryFootprintMap.put("Qatar", 37.1);
        countryFootprintMap.put("Romania", 3.8);
        countryFootprintMap.put("Russia", 11.5);
        countryFootprintMap.put("Saudi Arabia", 19.5);
        countryFootprintMap.put("Singapore", 9.0);
        countryFootprintMap.put("Slovakia", 6.0);
        countryFootprintMap.put("Slovenia", 6.9);
        countryFootprintMap.put("South Africa", 7.5);
        countryFootprintMap.put("South Korea", 12.3);
        countryFootprintMap.put("Spain", 5.5);
        countryFootprintMap.put("Sri Lanka", 1.1);
        countryFootprintMap.put("Sudan", 0.3);
        countryFootprintMap.put("Sweden", 4.5);
        countryFootprintMap.put("Switzerland", 4.3);
        countryFootprintMap.put("Syria", 1.6);
        countryFootprintMap.put("Taiwan", 10.9);
        countryFootprintMap.put("Tanzania", 0.2);
        countryFootprintMap.put("Thailand", 4.6);
        countryFootprintMap.put("Trinidad and Tobago", 24.9);
        countryFootprintMap.put("Tunisia", 2.4);
        countryFootprintMap.put("Turkey", 4.8);
        countryFootprintMap.put("Uganda", 0.1);
        countryFootprintMap.put("Ukraine", 5.7);
        countryFootprintMap.put("United Arab Emirates", 23.4);
        countryFootprintMap.put("United Kingdom", 5.8);
        countryFootprintMap.put("United States of America", 16.5);
        countryFootprintMap.put("Uruguay", 2.0);
        countryFootprintMap.put("Uzbekistan", 4.7);
        countryFootprintMap.put("Venezuela", 5.2);
        countryFootprintMap.put("Vietnam", 2.7);
        countryFootprintMap.put("Yemen", 0.9);
        countryFootprintMap.put("Zambia", 0.2);
        countryFootprintMap.put("Zimbabwe", 0.8);
    }
    private FirebaseAuth mAuth;
    private User user;
    private FirebaseDatabase db;
    private DatabaseReference userRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_compare_page, container, false);

        comparisonResultTextView = view.findViewById(R.id.comparison_result);
        Button backButton = view.findViewById(R.id.back_button);
        Button mainButton = view.findViewById(R.id.main_button);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        String UID = mAuth.getCurrentUser().getUid();
        db = FirebaseDatabase.getInstance();
        userRef = db.getReference("users").child(UID);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if (getArguments() != null) {
                    userFootprint = getArguments().getDouble("userFootprint", user.getTotalEmissionsByCategory().get(4)/1000);
                    selectedCountry = getArguments().getString("selectedCountry");
                    selectedCountryFootprint = getCountryFootprint(selectedCountry);
                }
                // Display comparison result
                displayComparisonResult();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
            }
        });

        // Set up back button to navigate back to CountrySelect fragment
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment countrySelectFragment = new CountrySelect();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, countrySelectFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        // Set up main button to navigate back to main page (assuming main page is another fragment called MainPage)
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

    private double getCountryFootprint(String country) {
        if (country == null || !countryFootprintMap.containsKey(country)) {
            return 10.0; // Default average if the country is not specified or found
        }
        return countryFootprintMap.get(country);
    }

    private void displayComparisonResult() {
        String comparisonResult;
        double difference = userFootprint - selectedCountryFootprint;
        double percentageDifference = (difference / selectedCountryFootprint) * 100;
        String percentageDifferenceFormatted = String.format("%.2f", Math.abs(percentageDifference));

        if (userFootprint < selectedCountryFootprint) {
            comparisonResult = "Your carbon footprint is below the average of " + selectedCountry + " (" + selectedCountryFootprint + " tons CO2e per year). " +
                    "It is " + percentageDifferenceFormatted + "% lower than the average.";
        } else if (userFootprint > selectedCountryFootprint) {
            comparisonResult = "Your carbon footprint is above the average of " + selectedCountry + " (" + selectedCountryFootprint + " tons CO2e per year). " +
                    "It is " + percentageDifferenceFormatted + "% higher than the average.";
        } else {
            comparisonResult = "Your carbon footprint is equal to the average of " + selectedCountry + " (" + selectedCountryFootprint + " tons CO2e per year).";
        }
        comparisonResultTextView.setText(comparisonResult);
    }
}