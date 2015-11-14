package com.android.meddata.Fragments;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.meddata.MedDataDTO.WorkListDTO;
import com.android.meddata.R;
import com.android.meddata.custom.CustomSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HandOffSearchResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HandOffSearchResultFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view = null;
    int mContainerId = -1;
    public TableLayout.LayoutParams tableRowParams = null;
    ImageButton save_fab;
    HashMap<Integer,String> spinnerStateHash;
    HashMap<Integer,WorkListDTO> patientListDTO;
//  final  TableRow tableDataRow=null;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HandOffSearchResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HandOffSearchResultFragment newInstance(String param1, String param2) {
        HandOffSearchResultFragment fragment = new HandOffSearchResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public HandOffSearchResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view  =  inflater.inflate(R.layout.handoff_search_result, container, false);
        mContainerId = container.getId();
        save_fab = (ImageButton)view.findViewById(R.id.fab_save);
        TableLayout tableLayout = (TableLayout)view.findViewById(R.id.tableLayout1);
        TableRow tableRow = new TableRow(getActivity());
        tableRow.setGravity(Gravity.CENTER_VERTICAL);
        tableRow.setTag("header");
        final LayoutInflater header_inflator = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout header_layout = (LinearLayout)header_inflator.inflate(R.layout.table_header_layout,null);
      //  b = (Button) inflater.inflate(R.layout.buttons, null);
        tableRow.addView(header_layout);
        tableRowParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
        tableRowParams.setMargins(1, 0, 1, 1);
        tableRow.setLayoutParams(tableRowParams);
        tableLayout.addView(tableRow);
        spinnerStateHash = new HashMap<Integer,String>();
        //table data rows
        for(int i=0;i<3;i++){
            final  TableRow  tableDataRow = new TableRow(getActivity());
            tableDataRow.setGravity(Gravity.CENTER_VERTICAL);
            tableDataRow.setTag(i);
            final LayoutInflater data_inflator = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout data_layout = (LinearLayout)data_inflator.inflate(R.layout.table_data_layout, null);
            LinearLayout leftLayout = (LinearLayout)data_layout.findViewById(R.id.left);

            CustomSpinner customSpinner = (CustomSpinner)data_layout.findViewById(R.id.right);
            List<String> customList = new ArrayList<String>();
            customList.add("One");
            customList.add("Two");
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                    getActivity(),
                    android.R.layout.simple_spinner_dropdown_item,
                    customList);
            spinnerArrayAdapter
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            customSpinner.setPrompt("Select");
            customSpinner.setAdapter(spinnerArrayAdapter);
            tableDataRow.addView(data_layout);
            tableRowParams = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT);
            tableRowParams.setMargins(1, 0, 1, 1);
            tableDataRow.setLayoutParams(tableRowParams);
            tableLayout.addView(tableDataRow);
            customSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("TAG", "Select Item " + parent.getItemAtPosition(position).toString() + "  " +tableDataRow.getTag());
                    spinnerStateHash.put((Integer)tableDataRow.getTag(),parent.getItemAtPosition(position).toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        save_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//compare with DTO hashMap with spinner HashMAP and post based on TAG
            }
        });

      ImageButton  back_float = (ImageButton)view.findViewById(R.id.fab_back);
        back_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(mContainerId, MyHandoffPatientFragment.newInstance("","")).addToBackStack(null).commit();
            }
        });
       // tableLayout.add
      /*  LinearLayout llayout = (LinearLayout)view.findViewById(R.id.header_data_layout);
        CustomSpinner customSpinner = (CustomSpinner)llayout.findViewById(R.id.right);
        List<String> customList = new ArrayList<String>();
        customList.add("One");
        customList.add("Two");
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                customList);
        spinnerArrayAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customSpinner.setPrompt("Select");
        customSpinner.setAdapter(spinnerArrayAdapter);*/
        return view;
    }




}
