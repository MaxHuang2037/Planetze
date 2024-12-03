package com.example.Planetzecarbontracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import java.util.List;

public class EcoTrackerFragment extends Fragment {

    private Button open_tracker;
    private Button back_button;
    private TextView daily_emission;
    private LinearLayout activities_container;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference userRef;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_eco_tracker, container, false);

        daily_emission = view.findViewById(R.id.emissionsValue);
        back_button = view.findViewById(R.id.return_to_dashboard);
        open_tracker = view.findViewById(R.id.view_activities_button);

        activities_container = view.findViewById(R.id.activities_container);

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

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new DashboardFragment());
            }
        });

        open_tracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ActivityTrackerFragment());
            }
        });

        return view;
    }

    public void loadActivities() {

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        List<Emission> activities = user.getEcoTracker().getEmissionsByDate(year, month, day);

        for (int i = 0; i < Math.min(activities.size(), 3); i++) {
            LinearLayout rowLayout = new LinearLayout(getContext());
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            TextView activityName = new TextView(getContext());
            activityName.setText(activities.get(i).getQuestion());
            activityName.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            activityName.setTextSize(16);
            rowLayout.addView(activityName);

            TextView carbonEmission = new TextView(getContext());
            carbonEmission.setText(String.format("%s kg C02", activities.get(i).getEmission()));
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

        if (activities.size() > 3) {
            TextView moreEmissions = new TextView(getContext());
            moreEmissions.setText(String.format("%d More...", activities.size() - 3));
            moreEmissions.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            moreEmissions.setTextSize(16);
            moreEmissions.setTextAlignment(TextView.TEXT_ALIGNMENT_VIEW_START);
            finalRow.addView(moreEmissions);
        } else if (activities.isEmpty()) {
            TextView moreEmissions = new TextView(getContext());
            moreEmissions.setText("No activities today");
            moreEmissions.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            moreEmissions.setTextSize(16);
            moreEmissions.setTextAlignment(TextView.TEXT_ALIGNMENT_VIEW_START);
            finalRow.addView(moreEmissions);
        }

        double total_emission = 0;

        for (Emission activity : activities) {
            total_emission += activity.getEmission();
        }

        daily_emission.setText(String.format("%s kg C02", total_emission));

        activities_container.addView(finalRow);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
