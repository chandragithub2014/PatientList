package com.android.meddata.Fragments;
//https://github.com/danramirez/wearable-listview-example/blob/master/app/src/main/res/layout/wearablelistview_image_text_item.xml

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.support.wearable.view.WearableListView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.meddata.Adapters.ReminderListAdapter;
import com.android.meddata.Adapters.ReminderListItemLayout;
import com.android.meddata.Application.MobileApplication;
import com.android.meddata.JSONParser.JSONParser;
import com.android.meddata.MedDataDTO.RemindersDTO;
import com.android.meddata.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CHANDRASAIMOHAN on 10/19/2015.
 */
public class ReminderListWearableListFragment extends Fragment {
    private static List<Integer> mIcons;
    private static List<String>dashBoardTitles;
    private static List<RemindersDTO> remindersDTOList;
  //  RelativeLayout listHeader;
    int mContainerId = -1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  =  inflater.inflate(R.layout.reminderlist_wearable_list_layout, container, false);
        Log.d("TAG", "ReminderList:::" + MobileApplication.getInstance().getReminderList());


        if(!TextUtils.isEmpty(MobileApplication.getInstance().getReminderList())){
            remindersDTOList = JSONParser.getInstance().getRemindersList(MobileApplication.getInstance().getReminderList());
        }else{
            //Dummy Data
            populateWorkList();
        }

        // This is our list header

 //        listHeader = (RelativeLayout)view.findViewById(R.id.reminder_header);

        WearableListView wearableListView =
                (WearableListView)view. findViewById(R.id.reminder_List);
        mContainerId = container.getId();
        Toolbar mToolBar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView)mToolBar.findViewById(R.id.title);
        toolbarTitle.setText("Reminders");
        ImageView back_img = (ImageView)mToolBar.findViewById(R.id.back);
        back_img.setVisibility(View.VISIBLE);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(mContainerId, new DashBoardWearableListFragment()).addToBackStack(null).commit();
            }
        });
        wearableListView.setAdapter(new ReminderListAdapter(getActivity(), remindersDTOList));
        WearableListView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        wearableListView.addItemDecoration(itemDecoration);
        wearableListView.setClickListener(mClickListener);
        wearableListView.addOnScrollListener(mOnScrollListener);


        return view;
    }

    private void populateWorkList(){
        remindersDTOList = new ArrayList<RemindersDTO>();
        RemindersDTO temp;
        temp = new RemindersDTO();
        temp.setDay("SUN");
        temp.setDateOfDay("Oct 20,2015");
        temp.setPrimaryPhysician("Ph. Dr.Physian1");
        temp.setPatientName("Kumar K");
        temp.setSecPhysician("Ph. Dr.Physian2");
        temp.setHospitalName("BPH-Hospital1");

        remindersDTOList.add(temp);

        temp = new RemindersDTO();
        temp.setDay("MON");
        temp.setDateOfDay("Oct 21,2015");
        temp.setPrimaryPhysician("Ph. Dr.Physian11");
        temp.setPatientName("Kumaaar K");
        temp.setSecPhysician("Ph. Dr.Physian22");
        temp.setHospitalName("BPH-Hospital1");

        remindersDTOList.add(temp);

        temp = new RemindersDTO();
        temp.setDay("TUE");
        temp.setDateOfDay("Oct 22,2015");
        temp.setPrimaryPhysician("Ph. Dr.PhysCian11");
        temp.setPatientName("KuUmaaar K");
        temp.setSecPhysician("Ph. Dr.Physian222");
        temp.setHospitalName("BPH-Hospital1");

        remindersDTOList.add(temp);

        temp = new RemindersDTO();
        temp.setDay("WED");
        temp.setDateOfDay("Oct 26,2015");
        temp.setPrimaryPhysician("Ph. Dr.PhysCian11");
        temp.setPatientName("KUMARRR K");
        temp.setSecPhysician("");
        temp.setHospitalName("BPH-Hospita21");

        remindersDTOList.add(temp);
    }
    // Handle our Wearable List's click events
    private WearableListView.ClickListener mClickListener =
            new WearableListView.ClickListener() {
                @Override
                public void onClick(WearableListView.ViewHolder viewHolder) {
                  /*  Toast.makeText(getActivity(),
                            String.format("You selected item #%s",
                                    viewHolder.getLayoutPosition()+1),
                            Toast.LENGTH_SHORT).show();*/
                    ReminderListItemLayout listViewRowView = (ReminderListItemLayout) viewHolder.itemView;
                    String tag_clicked = (String)listViewRowView.getTag();
                  /*  Toast.makeText(getActivity(),
                            String.format("You selected item #%s",
                                    viewHolder.getLayoutPosition()+1) +"TAG Clicked"+tag_clicked,
                            Toast.LENGTH_SHORT).show();*/

                    getFragmentManager().beginTransaction()
                            .replace(R.id.framelayout, PatentDetailsFragment.newInstance(tag_clicked,"")).addToBackStack(null)
                            .commit();
                }

                @Override
                public void onTopEmptyRegionClick() {
                 /*   Toast.makeText(getActivity(),
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
                 /*   if (i > 0) {
                        listHeader.setY(-i);
                    }*/
                }

                @Override
                public void onScroll(int i) {
                    // Placeholder
               //     listHeader.setY(listHeader.getY() - i);
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
