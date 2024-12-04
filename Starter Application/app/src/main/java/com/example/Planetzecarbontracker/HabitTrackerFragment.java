package com.example.Planetzecarbontracker;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class HabitTrackerFragment extends Fragment {

    private Button back_button;
    private LinearLayout habits_container;
    private LinearLayout habits_selector;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference userRef;
    private User user;

    private Spinner filter_category;
    private Spinner filter_intensity;
    private EditText search_habit;
    private TextView recommendedHabitName;
    private TextView recommendedHabitDescription;
    private Button habitToggleButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_habit_tracker, container, false);

        back_button = view.findViewById(R.id.back_button);
        habits_container = view.findViewById(R.id.habits_container);
        habits_selector = view.findViewById(R.id.habits_selector);
        filter_category = view.findViewById(R.id.category_spinner);
        filter_intensity = view.findViewById(R.id.impact_spinner);
        search_habit = view.findViewById(R.id.search_habit);
        recommendedHabitName = view.findViewById(R.id.recommended_habit_name);
        recommendedHabitDescription = view.findViewById(R.id.recommended_habit_description);
        habitToggleButton = view.findViewById(R.id.habit_toggle_button);

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
                            loadHabits();
                            loadHabitSelection(inflater, "", -1, -1);
                            recommendHabit();
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

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new EcoTrackerFragment());
            }
        });

        filter_category = view.findViewById(R.id.category_spinner);
        filter_intensity = view.findViewById(R.id.impact_spinner);
        search_habit = view.findViewById(R.id.search_habit);

        ArrayAdapter<String> intensityAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_layout,
                new String[]{"Intensity ", "Most intense", "Least intense"});
        intensityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filter_intensity.setAdapter(intensityAdapter);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_layout,
                new String[]{"Category", "Transportation", "Food", "Shopping", "Energy"});
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filter_category.setAdapter(categoryAdapter);

        search_habit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String searchQuery = charSequence.toString();
                loadHabitSelection(inflater, searchQuery, filter_category.getSelectedItemPosition() - 1, filter_intensity.getSelectedItemPosition() - 1);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        filter_intensity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                loadHabitSelection(inflater, search_habit.getText().toString(), filter_category.getSelectedItemPosition() - 1, position - 1 );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        filter_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                loadHabitSelection(inflater, search_habit.getText().toString(), position - 1, filter_intensity.getSelectedItemPosition() - 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        return view;
    }

    private void loadHabits () {
        HabitTracker habitTracker = user.getHabitTracker();
        EcoTracker ecoTracker = user.getEcoTracker();
        ArrayList<Boolean> tracking = habitTracker.getTracking();

        habits_container.removeAllViews();

        for (int i = 0; i < tracking.size(); i++) {
            if (!tracking.get(i)) {
                continue;
            }

            LinearLayout habitRow = new LinearLayout(getContext());
            habitRow.setOrientation(LinearLayout.HORIZONTAL);
            habitRow.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            habitRow.setPadding(16, 16, 16, 16);

            TextView habitName = new TextView(getContext());
            habitName.setText(habitTracker.habit_descriptions[(int) i/2][i % 2]);
            habitName.setTextSize(16);
            habitName.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            ));

            boolean isComplete = habitTracker.isComplete(ecoTracker, i);

            habitName.setTextColor(getResources().getColor(isComplete ? R.color.habit_green : R.color.text_black));

            TextView habitCheckmark = new TextView(getContext());
            habitCheckmark.setText(isComplete ? "✔" : "✖");
            habitCheckmark.setTextSize(18);
            habitCheckmark.setTextColor(getResources().getColor(isComplete ? R.color.habit_green : R.color.habit_grey));
            habitCheckmark.setPadding(8, 0, 0, 0);

            habitRow.addView(habitName);
            habitRow.addView(habitCheckmark);

            habits_container.addView(habitRow);
        }
    }

    private void recommendHabit() {

        HabitTracker habitTracker = user.getHabitTracker();
        EcoTracker ecoTracker = user.getEcoTracker();

        double[] totalEmissions = ecoTracker.getTotalEmissionsByCategory(ecoTracker.getEmissions());

        int highestCategory = 0;
        double highestEmission = totalEmissions[0];
        for (int i = 1; i < totalEmissions.length; i++) {
            if (totalEmissions[i] > highestEmission) {
                highestEmission = totalEmissions[i];
                highestCategory = i;
            }
        }

        String habit = habitTracker.habits[highestCategory][0];
        String habitDescriptionText = habitTracker.habit_descriptions[highestCategory][0];

        recommendedHabitName.setText(habit);
        recommendedHabitDescription.setText(habitDescriptionText);

        final int final_recommendation = highestCategory * 2;

        if (habitTracker.getTracking().get(final_recommendation)) {
            habitToggleButton.setText("Stop Habit");
        } else {
            habitToggleButton.setText("Start Habit");
        }

        habitToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                habitTracker.toggleTracking(final_recommendation);
                String UID = mAuth.getUid();
                userRef.child(UID).child("habitTracker").setValue(user.getHabitTracker());
                if (habitTracker.getTracking().get(final_recommendation)) {
                    habitToggleButton.setText("Stop Habit");
                    Toast.makeText(getContext(), "Habit started",
                            Toast.LENGTH_SHORT).show();
                } else {
                    habitToggleButton.setText("Start Habit");
                    Toast.makeText(getContext(), "Habit stopped",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    public void loadHabitSelection(@Nullable LayoutInflater inflater, String search, int category, int intensity) {

        HabitTracker habitTracker = user.getHabitTracker();
        habits_selector.removeAllViews();

        List<View> habitViews = new ArrayList<>();
        List<Integer> habitIntensities = new ArrayList<>();

        int habit_index = 0;

        for (int i = 0; i < habitTracker.habits.length; i++) {
            if (category != -1 && category != i) {
                habit_index += habitTracker.habits[i].length;
                continue;
            }

            for (int j = 0; j < habitTracker.habits[i].length; j++) {
                String habitName = habitTracker.habits[i][j];
                if (!habitName.toLowerCase().contains(search.toLowerCase())) {
                    habit_index++;
                    continue;
                }

                View habitRow = inflater.inflate(R.layout.habit_selection_item, null);

                TextView habitNameTextView = habitRow.findViewById(R.id.habit_name);
                habitNameTextView.setText(habitName);

                TextView habitDescriptionTextView = habitRow.findViewById(R.id.habit_description);
                habitDescriptionTextView.setText(habitTracker.habit_descriptions[i][j]);
                habitDescriptionTextView.setTextColor(Color.DKGRAY);

                Button toggleButton = habitRow.findViewById(R.id.habit_toggle_button);
                if (habitTracker.getTracking().get(habit_index)) {
                    toggleButton.setText("Stop Habit");
                } else {
                    toggleButton.setText("Start Habit");
                }

                final int final_habit_index = habit_index;

                toggleButton.setOnClickListener(v -> {
                    habitTracker.toggleTracking(final_habit_index);
                    String UID = mAuth.getUid();
                    userRef.child(UID).child("habitTracker").setValue(user.getHabitTracker());
                    if (habitTracker.getTracking().get(final_habit_index)) {
                        toggleButton.setText("Stop Habit");
                        Toast.makeText(getContext(), "Habit started",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        toggleButton.setText("Start Habit");
                        Toast.makeText(getContext(), "Habit stopped",
                                Toast.LENGTH_SHORT).show();
                    }

                });

                habitViews.add(habitRow);
                habitIntensities.add(habitTracker.habit_intensity[habit_index]);
                habit_index++;
            }
        }

        if (intensity == 1) { // Lowest first
            for (int i = 0; i < habitIntensities.size(); i++) {
                for (int j = i + 1; j < habitIntensities.size(); j++) {
                    Log.v("Intensity", habitIntensities.get(i) + " " + habitIntensities.get(j));
                    if (habitIntensities.get(i) > habitIntensities.get(j)) {
                        Collections.swap(habitIntensities, i, j);
                        Collections.swap(habitViews, i, j);
                    }
                }
            }
        } else if (intensity == 0) { // Highest first
            for (int i = 0; i < habitIntensities.size(); i++) {
                for (int j = i + 1; j < habitIntensities.size(); j++) {
                    Log.v("Intensity", habitIntensities.get(i) + " " + habitIntensities.get(j));
                    if (habitIntensities.get(i) < habitIntensities.get(j)) {
                        Collections.swap(habitIntensities, i, j);
                        Collections.swap(habitViews, i, j);
                    }
                }
            }
        }

        if (habitViews.isEmpty()) {
            TextView noHabitsText = new TextView(getContext());
            noHabitsText.setText("No habits found");
            noHabitsText.setTextColor(Color.DKGRAY);
            habits_selector.addView(noHabitsText);
        } else {
            for (View habitRow : habitViews) {
                habits_selector.addView(habitRow);
            }
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
