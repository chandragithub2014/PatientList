package com.android.meddata.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.meddata.Application.MobileApplication;
import com.android.meddata.JSONParser.JSONParser;
import com.android.meddata.MedDataDTO.PhysicianDTO;
import com.android.meddata.MedDataUtils.MedDataConstants;
import com.android.meddata.MessageAPI.MessageService;
import com.android.meddata.R;
import com.android.meddata.custom.CustomToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 */
public class HandOffIndividualPatientFragment extends Fragment {


    View v = null;
    TextView patientName;
    Spinner prPhySpinner;
    Button post_save_btn;
    int pid = 1;
    int eid = 1;
    String selectedPhy="";
    public HandOffIndividualPatientFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Register the local broadcast receiver
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(messageReceiver, messageFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v  =  inflater.inflate(R.layout.fragment_hand_off_individual_patient, container, false);


        Toolbar mToolBar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        ImageView back_img = (ImageView)mToolBar.findViewById(R.id.back);
        back_img.setVisibility(View.VISIBLE);
        TextView save = (TextView)mToolBar.findViewById(R.id.notes);
        save.setVisibility(View.VISIBLE);
        save.setText("Save");
        patientName = (TextView)v.findViewById(R.id.patientName);
        prPhySpinner = (Spinner)v.findViewById(R.id.physician_spinner);
        post_save_btn = (Button)v.findViewById(R.id.post_handOff);
         if(getArguments().getInt("PATIENT_ID")!=-1){
             pid  = getArguments().getInt("PATIENT_ID");
         }
        if(getArguments().getInt("ENCOUNTER_ID")!=-1){
            eid = getArguments().getInt("ENCOUNTER_ID");
        }
        if(!TextUtils.isEmpty(getArguments().getString("PATIENT_NAME"))){
            patientName.setText(getArguments().getString("PATIENT_NAME"));
        }



        if (!TextUtils.isEmpty(MobileApplication.getInstance().getPhysicianList())) {
            List<PhysicianDTO> prPhyList = JSONParser.getInstance().getPhysicianList(MobileApplication.getInstance().getPhysicianList());
            List<String> phyList = new ArrayList<String>();
            if (prPhyList != null && prPhyList.size() > 0) {
                for (int i = 0; i < prPhyList.size(); i++) {
                    phyList.add(prPhyList.get(i).getPhyDesc());
                }
                if (phyList != null && phyList.size() > 0) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, phyList);
                    prPhySpinner.setAdapter(adapter);
                    //      primary_phy_spinner.setAdapter(adapter);
                   /* if(!TextUtils.isEmpty(tempWorkListDto.getPhysicianName())){
                        int spinnerPosition = adapter.getPosition(tempWorkListDto.getPhysicianName());
                        physicianSpinner.setSelection(spinnerPosition);
                    }*/
                }
            }
        }

        prPhySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPhy = parent.getItemAtPosition(position)
                        .toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postHandedOffPatient();
            }
        });
        return v;
    }

private void postHandedOffPatient(){
JSONObject handOffJSON = prepareHandsOffPatientJSON();
    if(handOffJSON!=null){
        Log.d("TAG", "Patient HandOff  JSON:::" + handOffJSON);
        MobileApplication.getInstance().setHandOffPatientJSON(""+handOffJSON);
        MessageService.getInstance().startMessageService(getActivity(), "handoffpatient");
    }
}
/*
 {
"request":
[
{"PatientID":"1",
"Login_Id":"veereshm",
"PrimaryPhysician":"SueChilson",
"EncounterId":"352",
  "Key":"8C3A8A<k"
}
]


 */
    private JSONObject prepareHandsOffPatientJSON(){
        String globalJSON = MobileApplication.getInstance().getPropertiesJSON();
        String key = "";
        try {
            JSONObject jsonObject = new JSONObject(globalJSON);
            key = jsonObject.getString("Key");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        JSONArray handOffJSONArray = new JSONArray();
        JSONObject updateJSON = null;
        JSONObject    requestJsonObject = null;
        try {
            updateJSON = new JSONObject();


            updateJSON.put("PatientID",pid);
            updateJSON.put("Login_Id", MedDataConstants.LOGIN_ID);
            updateJSON.put("EncounterId", eid);


            if(!TextUtils.isEmpty(selectedPhy)){
                updateJSON.put("PrimaryPhysician", selectedPhy);
            }else {
                updateJSON.put("PrimaryPhysician", "Tyy Dr"); //Sue Chilson
            }

            updateJSON.put("Key",key);

         /*   if(clickedLocationId==0){
                updateJSON.put("LocationId", locationId);
            }else {
                updateJSON.put("LocationId", "" + clickedLocationId);
            }*/

            handOffJSONArray.put(updateJSON);
            requestJsonObject = new JSONObject();
            requestJsonObject.put("request", handOffJSONArray);

        }catch (JSONException e){
            e.printStackTrace();
        }


        return requestJsonObject;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //  String message = intent.getStringExtra("message");
            //Log.v("myTag", "Main activity received message: " + message);

            try {
                //
                if (intent.hasExtra("result")) {
                    String result_data = intent.getStringExtra("result");
                    if (result_data.equalsIgnoreCase("handoffpatient")) {
                        Log.d("TAG", "handoffpatient Response::::" + MobileApplication.getInstance().getPatientHandOffResponse());
                      //  Toast.makeText(getActivity(), MobileApplication.getInstance().getPatientHandOffResponse(), Toast.LENGTH_SHORT).show();
                        new CustomToast(getActivity(),getActivity()).displayToast(MobileApplication.getInstance().getPatientHandOffResponse());
                        //Toast.makeText(getActivity(), MobileApplication.getInstance().getHandsOffSearchResponse(), Toast.LENGTH_SHORT).show();
                        //      Toast.makeText(getActivity(),MobileApplication.getInstance().getBulkUpdateResponse(),Toast.LENGTH_LONG).show();

                    }

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


}
