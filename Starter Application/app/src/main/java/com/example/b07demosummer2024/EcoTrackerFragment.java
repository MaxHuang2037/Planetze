package com.example.b07demosummer2024;

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

import java.util.List;

public class EcoTrackerFragment extends Fragment {

    private Button open_tracker;
    private Button back_button;
    private TextView daily_emission;
    private LinearLayout activities_container;
    private List<Emission> activities;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_eco_tracker, container, false);

        daily_emission = view.findViewById(R.id.emissionsValue);
        back_button = view.findViewById(R.id.return_to_dashboard);
        open_tracker = view.findViewById(R.id.view_activities_button);

        LinearLayout activities_container = view.findViewById(R.id.activities_container);

        String[][] activities = {
                {"Running", "5 kg CO2"},
                {"Cycling", "2 kg CO2"},
                {"Driving", "20 kg CO2"}
        };

        for (int i = 0; i < Math.min(activities.length, 3); i++) {
            LinearLayout rowLayout = new LinearLayout(getContext());
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            TextView activityNumber = new TextView(getContext());
            activityNumber.setText("Activity " + (i + 1));
            activityNumber.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            activityNumber.setTextSize(16);
            rowLayout.addView(activityNumber);

            TextView activityName = new TextView(getContext());
            activityName.setText(activities[i][0]); // e.g., Running, Cycling
            activityName.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            activityName.setTextSize(16);
            rowLayout.addView(activityName);

            TextView carbonEmission = new TextView(getContext());
            carbonEmission.setText(activities[i][1]); // e.g., 5 kg CO2
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

        TextView moreEmissions = new TextView(getContext());
        moreEmissions.setText("7 More..."); // e.g., 5 kg CO2
        moreEmissions.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        moreEmissions.setTextSize(16);
        moreEmissions.setTextAlignment(TextView.TEXT_ALIGNMENT_VIEW_START);
        finalRow.addView(moreEmissions);

        activities_container.addView(finalRow);

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

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
