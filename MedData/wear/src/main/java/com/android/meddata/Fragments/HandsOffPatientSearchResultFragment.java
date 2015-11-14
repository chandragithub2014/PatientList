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
import android.support.wearable.view.WearableListView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.meddata.Adapters.HandOffSearchListItemLayout;
import com.android.meddata.Adapters.HandOffSearchResultsPatientAdapter;
import com.android.meddata.Application.MobileApplication;
import com.android.meddata.MedDataDTO.HandOffResultDTO;
import com.android.meddata.MessageAPI.MessageService;
import com.android.meddata.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HandsOffPatientSearchResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HandsOffPatientSearchResultFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View handOffPatientListView;
    private boolean isRevert = false;
    int mContainerId = -1;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HandsOffPatientSearchResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HandsOffPatientSearchResultFragment newInstance(String param1, String param2) {
        HandsOffPatientSearchResultFragment fragment = new HandsOffPatientSearchResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public HandsOffPatientSearchResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        // Register the local broadcast receiver
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(messageReceiver, messageFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        handOffPatientListView =  inflater.inflate(R.layout.fragment_hands_off_patient_search_result, container, false);
        WearableListView handoffListView = (WearableListView) handOffPatientListView.findViewById(R.id.handoff_search_result_List);
        mContainerId = container.getId();
        if(!TextUtils.isEmpty(mParam1) && mParam1.equalsIgnoreCase("revert")){
           isRevert = true;
       }
        ImageButton back_float = (ImageButton)handOffPatientListView.findViewById(R.id.fab_back);
        back_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if(isRevert) {
                    fragmentTransaction.replace(mContainerId, new MyHandoffPatientFragment()).addToBackStack(null).commit();
                }else{
                    fragmentTransaction.replace(mContainerId, new HandOffSearchListFragment()).addToBackStack(null).commit();
                }
            }
        });
        List<HandOffResultDTO> handOffResultDTOList = getPatientList(MobileApplication.getInstance().getHandsOffSearchResponse());
       if(handOffResultDTOList!=null && handOffResultDTOList.size()>0) {
           handoffListView.setAdapter(new HandOffSearchResultsPatientAdapter(getActivity(), handOffResultDTOList, isRevert));
           WearableListView.ItemDecoration itemDecoration =
                   new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
           handoffListView.addItemDecoration(itemDecoration);
           handoffListView.setClickListener(mClickListener);

       }
        return handOffPatientListView;
    }

    // Handle our Wearable List's click events
    private WearableListView.ClickListener mClickListener =
            new WearableListView.ClickListener() {
                @Override
                public void onClick(WearableListView.ViewHolder viewHolder) {
                    Toast.makeText(getActivity(),
                            String.format("You selected item #%s",
                                    viewHolder.getLayoutPosition() + 1),
                            Toast.LENGTH_SHORT).show();
                    HandOffSearchListItemLayout listViewRowView = (HandOffSearchListItemLayout) viewHolder.itemView;
                 /*   ImageView detail_img = (ImageView)listViewRowView.findViewById(R.id.detail);
                    detail_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity(),"Clicked on Detail Image",Toast.LENGTH_LONG).show();
                        }
                    });*/
                   /* String tag_clicked = (String)listViewRowView.getTag();
                    Toast.makeText(getActivity(),
                            String.format("You selected item #%s",
                                    viewHolder.getLayoutPosition()+1) +"TAG Clicked"+tag_clicked,
                            Toast.LENGTH_SHORT).show();*/
                //    TextView pateintId = (TextView)listViewRowView.findViewById( R.id.patient_id);
                    HandOffResultDTO temp = (HandOffResultDTO) listViewRowView.getTag();
                    int  patientID  = temp.getPatientId();
                  //  TextView encounter_Id = (TextView)listViewRowView.findViewById(R.id.encounter_id);
                    int  encounterId = temp.getEncounterId();
                    if(!isRevert) {
                        Fragment fr = new HandOffIndividualPatientFragment();
                        FragmentManager fm = getFragmentManager();
                        android.app.FragmentTransaction ft = fm.beginTransaction();
                        Bundle args = new Bundle();
                        args.putInt("PATIENT_ID", patientID);
                        args.putInt("ENCOUNTER_ID", encounterId);
                        fr.setArguments(args);
                        ft.replace(R.id.framelayout, fr);
                        ft.addToBackStack(null);
                        ft.commit();
                    }else{
                        postRevert(patientID,encounterId);
                    }
                 /*   getFragmentManager().beginTransaction()
                            .replace(R.id.framelayout, PatentDetailsFragment.newInstance(tag_clicked,"")).addToBackStack(null)
                            .commit();*/
                }

                @Override
                public void onTopEmptyRegionClick() {
                    Toast.makeText(getActivity(),
                            "Top empty area tapped", Toast.LENGTH_SHORT).show();
                }
            };

    List<HandOffResultDTO> getPatientList(String response){
        List<HandOffResultDTO> handOffResultList = new ArrayList<HandOffResultDTO>();
        try{
            JSONArray resultsArray = new JSONArray(response);
            for(int i=0;i<resultsArray.length();i++){
                JSONObject jsonObject = resultsArray.getJSONObject(i);
                HandOffResultDTO temp = new HandOffResultDTO();
                temp.setHospitalLocation(jsonObject.getString("LocationId"));
                temp.setPatientName(jsonObject.getString("Patientname"));
                temp.setPrimPhy(jsonObject.getString("PrimaryPhysician"));
                temp.setPatientId(jsonObject.getInt("PatientID"));
                temp.setEncounterId(jsonObject.getInt("EncounterId"));
            //   Log.d("TAG","Encounter Date::::"+jsonObject.getString("Encounter_Date"));
                if(!jsonObject.getString("Encounter_Date").equalsIgnoreCase("null")){
                    temp.setEncounterDate(jsonObject.getString("Encounter_Date"));
                }else{
                    temp.setEncounterDate("");
                }
                handOffResultList.add(temp);
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return handOffResultList;
    }

    private void postRevert(int patientId,int encounterId){
        JSONObject revertJSON = prepareRevertPatientJSON(patientId,encounterId);
        if(revertJSON!=null){
            Log.d("TAG", "Patient revert  JSON:::" + revertJSON);
            MobileApplication.getInstance().setPatientRevertJSON("" + revertJSON);
            MessageService.getInstance().startMessageService(getActivity(), "revertpatient");
        }
    }

    private JSONObject prepareRevertPatientJSON(int patientId,int encounterId){
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


            updateJSON.put("PatientID",patientId);
            updateJSON.put("Login_Id", "veereshm");
            updateJSON.put("EncounterId", encounterId);



                updateJSON.put("PrimaryPhysician", "veeresh"); //Sue Chilson


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


    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //  String message = intent.getStringExtra("message");
            //Log.v("myTag", "Main activity received message: " + message);

            try {
                //
                if (intent.hasExtra("result")) {
                    String result_data = intent.getStringExtra("result");
                    if (result_data.equalsIgnoreCase("revertpatient")) {
                        Log.d("TAG", "revertpatient Response::::" + MobileApplication.getInstance().getPatientRevertResponse());
                        Toast.makeText(getActivity(), MobileApplication.getInstance().getPatientRevertResponse(), Toast.LENGTH_SHORT).show();

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
