package com.example.b07demosummer2024;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class EcoGaugeFragement extends Fragment{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.eco_gauge_fragment, container, false);

        Spinner timeRange = view.findViewById(R.id.timeRange);
//        BarChart emmisionsBreakdown = view.findViewById(R.id.emmisionsBreakdown);
        LineChart emmisionsTrend = (LineChart) view.findViewById(R.id.emmisionsTrendGraph);

        LineDataSet lineDataSet = new LineDataSet(eBValues(), "emmisionsTrend");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData data = new LineData(dataSets);
        emmisionsTrend.setData(data);
        emmisionsTrend.invalidate();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.date_range_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeRange.setAdapter(adapter);

        return view;
    }

    private ArrayList<Entry> eBValues(){
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        for(int i = 0; i < 5; i++){
            dataVals.add(new Entry(i, i + 1));
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
