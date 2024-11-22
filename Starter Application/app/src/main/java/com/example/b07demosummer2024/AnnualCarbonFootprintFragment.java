package com.example.b07demosummer2024;

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
    private String[] questions = {
            "Do you own or regularly use a car?",
            "What type of car do you drive?",
            "How many kilometers/miles do you drive per year?",
            "How often do you use public transportation (bus, train, subway)?",
            "How much time do you spend on public transport per week?",
            "How many short-haul flights (less than 1,500 km / 932 miles) have you taken in the past year?",
            "How many long-haul flights (more than 1,500 km / 932 miles) have you taken in the past year?",
            "What best describes your diet?",
            "How often do you eat the following animal-based products?",
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
            {"Vegetarian", "Vegan", "Pescatarian (fish/seafood)", "Meat-based (eat all types of animal products)"},
            {"Daily", "Frequently (3-5 times/week)", "Occasionally (1-2 times/week)", "Never"},
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

        // initial question setup
        questionText.setText(String.format("%d. %s", i + 1, questions[i]));
        for(int j = 0; j < 6; j++){
            if(j < answers[i].length){
                choices[j].setText(answers[i][j]);
            } else {
                choices[j].setVisibility(View.INVISIBLE);
            }
        }

        q6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "WELCOME ", Toast.LENGTH_SHORT).show();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeQuestion(1);
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeQuestion(0);
            }
        });

        return view;
    }

    private void changeQuestion(int buttonType){
        if(buttonType == 0){
            if(i == 0) return;
            i -= 1;
        } else {
            if(i == 20) return;
            i += 1;
        }
        radioGroup.clearCheck();
        questionText.setText(String.format("%d. %s", i + 1, questions[i]));
        for(int j = 0; j < 6; j++){
            if(j < answers[i].length){
                choices[j].setVisibility(View.VISIBLE);
                choices[j].setText(answers[i][j]);
            } else {
                choices[j].setVisibility(View.INVISIBLE);
            }
        }
    }
}
