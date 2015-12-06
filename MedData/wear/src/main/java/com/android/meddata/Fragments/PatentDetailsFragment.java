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
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.LinearLayout;
    import android.widget.RelativeLayout;
    import android.widget.Spinner;
    import android.widget.TextView;

    import com.android.meddata.Application.MobileApplication;
    import com.android.meddata.JSONParser.JSONParser;
    import com.android.meddata.MedDataDTO.LocationDTO;
    import com.android.meddata.MedDataDTO.PhysicianDTO;
    import com.android.meddata.MedDataDTO.WorkListDTO;
    import com.android.meddata.MedDataUtils.MedDataConstants;
    import com.android.meddata.MessageAPI.MessageService;
    import com.android.meddata.R;
    import com.android.meddata.custom.CustomToast;

    import org.json.JSONException;
    import org.json.JSONObject;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;

    /**
     * A simple {@link Fragment} subclass.
     * Use the {@link PatentDetailsFragment#newInstance} factory method to
     * create an instance of this fragment.
     */
    public class PatentDetailsFragment extends Fragment{
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private static final String ARG_PARAM1 = "param1";
        private static final String ARG_PARAM2 = "param2";

        // TODO: Rename and change types of parameters
        private String encounterID;
        private String mParam2;


        ImageView phyImg;
        View v =null;
        EditText mandatory_name,mandatory_email;
        int mContainerId = -1;


        TextView patientName,patientRoom,encounterDate,location,mrn,age,dob,admissionno,finno;
        LinearLayout nameLayout,roomLayout,encounterDateLayout,mrnLayout,ageLayout,dobLayout,adminLayout,finanLayout;
        RelativeLayout location_spinner_layout,pr_phy_spinner_layout,billing_spinner_layout,dispoistion_spinner_layout,gender_spinner_layout,notes_category_spinner_layout,second_phy_spinner_layout;
        RelativeLayout dateLayout,admission_layout,finance_layout;
        private WorkListDTO tempWorkListDto;
        TextView saveDetails,cancelDetails;

        String selectedLocation = "";
        String selectedPhysician="";
        String selectedDisposition = "" ;


        Spinner locationSpinner;
        Spinner physicianSpinner;
        Spinner dispSpinner;
        Spinner billingSpinner;

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PatentDetailsFragment.
         */
        // TODO: Rename and change types and number of parameters
        public static PatentDetailsFragment newInstance(String param1, String param2) {
            PatentDetailsFragment fragment = new PatentDetailsFragment();
            Bundle args = new Bundle();
            args.putString(ARG_PARAM1, param1);
            args.putString(ARG_PARAM2, param2);
            fragment.setArguments(args);
            return fragment;
        }

        public PatentDetailsFragment() {
            // Required empty public constructor
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                encounterID = getArguments().getString(ARG_PARAM1);
                mParam2 = getArguments().getString(ARG_PARAM2);
            }
            IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
            MessageReceiver messageReceiver = new MessageReceiver();
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(messageReceiver, messageFilter);
        }


        public class MessageReceiver extends BroadcastReceiver {
            @Override
            public void onReceive(Context context, Intent intent) {
                //  String message = intent.getStringExtra("message");
                //Log.v("myTag", "Main activity received message: " + message);

                try {
                    //
                    if (intent.hasExtra("notes")) {
                        String result_data = intent.getStringExtra("notes");
                        if (result_data.equalsIgnoreCase("patientNotes")) {
                            Log.d("TAG", "Notes Response::::" + MobileApplication.getInstance().getPatientNotes());


                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(mContainerId, new PatientNotesListFragment()).addToBackStack(null).commit();
                           /*

                           [{
        "EncounterID": 0,
        "Encounters": 0,
        "EntityID": 0,
        "Flag": null,
        "Key": null,
        "LocationId": null,
        "Login_Id": null,
        "Month": null,
        "Notes": "cosign note",
        "NotesType": "Cosign Order",
        "PatientsList": null,
        "Physician": "Phy442",
        "UpdatedDate": "Dec 01, 2015",
        "Year": 0
    }]
                            */

                           // new CustomToast(getActivity(),getActivity()).displayToast(MobileApplication.getInstance().getAccount_update_response());
                            //    Toast.makeText(getActivity(), MobileApplication.getInstance().getAccount_update_response(), Toast.LENGTH_SHORT).show();
                            //      Toast.makeText(getActivity(),MobileApplication.getInstance().getBulkUpdateResponse(),Toast.LENGTH_LONG).show();
                        }

                    }
                    if (intent.hasExtra("patientUpdate")){
                        String result_data = intent.getStringExtra("patientUpdate");
                      new CustomToast(getActivity(),getActivity()).displayToast(result_data);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }




        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            v = inflater.inflate(R.layout.worklist_details1, container, false);
            mContainerId = container.getId();
            if(!TextUtils.isEmpty(encounterID)){
                tempWorkListDto = JSONParser.getInstance().getPatientDetail(encounterID);
            }
            if(tempWorkListDto!=null) {
                initAndPopulateLabels(v);
            }else{
                initAndDisableUnwantedViews(v);

            }
            Toolbar mToolBar = (Toolbar)getActivity().findViewById(R.id.toolbar);
            TextView toolbarTitle = (TextView)mToolBar.findViewById(R.id.title);
            toolbarTitle.setText("Patient Details");
            ImageView disposition_btn = (ImageView)mToolBar.findViewById(R.id.right_icon);
            disposition_btn.setVisibility(View.GONE);
        /*    disposition_btn.setImageResource(R.drawable.selectall);*/
            disposition_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TAG", "Clicked Notes::::");
                }
            });

            TextView notesView = (TextView)mToolBar.findViewById(R.id.notes);
            notesView.setVisibility(View.VISIBLE);
            notesView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d("TAG", "Clicked Notes::::");

                    JSONObject patientNotesJSON = preparePatientNotesJSON();
                    if(patientNotesJSON!=null){
                        Log.d("TAG", "patientNotesJSON:::" + patientNotesJSON);
                        MobileApplication.getInstance().setPatientNotes("" + patientNotesJSON);
                        MessageService.getInstance().startMessageService(getActivity(), "notes");
                    }
                }
            });
          /*  TextView rightToolbarTitle = (TextView)mToolBar.findViewById(R.id.right_text);
            rightToolbarTitle.setVisibility(View.VISIBLE);*/
           // toolbarTitle.setText("Patient Details");
            saveDetails = (TextView)v.findViewById(R.id.save_details);
            cancelDetails = (TextView)v.findViewById(R.id.cancel_details);
            saveDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 JSONObject updatedJSON  = JSONParser.getInstance().getPatientUpdateJSON(encounterID,selectedDisposition,selectedLocation,selectedPhysician);
                 MobileApplication.getInstance().setUpdatePatientDetails(""+updatedJSON);
                  MessageService.getInstance().startMessageService(getActivity(), "patientUpdate");
                }
            });

            cancelDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            return  v;
        }

    private void initAndPopulateLabels(View v ){


        patientName = (TextView)v.findViewById(R.id.patientName);
        patientName.setText(tempWorkListDto.getPatientName());



        patientRoom = (TextView)v.findViewById(R.id.roomNo);
        patientRoom.setText(tempWorkListDto.getRoomNum());



        TextView encounterDateVal = (TextView)v.findViewById(R.id.patientDate);
        encounterDateVal.setText(tempWorkListDto.getDate());

        location_spinner_layout = (RelativeLayout)v.findViewById(R.id.hospital_row);
        pr_phy_spinner_layout =   (RelativeLayout)v.findViewById(R.id.physician_row);
        billing_spinner_layout = (RelativeLayout)v.findViewById(R.id.billing_layout);

        dispoistion_spinner_layout = (RelativeLayout)v.findViewById(R.id.disposition_layout);
        gender_spinner_layout = (RelativeLayout)v.findViewById(R.id.gender_layout);
        notes_category_spinner_layout = (RelativeLayout)v.findViewById(R.id.notes_category_layout);
        second_phy_spinner_layout = (RelativeLayout)v.findViewById(R.id.second_physician_layout);
        dateLayout = (RelativeLayout)v.findViewById(R.id.date_layout);
        admission_layout = (RelativeLayout)v.findViewById(R.id.admission_layout);
        finance_layout = (RelativeLayout)v.findViewById(R.id.financial_no_layout);

        dispoistion_spinner_layout.setVisibility(View.GONE);
        notes_category_spinner_layout.setVisibility(View.GONE);
        second_phy_spinner_layout.setVisibility(View.GONE);
        admission_layout .setVisibility(View.GONE);
        dateLayout.setVisibility(View.GONE);
        finance_layout.setVisibility(View.GONE);
        gender_spinner_layout.setVisibility(View.GONE);

        ((TextView)location_spinner_layout.findViewById(R.id.spinner_label)).setVisibility(View.GONE);//.setText("Location");
        ((TextView)pr_phy_spinner_layout.findViewById(R.id.spinner_label)).setVisibility(View.GONE);//.setText("Primary Physician");
        ((TextView)billing_spinner_layout.findViewById(R.id.spinner_label)).setVisibility(View.GONE);//.setText("Billing Method");
        ((TextView)dispoistion_spinner_layout.findViewById(R.id.spinner_label)).setVisibility(View.GONE);//.setText("Set Disposition");
        ((TextView)gender_spinner_layout.findViewById(R.id.spinner_label)).setVisibility(View.GONE);//.setText("Gender");
        ((TextView)notes_category_spinner_layout.findViewById(R.id.spinner_label)).setVisibility(View.GONE);//.setText("Notes Category");
        ((TextView)second_phy_spinner_layout.findViewById(R.id.spinner_label)).setVisibility(View.GONE);//.setText("2nd Physician");

        ((TextView)dateLayout.findViewById(R.id.mandatory_label)).setVisibility(View.GONE);//.setText("Date");
        ((TextView)admission_layout.findViewById(R.id.mandatory_label)).setVisibility(View.GONE);//.setText("Admission");
        ((TextView)finance_layout.findViewById(R.id.mandatory_label)).setVisibility(View.GONE);//.setText("Finance No");

        /*mrnLayout =  (LinearLayout)v.findViewById(R.id.mrn_layout);
        mrn= (TextView)mrnLayout.findViewById(R.id.mandatory_label);
        mrn.setText("MRN");
        mrn.setVisibility(View.GONE);//*/


       /* EditText mrnVal = (EditText)mrnLayout.findViewById(R.id.editText2);
        mrnVal.setText(tempWorkListDto.getMrn());
        mrnVal.setVisibility(View.GONE);
    */
        EditText financeVal = (EditText)finance_layout.findViewById(R.id.editText2);
        financeVal.setText(tempWorkListDto.getFinancialNum());
        financeVal.setVisibility(View.GONE);

         locationSpinner = (Spinner)location_spinner_layout.findViewById(R.id.mand_spinner);
        if(!TextUtils.isEmpty(MobileApplication.getInstance().getLocationList())){
            List<LocationDTO> locList = JSONParser.getInstance().getLocationList(MobileApplication.getInstance().getLocationList());
            List<String> hospitalList = new ArrayList<String>();
            if(locList!=null && locList.size()>0){
                for(int i=0;i<locList.size();i++){
                    hospitalList.add(locList.get(i).getListDesc());
                }
                if(hospitalList!=null && hospitalList.size()>0) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, hospitalList);
                    locationSpinner.setAdapter(adapter);
                    if(!TextUtils.isEmpty(tempWorkListDto.getHospitalName())){
                        int spinnerPosition = adapter.getPosition(tempWorkListDto.getHospitalName());
                        selectedLocation = tempWorkListDto.getHospitalName();
                        locationSpinner.setSelection(spinnerPosition);
                    }
                }
            }


            /*
             String selectedLocation = "";
        String selectedPhysician="";
        String selectedDisposition = "" ;
             */
            locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedLocation = locationSpinner.getSelectedItem().toString();
                    Log.d("LocationSpinner","Selected Spinner Item:::"+selectedLocation);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }


         physicianSpinner = (Spinner)pr_phy_spinner_layout.findViewById(R.id.mand_spinner);
        Spinner secPhySpinner  =  (Spinner)second_phy_spinner_layout.findViewById(R.id.mand_spinner);
        if(!TextUtils.isEmpty(MobileApplication.getInstance().getPhysicianList())){
            List<PhysicianDTO> prPhyList = JSONParser.getInstance().getPhysicianList(MobileApplication.getInstance().getPhysicianList());
            List<String> phyList = new ArrayList<String>();
            if(prPhyList!=null && prPhyList.size()>0){
                for(int i=0;i<prPhyList.size();i++){
                    phyList.add(prPhyList.get(i).getPhyDesc());
                }
                if(phyList!=null && phyList.size()>0) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, phyList);
                    physicianSpinner.setAdapter(adapter);
                    secPhySpinner.setAdapter(adapter);
                    if(!TextUtils.isEmpty(tempWorkListDto.getPhysicianName())){
                        int spinnerPosition = adapter.getPosition(tempWorkListDto.getPhysicianName());
                        selectedPhysician = tempWorkListDto.getPhysicianName();
                        physicianSpinner.setSelection(spinnerPosition);
                    }if(!TextUtils.isEmpty(tempWorkListDto.getSecPhysician())){
                        int spinnerPosition = adapter.getPosition(tempWorkListDto.getSecPhysician());
                        secPhySpinner.setSelection(spinnerPosition);
                    }
                }
            }
        }

        physicianSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPhysician = physicianSpinner.getSelectedItem().toString();
                Log.d("physicianSpinner","Selected Spinner Item:::"+selectedPhysician);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Spinner genderSpinner = (Spinner)gender_spinner_layout.findViewById(R.id.mand_spinner);
        if(!TextUtils.isEmpty(MobileApplication.getInstance().getGender())){
            List<LocationDTO> locList = JSONParser.getInstance().getLocationList(MobileApplication.getInstance().getGender());
            List<String> hospitalList = new ArrayList<String>();
            if(locList!=null && locList.size()>0){
                for(int i=0;i<locList.size();i++){
                    hospitalList.add(locList.get(i).getListDesc());
                }
                if(hospitalList!=null && hospitalList.size()>0) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, hospitalList);
                    genderSpinner.setAdapter(adapter);
                    if(!TextUtils.isEmpty(tempWorkListDto.getGender())){
                        int spinnerPosition = adapter.getPosition(tempWorkListDto.getGender());
                        genderSpinner.setSelection(spinnerPosition);
                    }
                }

            }
        }

         dispSpinner = (Spinner) dispoistion_spinner_layout.findViewById(R.id.mand_spinner);
        if(!TextUtils.isEmpty(MobileApplication.getInstance().getDispositionList())){
            List<LocationDTO> locList = JSONParser.getInstance().getLocationList(MobileApplication.getInstance().getDispositionList());
            List<String> hospitalList = new ArrayList<String>();
            if(locList!=null && locList.size()>0){
                for(int i=0;i<locList.size();i++){
                    hospitalList.add(locList.get(i).getListDesc());
                }
                if(hospitalList!=null && hospitalList.size()>0) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, hospitalList);
                    dispSpinner.setAdapter(adapter);


                    if(!TextUtils.isEmpty(tempWorkListDto.getBillingType())){
                        int spinnerPosition = adapter.getPosition(tempWorkListDto.getBillingType());
                        dispSpinner.setSelection(spinnerPosition);
                        selectedDisposition = tempWorkListDto.getBillingType();
                    }
                }
            }
        }


        dispSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDisposition = dispSpinner.getSelectedItem().toString();
                Log.d("selectedDisposition","Selected Spinner Item:::"+selectedDisposition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


         billingSpinner = (Spinner) billing_spinner_layout.findViewById(R.id.mand_spinner);
        if(!TextUtils.isEmpty(MobileApplication.getInstance().getBillingList())){
            List<LocationDTO> locList = JSONParser.getInstance().getLocationList(MobileApplication.getInstance().getBillingList());
            List<String> hospitalList = new ArrayList<String>();
            if(locList!=null && locList.size()>0){
                for(int i=0;i<locList.size();i++){
                    hospitalList.add(locList.get(i).getListDesc());
                }
                if(hospitalList!=null && hospitalList.size()>0) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, hospitalList);
                    billingSpinner.setAdapter(adapter);
                    if(!TextUtils.isEmpty(tempWorkListDto.getBillingStatus())){
                        int spinnerPosition = adapter.getPosition(tempWorkListDto.getBillingStatus());
                        billingSpinner.setSelection(spinnerPosition);
                    }
                }
            }
        }

        billingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDisposition = billingSpinner.getSelectedItem().toString();
                Log.d("selectedDisposition","Selected Spinner Item:::"+selectedDisposition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Spinner notesSpinner = (Spinner)notes_category_spinner_layout.findViewById(R.id.mand_spinner);
        if(!TextUtils.isEmpty(MobileApplication.getInstance().getNotesType())){
            List<LocationDTO> locList = JSONParser.getInstance().getLocationList(MobileApplication.getInstance().getNotesType());
            List<String> hospitalList = new ArrayList<String>();
            if(locList!=null && locList.size()>0){
                for(int i=0;i<locList.size();i++){
                    hospitalList.add(locList.get(i).getListDesc());
                }
                if(hospitalList!=null && hospitalList.size()>0) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, hospitalList);
                    notesSpinner.setAdapter(adapter);
                    if(!TextUtils.isEmpty(tempWorkListDto.getNotesType())){
                        int spinnerPosition = adapter.getPosition(tempWorkListDto.getNotesType());
                        billingSpinner.setSelection(spinnerPosition);
                    }
                }

            }
        }
    }

        private JSONObject preparePatientNotesJSON(){

            //{"request":{"Login_Id":"test1","Key":"ABFC2R;h","EncounterID":"102"}}
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
                updateJSON.put("EncounterID",encounterID);

                requestJsonObject = new JSONObject();
                requestJsonObject.put("request", updateJSON);

            }catch (JSONException e){
                e.printStackTrace();
            }


            return requestJsonObject;
        }

        private void initAndDisableUnwantedViews(View v ){
            patientName = (TextView)v.findViewById(R.id.patientName);
            patientName.setText("Name");



            patientRoom = (TextView)v.findViewById(R.id.roomNo);
            patientRoom.setText("Room Num");



            TextView encounterDateVal = (TextView)v.findViewById(R.id.patientDate);
            encounterDateVal.setText("Date");


            location_spinner_layout = (RelativeLayout)v.findViewById(R.id.hospital_row);
            pr_phy_spinner_layout =   (RelativeLayout)v.findViewById(R.id.physician_row);
            billing_spinner_layout = (RelativeLayout)v.findViewById(R.id.billing_layout);

            dispoistion_spinner_layout = (RelativeLayout)v.findViewById(R.id.disposition_layout);
            gender_spinner_layout = (RelativeLayout)v.findViewById(R.id.gender_layout);
            notes_category_spinner_layout = (RelativeLayout)v.findViewById(R.id.notes_category_layout);
            second_phy_spinner_layout = (RelativeLayout)v.findViewById(R.id.second_physician_layout);
            dateLayout = (RelativeLayout)v.findViewById(R.id.date_layout);
            admission_layout = (RelativeLayout)v.findViewById(R.id.admission_layout);
            finance_layout = (RelativeLayout)v.findViewById(R.id.financial_no_layout);

            dispoistion_spinner_layout.setVisibility(View.GONE);
            notes_category_spinner_layout.setVisibility(View.GONE);
            second_phy_spinner_layout.setVisibility(View.GONE);
            admission_layout .setVisibility(View.GONE);
            dateLayout.setVisibility(View.GONE);
            finance_layout.setVisibility(View.GONE);
            gender_spinner_layout.setVisibility(View.GONE);

            ((TextView)location_spinner_layout.findViewById(R.id.spinner_label)).setVisibility(View.GONE);//.setText("Location");
            ((TextView)pr_phy_spinner_layout.findViewById(R.id.spinner_label)).setVisibility(View.GONE);//.setText("Primary Physician");
            ((TextView)billing_spinner_layout.findViewById(R.id.spinner_label)).setVisibility(View.GONE);//.setText("Billing Method");
            ((TextView)dispoistion_spinner_layout.findViewById(R.id.spinner_label)).setVisibility(View.GONE);//.setText("Set Disposition");
            ((TextView)gender_spinner_layout.findViewById(R.id.spinner_label)).setVisibility(View.GONE);//.setText("Gender");
            ((TextView)notes_category_spinner_layout.findViewById(R.id.spinner_label)).setVisibility(View.GONE);//.setText("Notes Category");
            ((TextView)second_phy_spinner_layout.findViewById(R.id.spinner_label)).setVisibility(View.GONE);//.setText("2nd Physician");

            ((TextView)dateLayout.findViewById(R.id.mandatory_label)).setVisibility(View.GONE);//.setText("Date");
            ((TextView)admission_layout.findViewById(R.id.mandatory_label)).setVisibility(View.GONE);//.setText("Admission");
            ((TextView)finance_layout.findViewById(R.id.mandatory_label)).setVisibility(View.GONE);//.setText("Finance No");

         /*   mrnLayout =  (LinearLayout)v.findViewById(R.id.mrn_layout);
            mrn= (TextView)mrnLayout.findViewById(R.id.mandatory_label);
            mrn.setText("MRN");
            mrn.setVisibility(View.GONE);//


            EditText mrnVal = (EditText)mrnLayout.findViewById(R.id.editText2);
       //     mrnVal.setText(tempWorkListDto.getMrn());
            mrnVal.setVisibility(View.GONE);

            EditText financeVal = (EditText)finance_layout.findViewById(R.id.editText2);
        //    financeVal.setText(tempWorkListDto.getFinancialNum());
            financeVal.setVisibility(View.GONE);
    */
            Spinner locationSpinner = (Spinner)location_spinner_layout.findViewById(R.id.mand_spinner);
            if(!TextUtils.isEmpty(MobileApplication.getInstance().getLocationList())){
                List<LocationDTO> locList = JSONParser.getInstance().getLocationList(MobileApplication.getInstance().getLocationList());
                List<String> hospitalList = new ArrayList<String>();
                if(locList!=null && locList.size()>0){
                    for(int i=0;i<locList.size();i++){
                        hospitalList.add(locList.get(i).getListDesc());
                    }
                    if(hospitalList!=null && hospitalList.size()>0) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, hospitalList);
                        locationSpinner.setAdapter(adapter);
                        if(!TextUtils.isEmpty(tempWorkListDto.getHospitalName())){
                            int spinnerPosition = adapter.getPosition(tempWorkListDto.getHospitalName());
                            locationSpinner.setSelection(spinnerPosition);
                        }
                    }
                }
            }


            Spinner physicianSpinner = (Spinner)pr_phy_spinner_layout.findViewById(R.id.mand_spinner);
            Spinner secPhySpinner  =  (Spinner)second_phy_spinner_layout.findViewById(R.id.mand_spinner);
            if(!TextUtils.isEmpty(MobileApplication.getInstance().getPhysicianList())){
                List<PhysicianDTO> prPhyList = JSONParser.getInstance().getPhysicianList(MobileApplication.getInstance().getPhysicianList());
                List<String> phyList = new ArrayList<String>();
                if(prPhyList!=null && prPhyList.size()>0){
                    for(int i=0;i<prPhyList.size();i++){
                        phyList.add(prPhyList.get(i).getPhyDesc());
                    }
                    if(phyList!=null && phyList.size()>0) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, phyList);
                        physicianSpinner.setAdapter(adapter);
                        secPhySpinner.setAdapter(adapter);
                        if(!TextUtils.isEmpty(tempWorkListDto.getPhysicianName())){
                            int spinnerPosition = adapter.getPosition(tempWorkListDto.getPhysicianName());
                            physicianSpinner.setSelection(spinnerPosition);
                        }if(!TextUtils.isEmpty(tempWorkListDto.getSecPhysician())){
                            int spinnerPosition = adapter.getPosition(tempWorkListDto.getSecPhysician());
                            secPhySpinner.setSelection(spinnerPosition);
                        }
                    }
                }
            }

            Spinner genderSpinner = (Spinner)gender_spinner_layout.findViewById(R.id.mand_spinner);
            if(!TextUtils.isEmpty(MobileApplication.getInstance().getGender())){
                List<LocationDTO> locList = JSONParser.getInstance().getLocationList(MobileApplication.getInstance().getGender());
                List<String> hospitalList = new ArrayList<String>();
                if(locList!=null && locList.size()>0){
                    for(int i=0;i<locList.size();i++){
                        hospitalList.add(locList.get(i).getListDesc());
                    }
                    if(hospitalList!=null && hospitalList.size()>0) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, hospitalList);
                        genderSpinner.setAdapter(adapter);
                        if(!TextUtils.isEmpty(tempWorkListDto.getGender())){
                            int spinnerPosition = adapter.getPosition(tempWorkListDto.getGender());
                            genderSpinner.setSelection(spinnerPosition);
                        }
                    }

                }
            }

            Spinner dispSpinner = (Spinner) dispoistion_spinner_layout.findViewById(R.id.mand_spinner);
            if(!TextUtils.isEmpty(MobileApplication.getInstance().getDispositionList())){
                List<LocationDTO> locList = JSONParser.getInstance().getLocationList(MobileApplication.getInstance().getDispositionList());
                List<String> hospitalList = new ArrayList<String>();
                if(locList!=null && locList.size()>0){
                    for(int i=0;i<locList.size();i++){
                        hospitalList.add(locList.get(i).getListDesc());
                    }
                    if(hospitalList!=null && hospitalList.size()>0) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, hospitalList);
                        dispSpinner.setAdapter(adapter);


                        if(!TextUtils.isEmpty(tempWorkListDto.getBillingType())){
                            int spinnerPosition = adapter.getPosition(tempWorkListDto.getBillingType());
                            dispSpinner.setSelection(spinnerPosition);
                        }
                    }
                }
            }

            Spinner billingSpinner = (Spinner) billing_spinner_layout.findViewById(R.id.mand_spinner);
            if(!TextUtils.isEmpty(MobileApplication.getInstance().getBillingList())){
                List<LocationDTO> locList = JSONParser.getInstance().getLocationList(MobileApplication.getInstance().getBillingList());
                List<String> hospitalList = new ArrayList<String>();
                if(locList!=null && locList.size()>0){
                    for(int i=0;i<locList.size();i++){
                        hospitalList.add(locList.get(i).getListDesc());
                    }
                    if(hospitalList!=null && hospitalList.size()>0) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, hospitalList);
                        billingSpinner.setAdapter(adapter);
                        if(!TextUtils.isEmpty(tempWorkListDto.getBillingStatus())){
                            int spinnerPosition = adapter.getPosition(tempWorkListDto.getBillingStatus());
                            billingSpinner.setSelection(spinnerPosition);
                        }
                    }
                }
            }

            Spinner notesSpinner = (Spinner)notes_category_spinner_layout.findViewById(R.id.mand_spinner);
            if(!TextUtils.isEmpty(MobileApplication.getInstance().getNotesType())){
                List<LocationDTO> locList = JSONParser.getInstance().getLocationList(MobileApplication.getInstance().getNotesType());
                List<String> hospitalList = new ArrayList<String>();
                if(locList!=null && locList.size()>0){
                    for(int i=0;i<locList.size();i++){
                        hospitalList.add(locList.get(i).getListDesc());
                    }
                    if(hospitalList!=null && hospitalList.size()>0) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, hospitalList);
                        notesSpinner.setAdapter(adapter);
                        if(!TextUtils.isEmpty(tempWorkListDto.getNotesType())){
                            int spinnerPosition = adapter.getPosition(tempWorkListDto.getNotesType());
                            billingSpinner.setSelection(spinnerPosition);
                        }
                    }

                }
            }
        }
    }
