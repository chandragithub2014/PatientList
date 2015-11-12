package com.android.meddata.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.wearable.view.WearableListView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.meddata.Adapters.HandOffSearchResultsPatientAdapter;
import com.android.meddata.Adapters.ReminderListAdapter;
import com.android.meddata.Adapters.ReminderListItemLayout;
import com.android.meddata.Application.MobileApplication;
import com.android.meddata.JSONParser.JSONParser;
import com.android.meddata.MedDataDTO.HandOffResultDTO;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        handOffPatientListView =  inflater.inflate(R.layout.fragment_hands_off_patient_search_result, container, false);
        WearableListView handoffListView = (WearableListView) handOffPatientListView.findViewById(R.id.handoff_search_result_List);
        List<HandOffResultDTO> handOffResultDTOList = getPatientList(MobileApplication.getInstance().getHandsOffSearchResponse());
       if(handOffResultDTOList!=null && handOffResultDTOList.size()>0) {
           handoffListView.setAdapter(new HandOffSearchResultsPatientAdapter(getActivity(), handOffResultDTOList));
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
                    ReminderListItemLayout listViewRowView = (ReminderListItemLayout) viewHolder.itemView;
                    String tag_clicked = (String)listViewRowView.getTag();
                    Toast.makeText(getActivity(),
                            String.format("You selected item #%s",
                                    viewHolder.getLayoutPosition()+1) +"TAG Clicked"+tag_clicked,
                            Toast.LENGTH_SHORT).show();

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
}
