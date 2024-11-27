package com.example.b07demosummer2024;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AnnualCarbonFootprintFragment extends Fragment {
    private TextView questionText;
    private Button nextButton, previousButton;
    private RadioGroup radioGroup;
    private RadioButton q1, q2, q3, q4, q5, q6;
    private TextView[] choices;
    private int i = 0;
    private int totalC02 = 0;
    private String[] questions = {
            "Do you own or regularly use a car?",
            "What type of car do you drive?",
            "How many kilometers/miles do you drive per year?",
            "How often do you use public transportation (bus, train, subway)?",
            "How much time do you spend on public transport per week?",
            "How many short-haul flights (less than 1,500 km / 932 miles) have you taken in the past year?",
            "How many long-haul flights (more than 1,500 km / 932 miles) have you taken in the past year?",
            "What best describes your diet?",
            "How often do you eat beef?",
            "How often do you eat pork?",
            "How often do you eat chicken?",
            "How often do you eat fish?",
            "How often do you waste food or throw away uneaten leftovers?",
            "What type of home do you live in?",
            "How many people live in your household?",
            "What is the size of your home?",
            "What type of energy do you use to heat your home?",
            "What is your average monthly electricity bill?",
            "What type of energy do you use to heat water in your home?",
            "Do you use any renewable energy sources for electricity or heating?",
            "How often do you buy new clothes?",
            "Do you buy second-hand or eco-friendly products?",
            "How many electronic devices (phones, laptops, etc.) have you purchased in the past year?",
            "How often do you recycle?"
    };

    String[][] answers = {
            {"Yes", "No"},
            {"Gasoline", "Diesel", "Hybrid", "Electric", "I don’t know"},
            {"Up to 5,000 km (3,000 miles)", "5,000–10,000 km (3,000–6,000 miles)", "10,000–15,000 km (6,000–9,000 miles)",
                    "15,000–20,000 km (9,000–12,000 miles)", "20,000–25,000 km (12,000–15,000 miles)", "More than 25,000 km (15,000 miles)"},
            {"Never", "Occasionally (1-2 times/week)", "Frequently (3-4 times/week)", "Always (5+ times/week)"},
            {"Under 1 hour", "1-3 hours", "3-5 hours", "5-10 hours", "More than 10 hours"},
            {"None", "1-2 flights", "3-5 flights", "6-10 flights", "More than 10 flights"},
            {"None", "1-2 flights", "3-5 flights", "6-10 flights", "More than 10 flights"},
            {"Vegetarian", "Vegan", "Pescatarian", "Meat-based"},
            {"Daily", "Frequently", "Occasionally", "Never"},
            {"Daily", "Frequently", "Occasionally", "Never"},
            {"Daily", "Frequently", "Occasionally", "Never"},
            {"Daily", "Frequently", "Occasionally", "Never"},
            {"Never", "Rarely", "Occasionally", "Frequently"},
            {"Detached house", "Semi-detached house", "Townhouse", "Condo/Apartment", "Other"},
            {"1", "2", "3-4", "5 or more"},
            {"Under 1000 sq. ft.", "1000-2000 sq. ft.", "Over 2000 sq. ft."},
            {"Natural Gas", "Electricity", "Oil", "Propane", "Wood", "Other"},
            {"Under $50", "$50-$100", "$100-$150", "$150-$200", "Over $200"},
            {"Natural Gas", "Electricity", "Oil", "Propane", "Solar", "Other"},
            {"Yes, primarily (more than 50% of energy use)", "Yes, partially (less than 50% of energy use)", "No"},
            {"Monthly", "Quarterly", "Annually", "Rarely"},
            {"Yes, regularly", "Yes, occasionally", "No"},
            {"None", "1", "2", "3 or more"},
            {"Never", "Occasionally", "Frequently", "Always"}
    };

    int[] chosenAnswers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.annual_carbon_footprint_fragment, container, false);

        questionText = view.findViewById(R.id.questionText);
        nextButton = view.findViewById(R.id.nextButton);
        previousButton = view.findViewById(R.id.previousButton);
        radioGroup = view.findViewById(R.id.choices);

        q1 = view.findViewById(R.id.q1);
        q2 = view.findViewById(R.id.q2);
        q3 = view.findViewById(R.id.q3);
        q4 = view.findViewById(R.id.q4);
        q5 = view.findViewById(R.id.q5);
        q6 = view.findViewById(R.id.q6);

        choices = new RadioButton[]{q1, q2, q3, q4, q5, q6};
        chosenAnswers = new int[21];

        questionText.setText(String.format("%d. %s", i + 1, questions[i]));
        for (int j = 0; j < 6; j++) {
            int temp = j;
            choices[j].setOnClickListener(v -> chosenAnswers[i] = temp);

            if (j < answers[i].length) {
                choices[j].setText(answers[i][j]);
            } else {
                choices[j].setVisibility(View.INVISIBLE);
            }
        }

        nextButton.setOnClickListener(v -> changeQuestion(1));
        previousButton.setOnClickListener(v -> changeQuestion(0));

        return view;
    }

    private void calculateTransportationEmission() {
        int transportationEmissions = 0;

        if (chosenAnswers[0] == 1) {
            Toast.makeText(getContext(), "No car usage, skipping car-related questions.", Toast.LENGTH_SHORT).show();
            return;
        }

        double emissionFactor = 0.0;
        switch (chosenAnswers[1]) {
            case 0: emissionFactor = 0.24; break;
            case 1: emissionFactor = 0.27; break;
            case 2: emissionFactor = 0.16; break;
            case 3: emissionFactor = 0.05; break;
            case 4: emissionFactor = 0.2; break;
        }

        int[] distances = {5000, 10000, 15000, 20000, 25000, 35000};
        transportationEmissions += emissionFactor * distances[chosenAnswers[2]];

        int[][] publicTransportEmissions = {
                {0, 0, 0, 0, 0},
                {246, 819, 1638, 3071, 4095},
                {573, 1911, 3822, 7166, 9555},
                {573, 1911, 3822, 7166, 9555}
        };
        transportationEmissions += publicTransportEmissions[chosenAnswers[3]][chosenAnswers[4]];

        int[] shortHaulFlightEmissions = {0, 225, 600, 1200, 1800};
        transportationEmissions += shortHaulFlightEmissions[chosenAnswers[5]];

        int[] longHaulFlightEmissions = {0, 825, 2200, 4400, 6600};
        transportationEmissions += longHaulFlightEmissions[chosenAnswers[6]];

        totalC02 += transportationEmissions;
    }

    private void calculateHousingEmission() {
        int emissions = 0;

        int houseType = chosenAnswers[10] == 4 ? 2 : chosenAnswers[10];
        int numPeople = chosenAnswers[11];
        int homeSize = chosenAnswers[12];
        int energyHeatHome = chosenAnswers[13] == 5 ? 1 : chosenAnswers[13];
        int averageMonthlyElectricBill = chosenAnswers[14];
        int energyHeatWater = chosenAnswers[15] == 5 ? 1 : chosenAnswers[15];

        if (energyHeatHome != energyHeatWater) {
            emissions += 233;
        }

        int yIndex = houseType * 12 + homeSize * 4 + numPeople;
        int xIndexHome = averageMonthlyElectricBill * 5 + energyHeatHome;
        int xIndexWater = averageMonthlyElectricBill * 5 + energyHeatWater;

        Toast.makeText(getContext(), "(" + xIndexHome + ", " + yIndex + ")", Toast.LENGTH_SHORT).show();

        if (chosenAnswers[16] == 0) {
            emissions -= 6000;
        } else if (chosenAnswers[16] == 1) {
            emissions -= 4000;
        }

        totalC02 += emissions;
    }

    private void calculateFoodEmissions() {
        // Question 1: Diet
        switch (chosenAnswers[7]) {
            case 0: totalC02 += 1000; return; // Vegetarian
            case 1: totalC02 += 500; return; // Vegan
            case 2: totalC02 += 1500; return; // Pescatarian
            // Meat-based will proceed to subsequent questions
        }

        // Question 2: Beef Consumption
        int[] beefEmissions = {2500, 1900, 1300, 0};
        totalC02 += beefEmissions[chosenAnswers[8]];

        // Question 3: Pork Consumption
        int[] porkEmissions = {1450, 860, 450, 0};
        totalC02 += porkEmissions[chosenAnswers[9]];

        // Question 4: Chicken Consumption
        int[] chickenEmissions = {950, 600, 200, 0};
        totalC02 += chickenEmissions[chosenAnswers[10]];

        // Question 5: Fish Consumption
        int[] fishEmissions = {800, 500, 150, 0};
        totalC02 += fishEmissions[chosenAnswers[11]];

        // Question 6: Food Waste
        double[] foodWasteEmissions = {0, 23.4, 70.2, 140.4};
        totalC02 += foodWasteEmissions[chosenAnswers[12]];
    }

    private void calculateConsumptionEmissions() {
        // Question 1: Clothing
        int[] clothingEmissions = {360, 120, 100, 5};
        int clothingCO2 = clothingEmissions[chosenAnswers[13]];
        totalC02 += clothingCO2;

        // Question 2: Eco-Friendly Products
        switch (chosenAnswers[14]) {
            case 0: totalC02 -= clothingCO2 * 0.5; break; // Regularly
            case 1: totalC02 -= clothingCO2 * 0.3; break; // Occasionally
        }

        // Question 3: Electronics
        int[] electronicsEmissions = {0, 300, 600, 900, 1200};
        totalC02 += electronicsEmissions[chosenAnswers[15]];

        // Question 4: Recycling
        double[][] recyclingReductions = {
                {0, 54, 108, 180}, // Monthly Buyers
                {0, 15, 30, 50},   // Annual Buyers
                {0, 0.75, 1.5, 2.5} // Rarely Buyers
        };
        int buyerIndex = chosenAnswers[13]; // Clothing buyers
        int recycleIndex = chosenAnswers[16]; // Recycling frequency
        if (buyerIndex < recyclingReductions.length && recycleIndex < recyclingReductions[0].length) {
            totalC02 -= recyclingReductions[buyerIndex][recycleIndex];
        }

        // Electronics Recycling
        int[][] electronicsRecycling = {
                {0, 45, 60, 90}, // 1 Device
                {0, 60, 120, 180}, // 2 Devices
                {0, 90, 180, 270}, // 3 Devices
                {0, 120, 240, 360} // 4+ Devices
        };
        int deviceIndex = Math.min(chosenAnswers[15], 3); // Clamp to max device index
        totalC02 -= electronicsRecycling[deviceIndex][recycleIndex];
    }



    private void navigateToResults() {
        calculateTransportationEmission();
        calculateHousingEmission();
        calculateFoodEmissions();
        calculateConsumptionEmissions();

        // Prepare Transportation Data
        StringBuilder transportationData = new StringBuilder("Transportation:\n");
        transportationData.append("1. ").append(questions[0]).append(": ").append(answers[0][chosenAnswers[0]]).append("\n");
        if (chosenAnswers[0] == 0) {
            transportationData.append("2. ").append(questions[1]).append(": ").append(answers[1][chosenAnswers[1]]).append("\n");
            transportationData.append("3. ").append(questions[2]).append(": ").append(answers[2][chosenAnswers[2]]).append("\n");
        }
        transportationData.append("4. ").append(questions[3]).append(": ").append(answers[3][chosenAnswers[3]]).append("\n");
        transportationData.append("5. ").append(questions[4]).append(": ").append(answers[4][chosenAnswers[4]]).append("\n");
        transportationData.append("6. ").append(questions[5]).append(": ").append(answers[5][chosenAnswers[5]]).append("\n");
        transportationData.append("7. ").append(questions[6]).append(": ").append(answers[6][chosenAnswers[6]]).append("\n");

        // Prepare Housing Data
        StringBuilder housingData = new StringBuilder("Housing:\n");
        housingData.append("10. ").append(questions[10]).append(": ").append(answers[10][chosenAnswers[10]]).append("\n");
        housingData.append("11. ").append(questions[11]).append(": ").append(answers[11][chosenAnswers[11]]).append("\n");
        housingData.append("12. ").append(questions[12]).append(": ").append(answers[12][chosenAnswers[12]]).append("\n");

        // Prepare Food Data
        StringBuilder foodData = new StringBuilder("Food:\n");
        foodData.append("8. ").append(questions[7]).append(": ").append(answers[7][chosenAnswers[7]]).append("\n");
        if (chosenAnswers[7] == 3) { // Meat-based
            foodData.append("9. ").append(questions[8]).append(": ").append(answers[8][chosenAnswers[8]]).append("\n");
            foodData.append("10. ").append(questions[9]).append(": ").append(answers[9][chosenAnswers[9]]).append("\n");
            foodData.append("11. ").append(questions[10]).append(": ").append(answers[10][chosenAnswers[10]]).append("\n");
            foodData.append("12. ").append(questions[11]).append(": ").append(answers[11][chosenAnswers[11]]).append("\n");
        }
        foodData.append("13. ").append(questions[12]).append(": ").append(answers[12][chosenAnswers[12]]).append("\n");

        // Prepare Consumption Data
        StringBuilder consumptionData = new StringBuilder("Consumption:\n");
        consumptionData.append("14. ").append(questions[13]).append(": ").append(answers[13][chosenAnswers[13]]).append("\n");
        consumptionData.append("15. ").append(questions[14]).append(": ").append(answers[14][chosenAnswers[14]]).append("\n");
        consumptionData.append("16. ").append(questions[15]).append(": ").append(answers[15][chosenAnswers[15]]).append("\n");
        consumptionData.append("17. ").append(questions[16]).append(": ").append(answers[16][chosenAnswers[16]]).append("\n");

        // Bundle and Pass Data
        Bundle bundle = new Bundle();
        bundle.putString("transportationData", transportationData.length() > 0 ? transportationData.toString() : "No transportation data.");
        bundle.putString("housingData", housingData.length() > 0 ? housingData.toString() : "No housing data.");
        bundle.putString("foodData", foodData.length() > 0 ? foodData.toString() : "No food data.");
        bundle.putString("consumptionData", consumptionData.length() > 0 ? consumptionData.toString() : "No consumption data.");
        bundle.putInt("totalCO2", totalC02);

        // Create and Navigate to Results Fragment
        FormulaAnswersRecorderActivity resultsFragment = new FormulaAnswersRecorderActivity();
        resultsFragment.setArguments(bundle);

        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, resultsFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }




    private void changeQuestion(int buttonType) {
        if (buttonType == 0) {
            if (i == 0) return;
            i -= 1;
        } else {
            if (i == 19) {
                nextButton.setText("Finish");
            } else if (i == 20) {
                navigateToResults();
                return;
            }
            i += 1;
        }
        radioGroup.clearCheck();
        questionText.setText(String.format("%d. %s", i + 1, questions[i]));
        for (int j = 0; j < 6; j++) {
            if (j < answers[i].length) {
                choices[j].setVisibility(View.VISIBLE);
                choices[j].setText(answers[i][j]);
            } else {
                choices[j].setVisibility(View.INVISIBLE);
            }
        }
    }
}
