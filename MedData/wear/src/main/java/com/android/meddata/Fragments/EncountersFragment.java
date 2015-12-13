package com.android.meddata.Fragments;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.meddata.R;
import com.android.meddata.interfaces.MyYAxisValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EncountersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EncountersFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    HashMap<String,List<Integer>> encounterHash = new HashMap<String,List<Integer>>();
    List<String> xAxisLableList = new ArrayList<String>();
    int maxYAxisElement = -1;
    List<Integer> yAxisLableList = new ArrayList<Integer>();

    //MPChart YaxisEntry;
    ArrayList<BarEntry> entries = new ArrayList<>();
    BarDataSet dataset;
    List<Integer> encountersPermonth;
    int mContainerId = -1;

    public EncountersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EncountersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EncountersFragment newInstance(String param1, String param2) {
        EncountersFragment fragment = new EncountersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        initializeEncounters();
        initializeXAxisLable();
        if(maxYAxisElement!=-1){
            calculateYAxisLable();
        }
        if(yAxisLableList.size()>0){
            calculateMPChartYaxis();
        }
    }
private void calculateMPChartYaxis(){
    Collections.sort(yAxisLableList);
    for(int i=0;i<encountersPermonth.size();i++){
 //       entries.add(new BarEntry(yAxisLableList.get(i), i));
        entries.add(new BarEntry(encountersPermonth.get(i), i));

      /*  entries.add(new BarEntry(8f, 1));
        entries.add(new BarEntry(6f, 2));
        entries.add(new BarEntry(12f, 3));
        entries.add(new BarEntry(18f, 4));
        entries.add(new BarEntry(9f, 5));*/
    }
     dataset = new BarDataSet(entries, "# of Encounters");
    dataset.setColor(Color.parseColor("#f27a14"));
}
    private void calculateYAxisLable(){
        yAxisLableList.add(0);
        int temp  = 0;
        int maxYaxis = maxYAxisElement;
        int noOfParts = maxYaxis/5;
        for(int i=0;i<noOfParts;i++){
            temp = temp+noOfParts;
            yAxisLableList.add(temp);

        }
        for(int i = 0  ; i<yAxisLableList.size();i++){
            Log.d("Encounters", "Yaxis Values::" + yAxisLableList.get(i));
        }
    }
private void initializeXAxisLable(){
    xAxisLableList.add("jan");
    xAxisLableList.add("feb");
    xAxisLableList.add("mar");
    xAxisLableList.add("apr");
    xAxisLableList.add("may");
    xAxisLableList.add("jun");
    xAxisLableList.add("jul");
    xAxisLableList.add("aug");
    xAxisLableList.add("sep");
    xAxisLableList.add("oct");
    xAxisLableList.add("nov");
    xAxisLableList.add("dec");
}
    private void initializeEncounters(){
        encountersPermonth = new ArrayList<Integer>();
        encountersPermonth.add(4);
        encountersPermonth.add(10);
        encountersPermonth.add(3);
        encountersPermonth.add(18);
        encountersPermonth.add(5);
        encountersPermonth.add(10);
        encountersPermonth.add(4);
        encountersPermonth.add(15);
        encountersPermonth.add(0);
        encountersPermonth.add(0);
        encountersPermonth.add(0);
        encountersPermonth.add(20);
        encounterHash.put("2015", encountersPermonth);
       // Collections.sort(encountersPermonth);
         maxYAxisElement =  encountersPermonth.get(encountersPermonth.size()-1);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  v =  inflater.inflate(R.layout.fragment_encounters, container, false);
        mContainerId = container.getId();
        Toolbar mToolBar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        ImageView back_img = (ImageView)mToolBar.findViewById(R.id.back);
        back_img.setVisibility(View.VISIBLE);
        TextView toolbarTitle = (TextView)mToolBar.findViewById(R.id.title);
        toolbarTitle.setText("Encounters");

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(mContainerId, new DashBoardWearableListFragment()).addToBackStack(null).commit();
            }
        });

        FrameLayout barChartContainer = (FrameLayout)v.findViewById(R.id.barChart_container);
        BarChart chart = new BarChart(getActivity());
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawGridLines(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setValueFormatter(new MyYAxisValueFormatter());

        chart.getLegend().setEnabled(true);

        //chart.setDrawValuesForWholeStack(false);
        chart.setMaxVisibleValueCount(0);
        BarData data = new BarData(xAxisLableList, dataset);
        chart.setData(data);
        chart.setDescription("");


       // chart.setDrawValueAboveBar(false);
       // chart.set
        chart.animateXY(2000, 2000);

        barChartContainer.addView(chart);
        return v;
    }

}

//https://github.com/PhilJay/MPAndroidChart/wiki/The-YAxisValueFormatter-interface
//https://github.com/PhilJay/MPAndroidChart/wiki/The-ValueFormatter-interface
//http://stackoverflow.com/questions/32077566/how-remove-zoom-feature-from-android-mpchart-library