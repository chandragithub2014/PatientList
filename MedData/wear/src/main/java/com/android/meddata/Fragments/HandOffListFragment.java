package com.android.meddata.Fragments;
//https://github.com/danramirez/wearable-listview-example/blob/master/app/src/main/res/layout/wearablelistview_image_text_item.xml

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.meddata.Adapters.HandOffAdapter;
import com.android.meddata.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CHANDRASAIMOHAN on 10/19/2015.
 */
public class HandOffListFragment extends Fragment {
    int mContainerId = -1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  =  inflater.inflate(R.layout.handoff_wearable_list_layout, container, false);
        mContainerId = container.getId();
        Toolbar mToolBar = (Toolbar)getActivity().findViewById(R.id.toolbar);


        TextView toolbarTitle = (TextView)mToolBar.findViewById(R.id.title);
        toolbarTitle.setText("Handsoff");
        ImageView back_img = (ImageView)mToolBar.findViewById(R.id.back);
        back_img.setVisibility(View.VISIBLE);
        RelativeLayout listHeader = (RelativeLayout)view.findViewById(R.id.handoff_list_header);
        listHeader.setVisibility(View.GONE);
        ImageButton back_float = (ImageButton)view.findViewById(R.id.fab_back);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(mContainerId, new DashBoardWearableListFragment()).addToBackStack(null).commit();
            }
        });
        back_float.setVisibility(View.GONE);
        List<String> listItems = new ArrayList<String>();
        listItems.add("Hand off current patients");
        listItems.add("Find my handed off patients");
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
                    /*getFragmentManager().beginTransaction()
                            .replace(R.id.framelayout, HandOffSearchListFragment.newInstance("","")).addToBackStack(null)
                            .commit();*/
                    getFragmentManager().beginTransaction()
                            .replace(R.id.framelayout, MyHandoffPatientFragment.newInstance("handoff","")).addToBackStack(null)
                            .commit();
                } else {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.framelayout, MyHandoffPatientFragment.newInstance("","")).addToBackStack(null)
                            .commit();
                }
            }
        });
        return view;
    }




    // Handle our Wearable List's click events

// header.setY(header.getY() - scroll);

    // The following code ensures that the title scrolls as the user scrolls up
    // or down the list

}
