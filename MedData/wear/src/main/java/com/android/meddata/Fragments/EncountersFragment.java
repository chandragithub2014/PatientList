package com.android.meddata.Fragments;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.meddata.JSONParser.JSONParser;
import com.android.meddata.R;
import com.android.meddata.interfaces.MyYAxisValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    //Json webService response
    HashMap<String,HashMap<String,Integer>> encounterYearlyMap= new HashMap<String,HashMap<String,Integer>>();
    List<String> years =  new ArrayList<String>();
    List<Integer> mpChartYAxis = new ArrayList<Integer>();
    Spinner yearlySpinner;
    LinearLayout barChartParent;
    //End
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

       // initializeEncounters();


       /* if(maxYAxisElement!=-1){
            calculateYAxisLable();
        }
        if(yAxisLableList.size()>0){
            calculateMPChartYaxis();
        }*/
    }
private void calculateMPChartYaxis(){
 //   Collections.sort(yAxisLableList);
  //  for(int i=0;i<encountersPermonth.size();i++){
        for(int i=0;i<  mpChartYAxis.size();i++){
 //       entries.add(new BarEntry(yAxisLableList.get(i), i));
        entries.add(new BarEntry(mpChartYAxis.get(i), i));

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
    xAxisLableList.add("Jan");
    xAxisLableList.add("Feb");
    xAxisLableList.add("Mar");
    xAxisLableList.add("Apr");
    xAxisLableList.add("May");
    xAxisLableList.add("Jun");
    xAxisLableList.add("Jul");
    xAxisLableList.add("Aug");
    xAxisLableList.add("Sep");
    xAxisLableList.add("Oct");
    xAxisLableList.add("Nov");
    xAxisLableList.add("Dec");
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

    private void initializePatientEncounters(){
        String response =   fetchEncounterResponseFromAssets();
        encounterYearlyMap =   JSONParser.getInstance().fetchEncounterYearlyResponse(response);
        if(encounterYearlyMap!=null && encounterYearlyMap.size()>0){
            populateYearKeys();
            initializeXAxisLable();
        //    printMap(encounterYearlyMap);
        }
    }

    private void populateYearKeys(){
        Log.d("Encounters", "populateYearKeys:::" + encounterYearlyMap.size());
         if(encounterYearlyMap!=null && encounterYearlyMap.size()>0){
             Iterator it = encounterYearlyMap.entrySet().iterator();
             while (it.hasNext()) {
                 Map.Entry pair = (Map.Entry)it.next();
                 Log.d("TAG", "Years:::"+(pair.getKey()));
                 years.add("" + pair.getKey());
                 Collections.sort(years);
             }
         }
    }


    private void calculateYaxisBasedonYear(String year){
        Log.d("Encounters", "calculateYaxisBasedonYear" + encounterYearlyMap.size());
        mpChartYAxis = new ArrayList<Integer>();
        HashMap<String ,Integer> yearlyHash = encounterYearlyMap.get(year);
     //   Log.d("Encounters","calculateYaxisBasedonYear"+yearlyHash.size());
        if(yearlyHash!=null && yearlyHash.size()>0){
            for(int i=0;i<xAxisLableList.size();i++){
                mpChartYAxis.add(yearlyHash.get(xAxisLableList.get(i)));
            }
        }

        calculateMPChartYaxis(mpChartYAxis);
        printYaxis();
    }


    private void printYaxis(){
        if(mpChartYAxis.size()>0){
            for(int i=0;i<mpChartYAxis.size();i++){
                Log.d("Encounters", "mpChartYAxis" + mpChartYAxis.get(i));
            }
        }
    }
    private void calculateMPChartYaxis(List<Integer> mpChartYAxis){
        entries = new ArrayList<>();
        //   Collections.sort(yAxisLableList);
        //  for(int i=0;i<encountersPermonth.size();i++){
        for(int i=0;i<  mpChartYAxis.size();i++){
            //       entries.add(new BarEntry(yAxisLableList.get(i), i));
            entries.add(new BarEntry(mpChartYAxis.get(i), i));

      /*  entries.add(new BarEntry(8f, 1));
        entries.add(new BarEntry(6f, 2));
        entries.add(new BarEntry(12f, 3));
        entries.add(new BarEntry(18f, 4));
        entries.add(new BarEntry(9f, 5));*/
        }
        dataset = new BarDataSet(entries, "# of Encounters");
        dataset.setColor(Color.parseColor("#f27a14"));
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
         barChartParent = (LinearLayout)v.findViewById(R.id.parent_layout);
        initializePatientEncounters();
         yearlySpinner = (Spinner)v.findViewById(R.id.encounter_years);
        if(years.size()>0){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, years);
            yearlySpinner.setAdapter(adapter);
        }
        yearlySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedYear = yearlySpinner.getSelectedItem().toString();
                Log.d("Encounters:::;", "Selected Year::::" + selectedYear);
                drawBarChart(selectedYear);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


   //     drawBarChart("2012");
   /*     String selectedYear = "2015";
        calculateYaxisBasedonYear(selectedYear);
        calculateMPChartYaxis();

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

        barChartParent.addView(chart);*/



        return v;
    }


private void drawBarChart(String Year){
/*if(barChartParent.getChildCount()>1){
    barChartParent.removeAllViews();
}*/
    String selectedYear = Year;
    calculateYaxisBasedonYear(selectedYear);
   // calculateMPChartYaxis();

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
    Log.d("Encounters", "Child Count:::" + barChartParent.getChildCount());
   // barChartParent.addView(chart);
    if(barChartParent.getChildCount()>1){
        barChartParent.removeViewAt(1);
        barChartParent.addView(chart,1);
    }else {
        barChartParent.addView(chart,1);
    }
}
    //

    public static void printMap(Map mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Log.d("TAG", (pair.getKey() + " = " + pair.getValue()));
            HashMap<String,Integer> monthlyHash =  (HashMap<String,Integer>) pair.getValue();
            Iterator mit = monthlyHash.entrySet().iterator();
            while(mit.hasNext()){
                Map.Entry mpair = (Map.Entry)mit.next();
           //     Log.d("Encounters", (mpair.getKey() + " = " + mpair.getValue()));
            }
            mit.remove();
            it.remove(); // avoids a ConcurrentModificationException
        }
    }
    private String fetchEncounterResponseFromAssets(){
        String result = "";
        AssetManager assetManager = getActivity().getAssets();
        InputStream inputStream = null;

        try{
            inputStream = assetManager.open("encounter_response");
            result = loadTextFile(inputStream);
          //  Toast.makeText(getActivity(),"Response:::"+result, Toast.LENGTH_LONG).show();
            Log.d("Encounters","Response::::"+result);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if (inputStream != null)
            try {
                inputStream.close();
            } catch (IOException e) {
                Log.d("Encounters", "Couldn't close file");
            }
        return result;
    }

    public String loadTextFile(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[4096];
        int len = 0;
        while ((len = inputStream.read(bytes)) > 0)
            byteStream.write(bytes, 0, len);
        return new String(byteStream.toByteArray(), "UTF8");
    }

    //
}

//https://github.com/PhilJay/MPAndroidChart/wiki/The-YAxisValueFormatter-interface
//https://github.com/PhilJay/MPAndroidChart/wiki/The-ValueFormatter-interface
//http://stackoverflow.com/questions/32077566/how-remove-zoom-feature-from-android-mpchart-library