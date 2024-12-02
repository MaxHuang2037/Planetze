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
        countryFootprintMap.put("Africa", 0.99);
        countryFootprintMap.put("Albania", 1.74);
        countryFootprintMap.put("Algeria", 3.93);
        countryFootprintMap.put("Andorra", 4.62);
        countryFootprintMap.put("Angola", 0.45);
        countryFootprintMap.put("Anguilla", 8.75);
        countryFootprintMap.put("Antigua and Barbuda", 6.42);
        countryFootprintMap.put("Argentina", 4.23);
        countryFootprintMap.put("Armenia", 2.30);
        countryFootprintMap.put("Aruba", 8.13);
        countryFootprintMap.put("Asia", 4.61);
        countryFootprintMap.put("Asia(excl. China and India", 4.02);
        countryFootprintMap.put("Australia", 14.99);
        countryFootprintMap.put("Austria", 6.88);
        countryFootprintMap.put("Azerbaijan", 3.7);
        countryFootprintMap.put("Bahamas", 5.17);
        countryFootprintMap.put("Bahrain", 25.67);
        countryFootprintMap.put("Bangladesh", 0.60);
        countryFootprintMap.put("Barbados", 4.38);
        countryFootprintMap.put("Belarus", 6.17);
        countryFootprintMap.put("Belgium", 7.69);
        countryFootprintMap.put("Belize", 1.79);
        countryFootprintMap.put("Benin", 0.63);
        countryFootprintMap.put("Bermuda", 6.94);
        countryFootprintMap.put("Bhutan", 1.35);
        countryFootprintMap.put("Bolivia", 1.76);
        countryFootprintMap.put("Bonaire Sint Eustatius and Saba", 4.08);
        countryFootprintMap.put("Bosnia and Herzegovina", 6.10);
        countryFootprintMap.put("Botswana", 2.84);
        countryFootprintMap.put("Brazil", 2.25);
        countryFootprintMap.put("British Virgin Island", 5.00);
        countryFootprintMap.put("Brunei", 23.95);
        countryFootprintMap.put("Bulgaria", 6.80);
        countryFootprintMap.put("Burkina Faso", 0.26);
        countryFootprintMap.put("Burundi", 0.06);
        countryFootprintMap.put("Cambodia", 1.19);
        countryFootprintMap.put("Cameroon", 0.34);
        countryFootprintMap.put("Canada", 14.25);
        countryFootprintMap.put("Cape Verde", 0.96);
        countryFootprintMap.put("Central African Republic", 0.04);
        countryFootprintMap.put("Chad", 0.13);
        countryFootprintMap.put("Chile", 4.30);
        countryFootprintMap.put("China", 7.99);
        countryFootprintMap.put("Colombia", 1.92);
        countryFootprintMap.put("Comoros", 0.49);
        countryFootprintMap.put("Congo", 1.24);
        countryFootprintMap.put("Cook Islands", 3.99);
        countryFootprintMap.put("Costa Rica", 1.52);
        countryFootprintMap.put("Cote d'Ivoire", 0.42);
        countryFootprintMap.put("Croatia", 4.34);
        countryFootprintMap.put("Cuba", 1.87);
        countryFootprintMap.put("Curaccao", 9.19);
        countryFootprintMap.put("Cyprus", 5.62);
        countryFootprintMap.put("Czechia", 9.34);
        countryFootprintMap.put("Democratic Republic of Congo", 0.04);
        countryFootprintMap.put("Denmark", 4.94);
        countryFootprintMap.put("Djibouti", 0.40);
        countryFootprintMap.put("Dominica", 2.10);
        countryFootprintMap.put("Dominican Republic", 2.10);
        countryFootprintMap.put("East Timor", 0.49);
        countryFootprintMap.put("Ecuador", 2.31);
        countryFootprintMap.put("Egypt", 2.33);
        countryFootprintMap.put("El Salvador", 1.22);
        countryFootprintMap.put("Equatorial Guinea", 3.03);
        countryFootprintMap.put("Eritrea", 0.19);
        countryFootprintMap.put("Estonia", 13.6);
        countryFootprintMap.put("Eswatini", 1.05);
        countryFootprintMap.put("Ethiopia", 0.15);
        countryFootprintMap.put("Europe", 6.86);
        countryFootprintMap.put("Europe(excl. EU-27)", 7.89);
        countryFootprintMap.put("Europe(excl. EU-28)", 8.82);
        countryFootprintMap.put("Europe Union(27)", 6.17);
        countryFootprintMap.put("Europe Union(28)", 5.98);
        countryFootprintMap.put("Faroe Island", 14.08);
        countryFootprintMap.put("Fiji", 1.16);
        countryFootprintMap.put("Finland", 8.5);
        countryFootprintMap.put("France", 5.5);
        countryFootprintMap.put("French Polynesia", 2.85);
        countryFootprintMap.put("Gabon", 2.39);
        countryFootprintMap.put("Gambia", 0.28);
        countryFootprintMap.put("Georgia", 2.96);
        countryFootprintMap.put("Germany", 7.98);
        countryFootprintMap.put("Ghana", 0.62);
        countryFootprintMap.put("Greece", 5.75);
        countryFootprintMap.put("Greenland", 10.47);
        countryFootprintMap.put("Grenada", 2.71);
        countryFootprintMap.put("Guatemala", 1.08);
        countryFootprintMap.put("Guinea", 0.36);
        countryFootprintMap.put("Guinea-Bissau", 0.16);
        countryFootprintMap.put("Guyana", 4.37);
        countryFootprintMap.put("Haiti", 0.21);
        countryFootprintMap.put("High-income countries", 10.13);
        countryFootprintMap.put("Honduras", 1.0);
        countryFootprintMap.put("Hong Kong", 4.08);
        countryFootprintMap.put("Hungary", 4.5);
        countryFootprintMap.put("Iceland", 9.49);
        countryFootprintMap.put("India", 1.99);
        countryFootprintMap.put("Indonesia", 2.65);
        countryFootprintMap.put("Iran", 7.79);
        countryFootprintMap.put("Iraq", 4.02);
        countryFootprintMap.put("Ireland", 7.72);
        countryFootprintMap.put("Israel", 6.21);
        countryFootprintMap.put("Italy", 5.73);
        countryFootprintMap.put("Jamaica", 2.29);
        countryFootprintMap.put("Japan", 8.50);
        countryFootprintMap.put("Jordan", 2.03);
        countryFootprintMap.put("Kazakhstan", 13.98);
        countryFootprintMap.put("Kenya", 0.46);
        countryFootprintMap.put("Kiribati", 0.52);
        countryFootprintMap.put("Kosovo", 4.83);
        countryFootprintMap.put("Kuwait", 25.7);
        countryFootprintMap.put("Kyrgyzstan", 1.43);
        countryFootprintMap.put("Laos", 3.08);
        countryFootprintMap.put("Latvia", 3.56);
        countryFootprintMap.put("Lebanon", 4.35);
        countryFootprintMap.put("Lesotho", 1.36);
        countryFootprintMap.put("Liberia", 0.17);
        countryFootprintMap.put("Libya", 9.24);
        countryFootprintMap.put("Liechtenstein", 3.81);
        countryFootprintMap.put("Lithuania", 4.2);
        countryFootprintMap.put("Low-income countries", 0.28);
        countryFootprintMap.put("Lower-middle-income countries", 1.78);
        countryFootprintMap.put("Luxembourg", 15.3);
        countryFootprintMap.put("Macao", 1.51);
        countryFootprintMap.put("Madagascar", 0.15);
        countryFootprintMap.put("Malawi", 0.10);
        countryFootprintMap.put("Malaysia", 8.58);
        countryFootprintMap.put("Maldives", 3.25);
        countryFootprintMap.put("Mali", 0.31);
        countryFootprintMap.put("Malta", 3.10);
        countryFootprintMap.put("Marshall Islands", 3.64);
        countryFootprintMap.put("Mauritania", 0.96);
        countryFootprintMap.put("Mauritius", 3.27);
        countryFootprintMap.put("Mexico", 4.02);
        countryFootprintMap.put("Micronesia (country)", 1.32);
        countryFootprintMap.put("Moldova", 1.66);
        countryFootprintMap.put("Mongolia", 11.15);
        countryFootprintMap.put("Montenegro", 3.66);
        countryFootprintMap.put("Montserrat", 4.84);
        countryFootprintMap.put("Morocco", 1.83);
        countryFootprintMap.put("Mozambique", 0.24);
        countryFootprintMap.put("Myanmar", 0.64);
        countryFootprintMap.put("Namibia", 1.54);
        countryFootprintMap.put("Nauru", 4.17);
        countryFootprintMap.put("Nepal", 0.51);
        countryFootprintMap.put("Netherlands", 7.14);
        countryFootprintMap.put("New Caledonia", 17.64);
        countryFootprintMap.put("New Zealand", 6.21);
        countryFootprintMap.put("Nicaragua", 0.80);
        countryFootprintMap.put("Niger", 0.12);
        countryFootprintMap.put("Nigeria", 0.59);
        countryFootprintMap.put("Niue", 3.87);
        countryFootprintMap.put("North America", 10.53);
        countryFootprintMap.put("North America (excl. USA)", 4.74);
        countryFootprintMap.put("North Korea", 1.95);
        countryFootprintMap.put("North Macedonia", 3.62);
        countryFootprintMap.put("Norway", 7.51);
        countryFootprintMap.put("Oceania", 9.85);
        countryFootprintMap.put("Oman", 15.73);
        countryFootprintMap.put("Pakistan", 0.85);
        countryFootprintMap.put("Palau", 12.12);
        countryFootprintMap.put("Palestine", 0.67);
        countryFootprintMap.put("Panama", 2.70);
        countryFootprintMap.put("Papua New Guinea", 0.77);
        countryFootprintMap.put("Paraguay", 1.33);
        countryFootprintMap.put("Peru", 1.79);
        countryFootprintMap.put("Philippines", 1.30);
        countryFootprintMap.put("Poland", 8.11);
        countryFootprintMap.put("Portugal", 4.05);
        countryFootprintMap.put("Qatar", 37.60);
        countryFootprintMap.put("Romania", 3.74);
        countryFootprintMap.put("Russia", 11.42);
        countryFootprintMap.put("Rwanda", 0.11);
        countryFootprintMap.put("Saint Helena", 3.30);
        countryFootprintMap.put("Saint Kitts and Nevis", 4.71);
        countryFootprintMap.put("Saint Lucia", 2.61);
        countryFootprintMap.put("Saint Pierre and Miquelon", 10.29);
        countryFootprintMap.put("Saint Vincent and the Grenadines", 2.30);
        countryFootprintMap.put("Samoa", 1.12);
        countryFootprintMap.put("Sao Tome and Principe", 0.58);
        countryFootprintMap.put("Saudi Arabia", 18.20);
        countryFootprintMap.put("Senegal", 0.67);
        countryFootprintMap.put("Serbia", 6.02);
        countryFootprintMap.put("Seychelles", 6.15);
        countryFootprintMap.put("Sierra Leone", 0.13);
        countryFootprintMap.put("Singapore", 8.91);
        countryFootprintMap.put("Sint Maarten (Dutch part)", 14.35);
        countryFootprintMap.put("Slovakia", 6.05);
        countryFootprintMap.put("Slovenia", 6.00);
        countryFootprintMap.put("Solomon Islands", 0.41);
        countryFootprintMap.put("Somalia", 0.04);
        countryFootprintMap.put("South Africa", 6.75);
        countryFootprintMap.put("South America", 2.49);
        countryFootprintMap.put("South Korea", 11.60);
        countryFootprintMap.put("South Sudan", 0.17);
        countryFootprintMap.put("Spain", 5.16);
        countryFootprintMap.put("Sri Lanka", 0.79);
        countryFootprintMap.put("Sudan", 0.47);
        countryFootprintMap.put("Suriname", 5.80);
        countryFootprintMap.put("Sweden", 3.61);
        countryFootprintMap.put("Switzerland", 4.05);
        countryFootprintMap.put("Syria", 1.25);
        countryFootprintMap.put("Taiwan", 11.63);
        countryFootprintMap.put("Tajikistan", 1.01);
        countryFootprintMap.put("Tanzania", 0.24);
        countryFootprintMap.put("Thailand", 3.78);
        countryFootprintMap.put("Togo", 0.29);
        countryFootprintMap.put("Tonga", 1.77);
        countryFootprintMap.put("Trinidad and Tobago", 22.42);
        countryFootprintMap.put("Tunisia", 2.88);
        countryFootprintMap.put("Turkey", 5.11);
        countryFootprintMap.put("Turkmenistan", 11.03);
        countryFootprintMap.put("Turks and Caicos Islands", 7.64);
        countryFootprintMap.put("Tuvalu", 1.00);
        countryFootprintMap.put("Uganda", 0.13);
        countryFootprintMap.put("Ukraine", 3.56);
        countryFootprintMap.put("United Arab Emirates", 25.83);
        countryFootprintMap.put("United Kingdom", 4.72);
        countryFootprintMap.put("United States", 14.95);
        countryFootprintMap.put("Upper-middle-income countries", 6.23);
        countryFootprintMap.put("Uruguay", 2.31);
        countryFootprintMap.put("Uzbekistan", 3.48);
        countryFootprintMap.put("Vanuatu", 0.64);
        countryFootprintMap.put("Venezuela", 2.72);
        countryFootprintMap.put("Vietnam", 3.50);
        countryFootprintMap.put("Wallis and Futuna", 2.28);
        countryFootprintMap.put("World", 4.66);
        countryFootprintMap.put("Yemen", 0.34);
        countryFootprintMap.put("Zambia", 0.45);
        countryFootprintMap.put("Zimbabwe", 0.54);
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