package com.android.meddata.Fragments;
//https://github.com/danramirez/wearable-listview-example/blob/master/app/src/main/res/layout/wearablelistview_image_text_item.xml
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.widget.Toolbar;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.meddata.Adapters.DashBoardAdapter;
import com.android.meddata.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CHANDRASAIMOHAN on 10/19/2015.
 */
public class DashBoardWearableListFragment extends Fragment {
    private static List<Integer> mIcons;
    private static List<String>dashBoardTitles;
    TextView mHeader;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  =  inflater.inflate(R.layout.dashboard_wearable_list_layout, container, false);
        // Sample icons for the list
        mIcons = new ArrayList<Integer>();
        mIcons.add(R.drawable.ic_action_worklist);
        mIcons.add(R.drawable.ic_action_reminder);
        mIcons.add(R.drawable.ic_action_star);
        mIcons.add(R.drawable.ic_action_myaccount);

        dashBoardTitles = new ArrayList<String>();
        dashBoardTitles.add("Work List");
        dashBoardTitles.add("Reminders");
        dashBoardTitles.add("Hands Off");
        dashBoardTitles.add("My Account");

        // This is our list header
        mHeader = (TextView)view. findViewById(R.id.header);

        WearableListView wearableListView =
                (WearableListView)view. findViewById(R.id.wearable_List);
        Toolbar mToolBar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        ImageView back_img = (ImageView)mToolBar.findViewById(R.id.back);
        back_img.setVisibility(View.INVISIBLE);
        TextView toolbarTitle = (TextView)mToolBar.findViewById(R.id.title);
        toolbarTitle.setText("Patient List");
        wearableListView.setAdapter(new DashBoardAdapter(getActivity(), mIcons, dashBoardTitles));
        wearableListView.setClickListener(mClickListener);
        wearableListView.addOnScrollListener(mOnScrollListener);
        ImageView disposition_btn = (ImageView)mToolBar.findViewById(R.id.right_icon);
        disposition_btn.setVisibility(View.INVISIBLE);
        TextView notesView = (TextView)mToolBar.findViewById(R.id.notes);
        notesView.setVisibility(View.GONE);

        return view;
    }

    // Handle our Wearable List's click events
    private WearableListView.ClickListener mClickListener =
            new WearableListView.ClickListener() {
                @Override
                public void onClick(WearableListView.ViewHolder viewHolder) {
                   /* Toast.makeText(getActivity(),
                            String.format("You selected item #%s",
                                    viewHolder.getLayoutPosition()+1),
                            Toast.LENGTH_SHORT).show();*/
                    if( viewHolder.getLayoutPosition()+1 == 1){
                        getFragmentManager().beginTransaction()
                                .replace(R.id.framelayout, new WorkListWearableListFragment()).addToBackStack(null)
                                .commit();
                    }else if( viewHolder.getLayoutPosition()+1 == 2){
                        getFragmentManager().beginTransaction()
                                .replace(R.id.framelayout, new ReminderListWearableListFragment()).addToBackStack(null)
                                .commit();
                    }else if( viewHolder.getLayoutPosition()+1 == 4){
                        getFragmentManager().beginTransaction()
                                .replace(R.id.framelayout, new Account_Details_Fragment()).addToBackStack(null)
                                .commit();
                    }else if( viewHolder.getLayoutPosition()+1 == 3){
                        getFragmentManager().beginTransaction()
                                .replace(R.id.framelayout, new HandOffListFragment()).addToBackStack(null)
                                .commit();
                    }
                }

                @Override
                public void onTopEmptyRegionClick() {
                   /* Toast.makeText(getActivity(),
                            "Top empty area tapped", Toast.LENGTH_SHORT).show();*/
                }
            };


    // The following code ensures that the title scrolls as the user scrolls up
    // or down the list
    private WearableListView.OnScrollListener mOnScrollListener =
            new WearableListView.OnScrollListener() {
                @Override
                public void onAbsoluteScrollChange(int i) {
                    // Only scroll the title up from its original base position
                    // and not down.
                    if (i > 0) {
                        mHeader.setY(-i);
                    }
                }

                @Override
                public void onScroll(int i) {
                    // Placeholder
                }

                @Override
                public void onScrollStateChanged(int i) {
                    // Placeholder
                }

                @Override
                public void onCentralPositionChanged(int i) {
                    // Placeholder
                }
            };
}
