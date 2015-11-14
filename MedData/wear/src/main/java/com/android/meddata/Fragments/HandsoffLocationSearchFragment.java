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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.meddata.Application.MobileApplication;
import com.android.meddata.JSONParser.JSONParser;
import com.android.meddata.MedDataDTO.LocationDTO;
import com.android.meddata.MedDataDTO.PhysicianDTO;
import com.android.meddata.MessageAPI.MessageService;
import com.android.meddata.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HandsoffLocationSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HandsoffLocationSearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    HashMap<String,String> locationHash;
    int clickedLocationId = 1;
    String locationId="";
    int mContainerId = -1;
    Spinner location_spinner,primary_phy_spinner;
    Button search_patients;
    String selectedPhy="";


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HandsoffLocationSearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HandsoffLocationSearchFragment newInstance(String param1, String param2) {
        HandsoffLocationSearchFragment fragment = new HandsoffLocationSearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public HandsoffLocationSearchFragment() {
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
      //  View view  =  inflater.inflate(R.layout.handoff_wearable_list_layout, container, false);
        View view  =  inflater.inflate(R.layout.handoff_search_layout, container, false);

        mContainerId = container.getId();
      /*  RelativeLayout headerLayout = (RelativeLayout)view.findViewById(R.id.handoff_list_header);
        headerLayout.setVisibility(View.INVISIBLE);*/

        location_spinner = (Spinner)view.findViewById(R.id.loc_spinner);
        primary_phy_spinner = (Spinner)view.findViewById(R.id.primary_spinner);
        locationHash = new HashMap<String,String>();
        List<LocationDTO> locList = JSONParser.getInstance().getLocationList(MobileApplication.getInstance().getLocationList());
        List<String> hospitalList = new ArrayList<String>();
        if (locList != null && locList.size() > 0) {
            hospitalList.add("All");
            for (int i = 0; i < locList.size(); i++) {
                hospitalList.add(locList.get(i).getListDesc());
                locationHash.put(locList.get(i).getListDesc(), locList.get(i).getLocId());

            }

        }else{
            hospitalList.add("All");
            hospitalList.add("BPH - Hospital1");
            hospitalList.add("BPT - Hospital1");
        }


        if(hospitalList!=null && hospitalList.size()>0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, hospitalList);
            location_spinner.setAdapter(adapter);
            location_spinner.setSelection(0);

        }

        location_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selctedLocation = parent.getItemAtPosition(position)
                        .toString();
                locationId =selctedLocation;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (!TextUtils.isEmpty(MobileApplication.getInstance().getPhysicianList())) {
            List<PhysicianDTO> prPhyList = JSONParser.getInstance().getPhysicianList(MobileApplication.getInstance().getPhysicianList());
            List<String> phyList = new ArrayList<String>();
            if (prPhyList != null && prPhyList.size() > 0) {
                for (int i = 0; i < prPhyList.size(); i++) {
                    phyList.add(prPhyList.get(i).getPhyDesc());
                }
                if (phyList != null && phyList.size() > 0) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, phyList);
                    primary_phy_spinner.setAdapter(adapter);
              //      primary_phy_spinner.setAdapter(adapter);
                   /* if(!TextUtils.isEmpty(tempWorkListDto.getPhysicianName())){
                        int spinnerPosition = adapter.getPosition(tempWorkListDto.getPhysicianName());
                        physicianSpinner.setSelection(spinnerPosition);
                    }*/
                }
            }
        }

        primary_phy_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPhy = parent.getItemAtPosition(position)
                        .toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        search_patients  = (Button)view.findViewById(R.id.search_handsoff);
      /*  WearableListView handoffListView = (WearableListView) view.findViewById(R.id.handoff_List);
        handoffListView.setAdapter(new HandOffAdapter(getActivity(), hospitalList));
        WearableListView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        handoffListView.addItemDecoration(itemDecoration);
        handoffListView.setClickListener(mClickListener);*/

        ImageButton back_float = (ImageButton)view.findViewById(R.id.fab_back);
        back_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(mContainerId, new HandOffSearchListFragment()).addToBackStack(null).commit();
            }
        });

        search_patients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postForSearchResults();
            }
        });

        return view;
    }

    // Handle our Wearable List's click events
    private WearableListView.ClickListener mClickListener =
            new WearableListView.ClickListener() {
                @Override
                public void onClick(WearableListView.ViewHolder viewHolder) {
                    TextView handOffListItem = (TextView) viewHolder.itemView.findViewById(R.id.textView);
                   /* WorkListItemLayout listViewRowView = (WorkListItemLayout) viewHolder.itemView;
                    TextView handOffListItem = (TextView)listViewRowView.findViewById(R.id.textView);*/
                    Log.d("TAG", "Clicked Item Name:::" +handOffListItem.getText().toString());
                   /* if(handOffListItem.getText().toString().equalsIgnoreCase("All")){
                        clickedLocationId = 0;
                    }else {
                        clickedLocationId = Integer.parseInt(locationHash.get(handOffListItem.getText().toString()));
                    }*/
                    locationId = handOffListItem.getText().toString();

                    postForSearchResults();
                   /* String tag_clicked = (String)listViewRowView.getTag();*/
                /*    Toast.makeText(getActivity(),
                            String.format("You selected item #%s",
                                    viewHolder.getLayoutPosition()+1) +"TAG Clicked"+tag_clicked,
                            Toast.LENGTH_SHORT).show();
*/

                }

                @Override
                public void onTopEmptyRegionClick() {
                    Toast.makeText(getActivity(),
                            "Top empty area tapped", Toast.LENGTH_SHORT).show();
                }
            };


    public void postForSearchResults(){
        JSONObject handOffSearchJSON = prepareHandsOffSearchJSON();
        if(handOffSearchJSON!=null){
            Log.d("TAG", "Account Details Update JSON:::" + handOffSearchJSON);
            MobileApplication.getInstance().setHandOffSearchJSON(""+handOffSearchJSON);
            MessageService.getInstance().startMessageService(getActivity(), "handoffSearch");
        }
    }



    private JSONObject prepareHandsOffSearchJSON(){
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
            if(!TextUtils.isEmpty(selectedPhy)){
                updateJSON.put("PrimaryPhysician", selectedPhy);
            }else {
                updateJSON.put("PrimaryPhysician", "Tyy Dr"); //Sue Chilson
            }
            updateJSON.put("FromDate", null);
            updateJSON.put("ToDate", null);
            updateJSON.put("MedicalRecordNo", "");
            updateJSON.put("Key",key);
            updateJSON.put("Login_Id", "veereshm");
            updateJSON.put("TransactionType", "T");
            updateJSON.put("FirstName", "");
            updateJSON.put("LocationId", locationId);
         /*   if(clickedLocationId==0){
                updateJSON.put("LocationId", locationId);
            }else {
                updateJSON.put("LocationId", "" + clickedLocationId);
            }*/

            requestJsonObject = new JSONObject();
            requestJsonObject.put("request", updateJSON);

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
                    if (result_data.equalsIgnoreCase("handoffSearch")) {
                        Log.d("TAG", "handoffSearchResponse::::" + MobileApplication.getInstance().getHandsOffSearchResponse());
                        if(MobileApplication.getInstance().getHandsOffSearchResponse().equalsIgnoreCase("No")){
                            Toast.makeText(getActivity(),"No Records Found",Toast.LENGTH_SHORT).show();
                        }else{
                            Log.d("TAG","Search Response:::"+MobileApplication.getInstance().getHandsOffSearchResponse());
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.framelayout, HandsOffPatientSearchResultFragment.newInstance("","")).addToBackStack(null)
                                    .commit();
                        }
                        //Toast.makeText(getActivity(), MobileApplication.getInstance().getHandsOffSearchResponse(), Toast.LENGTH_SHORT).show();
                        //      Toast.makeText(getActivity(),MobileApplication.getInstance().getBulkUpdateResponse(),Toast.LENGTH_LONG).show();

                    }

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private String getJsonDateFormat(String javaDate){
        String jsonDate = "";
        if(!TextUtils.isEmpty(javaDate)){
			/* Date dt = new Date(javaDate);
			 Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
					 cal.setTime(dt);*/
            //cal.setTimeZone(TimeZone.getTimeZone("GMT"));
            //TimeZone.getTimeZone("GMT").getOffset(time)
            //SimpleDateFormat format = new SimpleDateFormat("Z");

            //String str = "\/Date(" + dt.getTime() + "-0400)\/";
            ///	format.setTimeZone(TimeZone.getTimeZone("GMT"));
            //jsonDate  ="/Date(" + String.valueOf(dt.getTime())+")/";

            try {
                SimpleDateFormat sdf  = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss");
                javaDate =javaDate +" "+Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+":"+
                        Calendar.getInstance().get(Calendar.MINUTE)+":"+
                        Calendar.getInstance().get(Calendar.SECOND);
                System.out.println("javaDate"+javaDate);
                long unixtime = sdf.parse(javaDate).getTime();

                jsonDate  ="/Date(" + String.valueOf(unixtime)+")/";
            } catch(java.text.ParseException e){
                e.printStackTrace();
            }
        }

        return jsonDate;
    }
}