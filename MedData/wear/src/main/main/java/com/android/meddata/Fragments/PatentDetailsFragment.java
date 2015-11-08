package com.android.meddata.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.android.meddata.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatentDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatentDetailsFragment extends Fragment {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.worklist_details, container, false);
        mContainerId = container.getId();
        if(!TextUtils.isEmpty(encounterID)){
            tempWorkListDto = JSONParser.getInstance().getPatientDetail(encounterID);
        }
        if(tempWorkListDto!=null) {
            initAndPopulateLabels(v);
        }
        return  v;
    }

private void initAndPopulateLabels(View v ){
    nameLayout = (LinearLayout)v.findViewById(R.id.name_label);
    patientName = (TextView)nameLayout.findViewById(R.id.mandatory_label);

    patientName.setText("Name");
    EditText patientNameVal = (EditText)nameLayout.findViewById(R.id.editText2);
    patientNameVal.setText(tempWorkListDto.getPatientName());

    roomLayout =  (LinearLayout)v.findViewById(R.id.name_val_layout);
    patientRoom  = (TextView)roomLayout.findViewById(R.id.mandatory_label);
    patientRoom.setText("Room No");
    EditText patientRoomVal = (EditText)roomLayout.findViewById(R.id.editText2);
    patientRoomVal.setText(tempWorkListDto.getRoomNum());

    encounterDateLayout =  (LinearLayout)v.findViewById(R.id.encounter_date_val_layout);
    encounterDate= (TextView)encounterDateLayout.findViewById(R.id.mandatory_label);
    encounterDate.setText("Encounter Date");
    EditText encounterDateVal = (EditText)encounterDateLayout.findViewById(R.id.editText2);
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

    ((TextView)location_spinner_layout.findViewById(R.id.spinner_label)).setText("Location");
    ((TextView)pr_phy_spinner_layout.findViewById(R.id.spinner_label)).setText("Primary Physician");
    ((TextView)billing_spinner_layout.findViewById(R.id.spinner_label)).setText("Billing Method");
    ((TextView)dispoistion_spinner_layout.findViewById(R.id.spinner_label)).setText("Set Disposition");
    ((TextView)gender_spinner_layout.findViewById(R.id.spinner_label)).setText("Gender");
    ((TextView)notes_category_spinner_layout.findViewById(R.id.spinner_label)).setText("Notes Category");
    ((TextView)second_phy_spinner_layout.findViewById(R.id.spinner_label)).setText("2nd Physician");

    ((TextView)dateLayout.findViewById(R.id.mandatory_label)).setText("Date");
    ((TextView)admission_layout.findViewById(R.id.mandatory_label)).setText("Admission");
    ((TextView)finance_layout.findViewById(R.id.mandatory_label)).setText("Finance No");

    mrnLayout =  (LinearLayout)v.findViewById(R.id.mrn_layout);
    mrn= (TextView)mrnLayout.findViewById(R.id.mandatory_label);
    mrn.setText("MRN");


    EditText mrnVal = (EditText)mrnLayout.findViewById(R.id.editText2);
    mrnVal.setText(tempWorkListDto.getMrn());

    EditText financeVal = (EditText)finance_layout.findViewById(R.id.editText2);
    financeVal.setText(tempWorkListDto.getFinancialNum());


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
