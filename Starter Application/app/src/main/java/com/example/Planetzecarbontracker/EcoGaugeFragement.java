package com.example.Planetzecarbontracker;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class EcoGaugeFragement extends Fragment{
    private FirebaseAuth mAuth;
    private User user;
    private FirebaseDatabase db;
    private DatabaseReference userRef;
    private String[] categories = {"Transportation", "Food", "Shopping", "Energy"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.eco_gauge_fragment, container, false);

        // read from database
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        String UID = mAuth.getCurrentUser().getUid();
        db = FirebaseDatabase.getInstance();
        userRef = db.getReference("users").child(UID);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
            }
        });

        Spinner timeRange = view.findViewById(R.id.timeRange);
        TextView totalEmissions = view.findViewById(R.id.totalEmissions);
        PieChart emissionsBreakdown = view.findViewById(R.id.emmisionsBreakdown);
        LineChart emissionsTrend = view.findViewById(R.id.emmisionsTrendGraph);
        Button backButton = view.findViewById(R.id.backButton);
        TextView emissionsTrendText = view.findViewById(R.id.emissionsTrendText);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.date_range_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeRange.setAdapter(adapter);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        timeRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String range = parentView.getItemAtPosition(position).toString();

                double emissions = 0;
                String t = "";
                int type = 0;

                List<Emission> e = Collections.emptyList();
                if (range.equals("Daily")) {
                    type = 0;
                    e = user.getEcoTracker().getEmissionsByDate(year, month, day);
                    emissions = user.calculateTotalEmissionsByDateRange(e);
                    t = "today";
                    emissionsTrendText.setText("Emissions trend for past 7 days");
                }
                else if (range.equals("Monthly")) {
                    type = 1;
                    e = user.getEcoTracker().getEmissionsByMonth(year, month);
                    emissions = user.calculateTotalEmissionsByDateRange(e);
                    t = "this month";
                    emissionsTrendText.setText("Emissions trend this month");
                }
                else if (range.equals("Yearly")){
                    type = 2;
                    e = user.getEcoTracker().getEmissionsByYear(year);
                    emissions = user.calculateTotalEmissionsByDateRange(e);
                    t = "this year";
                    emissionsTrendText.setText("Emissions trend this year");
                }
                double[] emissionsByCategory = user.getEcoTracker().getTotalEmissionsByCategory(e);
                // pie chart
                PieDataSet pieDataSet = new PieDataSet(pieChartValues(emissionsByCategory), "");

                pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                pieDataSet.setValueTextSize(16f);

                // line chart

                // past 7 days
                double[] emissionsByWeek = new double[7];

                for(int i = 0; i < 7; i++){
                    Date today = new Date();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(today);

                    calendar.add(Calendar.DAY_OF_MONTH, i - 6);
                    emissionsByWeek[i] = user.calculateTotalEmissionsByDateRange(user.getEcoTracker().getEmissionsByDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)));
                }

                // this month
                int totalDaysThisMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                double[] emissionsThisMonth = new double[totalDaysThisMonth];

                for(int i = 0; i < totalDaysThisMonth; i++){
                    emissionsThisMonth[i] = user.calculateTotalEmissionsByDateRange(user.getEcoTracker().getEmissionsByDate(year, month, i + 1));
                }

                // this year
                double[] emissionsByMonth = new double[12];
                for(int i = 0; i < 12; i++){
                    emissionsByMonth[i] = user.calculateTotalEmissionsByDateRange(user.getEcoTracker().getEmissionsByMonth(year, i));
                }

                double[][] emissionLists = {emissionsByWeek, emissionsThisMonth, emissionsByMonth};

                LineDataSet lineDataSet = new LineDataSet(eBValues(emissionLists[type]), "Emissions in KG");
                lineDataSet.setColors(Color.parseColor("#009999"));
                lineDataSet.setValueTextSize(0f);

                LineData data = new LineData(lineDataSet);
                emissionsTrend.setData(data);
                emissionsTrend.getDescription().setEnabled(false);
                emissionsTrend.getXAxis().setTextSize(10f);
                emissionsTrend.getAxisLeft().setTextSize(14f);
                emissionsTrend.getAxisRight().setEnabled(false);
                Legend l = emissionsTrend.getLegend();
                l.setTextSize(15f);

                PieData pieData = new PieData(pieDataSet);
                emissionsBreakdown.setData(pieData);
                emissionsBreakdown.setEntryLabelColor(Color.BLACK);
                emissionsBreakdown.setEntryLabelTextSize(0f);
                emissionsBreakdown.getDescription().setEnabled(false);
                Legend l2 = emissionsBreakdown.getLegend();
                l2.setTextSize(15f);

                emissionsTrend.notifyDataSetChanged();
                emissionsTrend.invalidate();

                emissionsBreakdown.notifyDataSetChanged();
                emissionsBreakdown.invalidate();

                totalEmissions.setText("You’ve emitted " + emissions + " kg CO2e " + t);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new DashboardFragment());
            }
        });

        return view;
    }

    private ArrayList<Entry> eBValues(double[] emissions){
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        int i = 0;
        for(double emission : emissions){
            dataVals.add(new Entry(i+ 1, (float) emission));
            i += 1;
        }
        return dataVals;
    }

    private ArrayList<PieEntry> pieChartValues(double[] emissions){
        ArrayList<PieEntry> dataVals = new ArrayList<PieEntry>();
        for(int i = 0; i < emissions.length; i++){
            if(emissions[i] == 0) continue;
            dataVals.add(new PieEntry((float) emissions[i], categories[i]));
        }
        return dataVals;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
