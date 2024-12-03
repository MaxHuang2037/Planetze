package com.example.Planetzecarbontracker;

import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ActivityTrackerFragment extends Fragment {
    private LinearLayout add_activity_wrap;
    private LinearLayout select_category_wrap;
    private LinearLayout question_details_wrap;
    private LinearLayout question_buttons_wrap;
    private EditText activity_quantity;
    private EditText date_picker;
    private Button submit_activity;
    private Spinner activity_dropdown;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference userRef;
    private User user;
    private Date tracking_date = new Date();
    private LinearLayout activities_container;
    private TextView daily_emission;
    private int selected_index = 0;
    private final String[][] actions = {
            {"Drive personal vehicle", "Take public transportation", "Cycling or walking", "Flight"},
            {"Had A Meal"},
            {"Buy new clothes", "Buy electronics"},
            {"Energy bills"}
    };

    private final String[][] dropdown_questions = {
            {"Gasoline", "Diesel", "Hybrid", "Electric"},
            {"Bus", "Train", "Subway"},
            {"Walking or Cycling"},
            {"Short-Haul (Less than 1500km)", "Long-Haul (More than 1500km)"},
            {"Beef", "Pork", "Chicken", "Fish", "Plant-Based"},
            {"Clothing"},
            {"Electronics"},
            {"Electricity", "Gas", "Water"}
    };

    private final String[][] input_questions = {
            {"Distance driven in km", "Hours spent in transit", "Hours travelled", "Times flown"},
            {"Meals eaten"},
            {"Clothing items purchased", "Electronics purchased"},
            {"Your monthly bill in dollars"}
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_emission_manager, container, false);

        Button back_button = view.findViewById(R.id.back_button);
        Button category_transportation = view.findViewById(R.id.category_transportation);
        Button category_food = view.findViewById(R.id.category_food);
        Button category_shopping = view.findViewById(R.id.category_shopping);
        Button category_energy = view.findViewById(R.id.category_energy);

        add_activity_wrap = view.findViewById(R.id.add_activity_wrap);
        select_category_wrap = view.findViewById(R.id.select_category_wrap);
        question_details_wrap = view.findViewById(R.id.question_details_wrap);
        question_buttons_wrap = view.findViewById(R.id.question_buttons_wrap);
        activity_dropdown = view.findViewById(R.id.activity_dropdown);
        activity_quantity = view.findViewById(R.id.activity_quantity);
        submit_activity = view.findViewById(R.id.submit_activity);
        date_picker = view.findViewById(R.id.date_picker);
        activities_container = view.findViewById(R.id.activities_container);
        daily_emission = view.findViewById(R.id.emissionsValue);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            String UID = mAuth.getCurrentUser().getUid();
            db = FirebaseDatabase.getInstance();
            userRef = db.getReference("users");
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User u = snapshot.getValue(User.class);
                        if(u.getId().equals(UID)){
                            user = u;
                            loadActivities();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Failed to read value
                }
            });
        } else {
            loadFragment(new HomeFragment());
        }

        Calendar calendar = Calendar.getInstance();
        date_picker.setText(String.format("%d/%d/%d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR)));

        date_picker.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(getContext(), (picker, selectedYear, selectedMonth, selectedDay) -> {
                calendar.set(selectedYear, selectedMonth, selectedDay);

                tracking_date = calendar.getTime();
                String dateString = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                date_picker.setText(dateString);

                loadActivities();
            }, year, month, day).show();
        });

        activity_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selected_index = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new EcoTrackerFragment());
            }
        });

        category_transportation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCategory(0);
            }
        });

        category_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCategory(1);
            }
        });

        category_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCategory(2);
            }
        });

        category_energy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCategory(3);
            }
        });

        return view;
    }

    public void loadActivities() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(tracking_date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        double total_emission = 0;

        activities_container.removeAllViews();

        List<Emission> activities = user.getEcoTracker().getEmissionsByDate(year, month, day);

        for (int i = 0; i < activities.size(); i++) {

            Emission emission = activities.get(i);

            total_emission += emission.getEmission();

            LinearLayout rowLayout = new LinearLayout(getContext());
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            rowLayout.setGravity(Gravity.CENTER_VERTICAL);

            ImageButton deleteButton = new ImageButton(getContext());
            deleteButton.setImageResource(android.R.drawable.ic_delete);
            LinearLayout.LayoutParams deleteButtonParams = new LinearLayout.LayoutParams(
                    (int) getResources().getDisplayMetrics().density * 24,
                    (int) getResources().getDisplayMetrics().density * 24
            );
            deleteButton.setLayoutParams(deleteButtonParams);
            deleteButtonParams.rightMargin = (int) getResources().getDisplayMetrics().density * 8;
            deleteButton.setBackgroundColor(Color.TRANSPARENT);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.getEcoTracker().removeEmission(emission);

                    String UID = mAuth.getUid();
                    userRef.child(UID).child("ecoTracker").setValue(user.getEcoTracker());

                    Toast.makeText(getContext(), "Emission Removed.",
                            Toast.LENGTH_SHORT).show();
                }
            });

            rowLayout.addView(deleteButton);

            TextView activityName = new TextView(getContext());
            activityName.setText(activities.get(i).getQuestion());
            activityName.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            activityName.setTextSize(16);
            rowLayout.addView(activityName);

            TextView carbonEmission = new TextView(getContext());
            carbonEmission.setText(String.format("%s kg C02", activities.get(i).getEmission())); // e.g., 5 kg CO2
            carbonEmission.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            carbonEmission.setTextSize(16);
            carbonEmission.setTextAlignment(TextView.TEXT_ALIGNMENT_VIEW_END);
            rowLayout.addView(carbonEmission);

            activities_container.addView(rowLayout);
        }

        LinearLayout finalRow = new LinearLayout(getContext());
        finalRow.setOrientation(LinearLayout.HORIZONTAL);
        finalRow.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        daily_emission.setText(String.format("Daily emissions: %s kg C02", ((double) Math.round(total_emission * 100) / 100)));

        activities_container.addView(finalRow);
    }

    public void loadQuestion(int category, int question) {
        question_buttons_wrap.setVisibility(View.GONE);
        question_details_wrap.setVisibility(View.VISIBLE);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                dropdown_questions[getQuestionIndex(category, question)]
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activity_dropdown.setAdapter(adapter);

        activity_quantity.setHint(input_questions[category][question]);

        submit_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = activity_quantity.getText().toString().trim();
                if (user != null && !quantity.isEmpty()) {
                    int value = Integer.parseInt(quantity);
                    // testing
                    Emission new_activity = new Emission(category, getDropdownQuestionIndex(getQuestionIndex(category, question), selected_index), value, tracking_date);
                    user.getEcoTracker().addEmission(new_activity);

                    String UID = mAuth.getUid();
                    userRef.child(UID).child("ecoTracker").setValue(user.getEcoTracker());

                    Toast.makeText(getContext(), "New activity logged.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void loadCategory(int category) {
        select_category_wrap.setVisibility(View.GONE);
        add_activity_wrap.setVisibility(View.VISIBLE);
        question_buttons_wrap.setVisibility(View.VISIBLE);
        activities_container.setVisibility(View.INVISIBLE);

        for (int i = 0; i < 4; i++) {
            View view = question_buttons_wrap.getChildAt(i);

            if (view instanceof Button) {

                if (i < actions[category].length) {
                    Button question_btn = (Button) view;
                    question_btn.setText(actions[category][i]);

                    int button_index = i;
                    question_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadQuestion(category, button_index);
                        }
                    });
                } else {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }

    public int getQuestionIndex(int category, int question) {
        int counter = 0;
        for (int i = 0; i < actions.length; i++) {
            if (category == i) {
                return counter + question;
            }
            counter += actions[i].length;
        }
        return -1;
    }

    public int getDropdownQuestionIndex(int question, int selection) {
        int counter = 0;
        for (int i = 0; i < dropdown_questions.length; i++) {
            if (question == i) {
                return counter + selection;
            }
            counter += dropdown_questions[i].length;
        }
        return -1;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
