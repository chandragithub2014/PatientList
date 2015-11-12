package com.android.meddata.Fragments;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.android.meddata.Adapters.HandOffAdapter;
import com.android.meddata.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HandOffSearchListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HandOffSearchListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    int mContainerId = -1;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HandOffSearchListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HandOffSearchListFragment newInstance(String param1, String param2) {
        HandOffSearchListFragment fragment = new HandOffSearchListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public HandOffSearchListFragment() {
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
        View view  =  inflater.inflate(R.layout.handoff_wearable_list_layout, container, false);
        mContainerId = container.getId();
        RelativeLayout headerLayout = (RelativeLayout)view.findViewById(R.id.handoff_list_header);
        headerLayout.setVisibility(View.INVISIBLE);
        List<String> listItems = new ArrayList<String>();
        listItems.add("Search By Location");
        listItems.add("Search By Date");

        ImageButton back_float = (ImageButton)view.findViewById(R.id.fab_back);
        back_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(mContainerId, new HandOffListFragment()).addToBackStack(null).commit();
            }
        });
        WearableListView handoffListView = (WearableListView) view.findViewById(R.id.handoff_List);
        handoffListView.setAdapter(new HandOffAdapter(getActivity(), listItems));
        WearableListView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        handoffListView.addItemDecoration(itemDecoration);
        handoffListView.setClickListener(new WearableListView.ClickListener() {
            @Override
            public void onTopEmptyRegionClick() {

            }

            @Override
            public void onClick(WearableListView.ViewHolder viewHolder) {
               /* Toast.makeText(getActivity(),
                        String.format("You selected item #%s",
                                viewHolder.getLayoutPosition() + 1),
                        Toast.LENGTH_SHORT).show();*/
                if ((viewHolder.getLayoutPosition() + 1) == 1) {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.framelayout, HandsoffLocationSearchFragment.newInstance("", "")).addToBackStack(null)
                            .commit();
                } else {

                }
            }
        });
        return view;
    }


}
