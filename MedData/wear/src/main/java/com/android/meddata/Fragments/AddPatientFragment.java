package com.android.meddata.Fragments;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.meddata.Application.MobileApplication;
import com.android.meddata.JSONParser.JSONParser;
import com.android.meddata.MedDataDTO.LocationDTO;
import com.android.meddata.MedDataUtils.MedDataConstants;
import com.android.meddata.MedDataUtils.MedDataUtil;
import com.android.meddata.MessageAPI.MessageService;
import com.android.meddata.R;
import com.android.meddata.custom.CustomToast;
import com.android.meddata.interfaces.SendDataDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPatientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPatientFragment extends Fragment implements View.OnClickListener,SendDataDialogListener,AdapterView.OnItemSelectedListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Spinner locSpinner,billingSpinner,dispositionSpinner,genderSpinner,notesTypeSpinner,secPhySpinner;
    View addPatientView = null;

    int mContainerId = -1;
    EditText encounterDate,dateOfBirth;

//dob_patient

    public AddPatientFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddPatientFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddPatientFragment newInstance(String param1, String param2) {
        AddPatientFragment fragment = new AddPatientFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.hasExtra("patientUpdate")){
                    String result_data = intent.getStringExtra("patientUpdate");
                    Log.d("TAG","Result Data:::"+result_data);
                    new CustomToast(getActivity(),getActivity()).displayToast(result_data);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(messageReceiver, messageFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        addPatientView =  inflater.inflate(R.layout.add_patient_layout, container, false);
        mContainerId = container.getId();
        initViews(addPatientView);
        populateViews();
        Toolbar mToolBar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        ImageView back_img = (ImageView)mToolBar.findViewById(R.id.back);
        back_img.setVisibility(View.VISIBLE);
        TextView toolbarTitle = (TextView)mToolBar.findViewById(R.id.title);
        toolbarTitle.setText("Work List");
        TextView notesView = (TextView)mToolBar.findViewById(R.id.notes);
        notesView.setVisibility(View.VISIBLE);
        notesView.setText("Save");
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(mContainerId, new DashBoardWearableListFragment()).addToBackStack(null).commit();
            }
        });

        notesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              JSONObject postJSON =   formPostJSON();
                MobileApplication.getInstance().setUpdatePatientDetails(""+postJSON);
                MessageService.getInstance().startMessageService(getActivity(), "patientUpdate");
            }
        });
        return addPatientView;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.encounter_date:
                FragmentManager fm = getFragmentManager();
                DatePickerDialogFragment dialogFragment = new DatePickerDialogFragment ();
                Bundle args = new Bundle();
                SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                Date dt = new Date();
                args.putString("date", formatter.format(dt));
                args.putSerializable("type","encDate");
                dialogFragment.setArguments(args);
                dialogFragment.setTargetFragment(this, 0);
                dialogFragment.show(fm, "Date Dialog Fragment");
                break;

            case R.id.dob_patient:
                FragmentManager fmm = getFragmentManager();
                DatePickerDialogFragment dialogFragmentt = new DatePickerDialogFragment ();
                Bundle argss = new Bundle();
                SimpleDateFormat formatterr = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                Date dtt = new Date();
                argss.putString("date", formatterr.format(dtt));
                argss.putSerializable("type","dob");
                dialogFragmentt.setArguments(argss);
                dialogFragmentt.setTargetFragment(this, 0);
                dialogFragmentt.show(fmm, "Date Dialog Fragment");
                break;
        }
    }


    @Override
    public void onFinishDialog(String inputText,String type) {
        if(!TextUtils.isEmpty(inputText)) {
            //Oct 1,2015
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "MMM dd, yyyy", Locale.ENGLISH);
            // Date myDate;
            try {
                Date myDate = dateFormat.parse(inputText);
                if(type.equalsIgnoreCase("encDate")) {
                    if (encounterDate != null) {
                        encounterDate.setText(dateFormat.format(myDate));
                    }
                }
               if(type.equalsIgnoreCase("dob")){
                    if (dateOfBirth != null) {
                        dateOfBirth.setText(dateFormat.format(myDate));
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
    }

    private JSONObject formPostJSON(){
        String globalJSON = MobileApplication.getInstance().getPropertiesJSON();
        String key = "";
        try {
            JSONObject jsonObject = new JSONObject(globalJSON);
            key = jsonObject.getString("Key");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        JSONObject updateJSON = null;
        JSONObject    requestJsonObject = null;
        try {
            updateJSON = new JSONObject();
            updateJSON.put("Login_Id", MedDataConstants.LOGIN_ID);
            updateJSON.put("Key",key);
            if(!TextUtils.isEmpty(encounterDate.getText().toString())){
                String jsonFormatDate = MedDataUtil.getInstance().getJsonDateFormat(encounterDate.getText().toString());
                updateJSON.put("Encounter_Date",jsonFormatDate);
            }
            if(!TextUtils.isEmpty(selectedLocation)){
                updateJSON.put("LocationId",selectedLocation);
            }

            updateJSON.put("FirstName","AB"); // TODO: 12/8/2015
            updateJSON.put("MiddleName","DE");// TODO: 12/8/2015
            updateJSON.put("LastName","AMLA");// TODO: 12/8/2015
            updateJSON.put("MedicalRecordNo", mrnNum.getText().toString());
            if(!TextUtils.isEmpty(selectedSecPhy)) {
                updateJSON.put("SecondaryPhysician", selectedSecPhy);
            }
            updateJSON.put("PrimaryPhysician", "Test Physician"); // TODO: 12/8/2015
            if(!TextUtils.isEmpty(dateOfBirth.getText().toString())){
                String jsonFormatDate = MedDataUtil.getInstance().getJsonDateFormat(dateOfBirth.getText().toString());
                updateJSON.put("DOB", jsonFormatDate);
                int age = MedDataUtil.getInstance().getAge(new Date(dateOfBirth.getText().toString()));
                updateJSON.put("Age",""+age);
            }
           if(!TextUtils.isEmpty(adminNum.getText().toString())){
                updateJSON.put("AdmissionNo",adminNum.getText().toString());
            }
            if(!TextUtils.isEmpty(finanNum.getText().toString())){
                updateJSON.put("FinancialNo",finanNum.getText().toString());
            }

            if(!TextUtils.isEmpty(patient_notes.getText().toString())){
                updateJSON.put("Notes",patient_notes.getText().toString());
            }
            if(!TextUtils.isEmpty(selectedDisposition)){
                updateJSON.put("DispositionId",selectedDisposition);
            }
            updateJSON.put("TransactionType","AN");
            updateJSON.put("ImageRef1","");
            updateJSON.put("ImageRef2","");
            updateJSON.put("ImageRef2","");

            if(!TextUtils.isEmpty(selectedNotesType)){
                updateJSON.put("NotesType",selectedNotesType);
            }
            if(!TextUtils.isEmpty(selectedBilling)){
                updateJSON.put("BillingType",selectedBilling);
            }
            if(!TextUtils.isEmpty(selectedGender)){
                updateJSON.put("Gender",selectedGender);
            }
            if(!TextUtils.isEmpty(roomNum.getText().toString())){
                updateJSON.put("RoomNumber",roomNum.getText().toString());
            }


/*

 private String selectedLocation="";
    private String selectedBilling="";
    private String selectedDisposition="";
    private String selectedGender="";
    private String selectedNotesType="";
    private String selectedSecPhy="";
 */


            requestJsonObject = new JSONObject();
            requestJsonObject.put("request", updateJSON);

        }catch (JSONException e){
            e.printStackTrace();
        }


        return requestJsonObject;
    }
    EditText mrnNum,adminNum,finanNum,roomNum;
    EditText patient_notes;
    private void initViews(View v){
//locSpinner,billingSpinner,dispositionSpinner,genderSpinner,notesTypeSpinner,secPhySpinner
        locSpinner = (Spinner)v.findViewById(R.id.loc_spinner);
        locSpinner.setOnItemSelectedListener(this);
        billingSpinner = (Spinner)v.findViewById(R.id.billing_spinner);
        billingSpinner.setOnItemSelectedListener(this);
        dispositionSpinner = (Spinner)v.findViewById(R.id.disposition_spinner);
        dispositionSpinner.setOnItemSelectedListener(this);
        genderSpinner =  (Spinner)v.findViewById(R.id.gender_spinner);
        genderSpinner.setOnItemSelectedListener(this);
        notesTypeSpinner =  (Spinner)v.findViewById(R.id.notes_type_spinner);
        notesTypeSpinner.setOnItemSelectedListener(this);
        secPhySpinner =  (Spinner)v.findViewById(R.id.sec_phy_spinner);
        secPhySpinner.setOnItemSelectedListener(this);
        encounterDate = (EditText)v.findViewById(R.id.encounter_date);
        encounterDate.setOnClickListener(this);
        dateOfBirth = (EditText)v.findViewById(R.id.dob_patient);
        dateOfBirth.setOnClickListener(this);
        mrnNum = (EditText)v.findViewById(R.id.mrn_num);
        adminNum = (EditText)v.findViewById(R.id.admin_num);
        finanNum = (EditText)v.findViewById(R.id.finan_num);
        patient_notes = (EditText)v.findViewById(R.id.notes_patient);
        roomNum = (EditText)v.findViewById(R.id.room_num);
    }

    private void populateViews(){
        //Location
        if(!TextUtils.isEmpty(MobileApplication.getInstance().getLocationList())){
            List<LocationDTO> locList = JSONParser.getInstance().getLocationList(MobileApplication.getInstance().getLocationList());
            List<String> hospitalList = new ArrayList<String>();
            hospitalList.add("Select Location");
            if(locList!=null && locList.size()>0){
                for(int i=0;i<locList.size();i++){
                    hospitalList.add(locList.get(i).getListDesc());
                }
                if(hospitalList!=null && hospitalList.size()>0) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, hospitalList);
                    locSpinner.setAdapter(adapter);
                    locSpinner.setSelection(0);

                }
            }
        }

        //Billing Details

        if(!TextUtils.isEmpty(MobileApplication.getInstance().getBillingList())){
            List<LocationDTO> locList = JSONParser.getInstance().getLocationList(MobileApplication.getInstance().getBillingList());
            List<String> hospitalList = new ArrayList<String>();
            hospitalList.add("Select Billing");
            if(locList!=null && locList.size()>0){
                for(int i=0;i<locList.size();i++){
                    hospitalList.add(locList.get(i).getListDesc());
                }
                if(hospitalList!=null && hospitalList.size()>0) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, hospitalList);
                    billingSpinner.setAdapter(adapter);
                    billingSpinner.setSelection(0);

                }
            }
        }

        //Disposition Details

        if(!TextUtils.isEmpty(MobileApplication.getInstance().getDispositionList())){
            List<LocationDTO> locList = JSONParser.getInstance().getLocationList(MobileApplication.getInstance().getDispositionList());
            List<String> hospitalList = new ArrayList<String>();
            hospitalList.add("Select Disposition");
            if(locList!=null && locList.size()>0){
                for(int i=0;i<locList.size();i++){
                    hospitalList.add(locList.get(i).getListDesc());
                }
                if(hospitalList!=null && hospitalList.size()>0) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, hospitalList);
                    dispositionSpinner.setAdapter(adapter);
                    dispositionSpinner.setSelection(0);

                }
            }
        }

        //genderSpinner
        if(!TextUtils.isEmpty(MobileApplication.getInstance().getGender())){
            List<LocationDTO> locList = JSONParser.getInstance().getLocationList(MobileApplication.getInstance().getGender());
            List<String> hospitalList = new ArrayList<String>();
            hospitalList.add("Select Gender");
            if(locList!=null && locList.size()>0){
                for(int i=0;i<locList.size();i++){
                    hospitalList.add(locList.get(i).getListDesc());
                }
                if(hospitalList!=null && hospitalList.size()>0) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, hospitalList);
                    genderSpinner.setAdapter(adapter);
                    genderSpinner.setSelection(0);

                }
            }
        }



        //secPhySpinner
        if(!TextUtils.isEmpty(MobileApplication.getInstance().getPhysicianList())){
            List<LocationDTO> locList = JSONParser.getInstance().getLocationList(MobileApplication.getInstance().getPhysicianList());
            List<String> hospitalList = new ArrayList<String>();
            hospitalList.add("Select Secondary Physician");
            if(locList!=null && locList.size()>0){
                for(int i=0;i<locList.size();i++){
                    hospitalList.add(locList.get(i).getListDesc());
                }
                if(hospitalList!=null && hospitalList.size()>0) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, hospitalList);
                    secPhySpinner.setAdapter(adapter);
                    secPhySpinner.setSelection(0);

                }
            }
        }



        //secPhySpinner
        if(!TextUtils.isEmpty(MobileApplication.getInstance().getNotesType())){
            List<LocationDTO> locList = JSONParser.getInstance().getLocationList(MobileApplication.getInstance().getNotesType());
            List<String> hospitalList = new ArrayList<String>();
            hospitalList.add("Select Notes Type");
            if(locList!=null && locList.size()>0){
                for(int i=0;i<locList.size();i++){
                    hospitalList.add(locList.get(i).getListDesc());
                }
                if(hospitalList!=null && hospitalList.size()>0) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, hospitalList);
                    notesTypeSpinner.setAdapter(adapter);
                    notesTypeSpinner.setSelection(0);

                }
            }
        }
    }
    private String selectedLocation="";
    private String selectedBilling="";
    private String selectedDisposition="";
    private String selectedGender="";
    private String selectedNotesType="";
    private String selectedSecPhy="";

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if(position!= 0) {
            if (spinner.getId() == R.id.loc_spinner) {
                selectedLocation = spinner.getSelectedItem().toString();
            } else if (spinner.getId() == R.id.billing_spinner) {
                selectedBilling = spinner.getSelectedItem().toString();
            } else if (spinner.getId() == R.id.disposition_spinner) {
                selectedDisposition =  spinner.getSelectedItem().toString();
            } else if (spinner.getId() == R.id.gender_spinner) {
                selectedGender =   spinner.getSelectedItem().toString();
            } else if (spinner.getId() == R.id.notes_type_spinner) {
                selectedNotesType = spinner.getSelectedItem().toString();
            } else if (spinner.getId() == R.id.sec_phy_spinner) {
                selectedSecPhy =  spinner.getSelectedItem().toString();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
