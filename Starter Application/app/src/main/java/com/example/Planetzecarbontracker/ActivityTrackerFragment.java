package com.example.Planetzecarbontracker;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
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

import java.util.Date;
import java.util.List;

public class ActivityTrackerFragment extends Fragment {
    private LinearLayout add_activity_wrap;
    private LinearLayout select_category_wrap;
    private LinearLayout question_details_wrap;
    private LinearLayout question_buttons_wrap;
    private EditText activity_quantity;
    private Button submit_activity;
    private Spinner activity_dropdown;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference userRef;
    private User user;
    private final String[][] actions = {
            {"Drive personal vehicle", "Take public transportation", "Cycling or walking", "Flight"},
            {"Had A Meal"},
            {"Buy new clothes", "Buy electronics"},
            {"Energy bills"}
    };

    private final String[][] dropdown_questions = {
            {"Gasoline", "Diesel", "Hybrid", "Electric"},
            {"Bus", "Train", "Subway"},
            {"Walking", "Cycling"},
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

        Log.d("ExampleFragment", "onCreateView: Fragment is being created");

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
                    Emission new_activity = new Emission(category, question, value, new Date());
                    user.getEcoTracker().addEmission(new_activity);

                    userRef = db.getReference("users");
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

    public int getQuestionIndex (int category, int question) {
        int counter = 0;
        for (int i = 0; i < actions.length; i++) {
            if (category == i) {
                return counter + question;
            }
            counter += actions[i].length;
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
