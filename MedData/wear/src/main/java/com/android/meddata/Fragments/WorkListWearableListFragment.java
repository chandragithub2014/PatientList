package com.android.meddata.Fragments;
//https://github.com/danramirez/wearable-listview-example/blob/master/app/src/main/res/layout/wearablelistview_image_text_item.xml

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.support.wearable.view.WearableListView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.meddata.Adapters.AlertAdapter;
import com.android.meddata.Adapters.WorkListAdapter;
import com.android.meddata.Adapters.WorkListItemLayout;
import com.android.meddata.Application.MobileApplication;
import com.android.meddata.JSONParser.JSONParser;
import com.android.meddata.MedDataDTO.WorkListDTO;
import com.android.meddata.MessageAPI.MessageService;
import com.android.meddata.R;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CHANDRASAIMOHAN on 10/19/2015.
 */
public class WorkListWearableListFragment extends Fragment {
    private static List<Integer> mIcons;
    private static List<String>dashBoardTitles;
    private static List<WorkListDTO> workList;
    RelativeLayout listHeader;
    int mContainerId = -1;
    RelativeLayout header;
    ImageButton floatingToolBarButton;
    AlertDialog  sortByDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Register the local broadcast receiver
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(messageReceiver, messageFilter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  =  inflater.inflate(R.layout.worklist_wearable_list_layout, container, false);

        Log.d("TAG", "WorkList:::" + MobileApplication.getInstance().getPatientList());

        if(!TextUtils.isEmpty(MobileApplication.getInstance().getPatientList())){
            workList = JSONParser.getInstance().getWorkList(MobileApplication.getInstance().getPatientList());
        }else{
            //Dummy Data
            populateWorkList();
        }
        // This is our list header
        floatingToolBarButton = (ImageButton)view.findViewById(R.id.fab);
         listHeader = (RelativeLayout)view.findViewById(R.id.work_list_header);
        listHeader.setVisibility(View.GONE);
        mContainerId = container.getId();
        WearableListView wearableListView =
                (WearableListView)view. findViewById(R.id.work_List);
        header = (RelativeLayout)view. findViewById(R.id.work_list_header);
        Toolbar mToolBar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        ImageView back_img = (ImageView)mToolBar.findViewById(R.id.back);
        back_img.setVisibility(View.VISIBLE);
      TextView toolbarTitle = (TextView)mToolBar.findViewById(R.id.title);
        toolbarTitle.setText("Work List");
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(mContainerId, new DashBoardWearableListFragment()).addToBackStack(null).commit();
            }
        });
        floatingToolBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View sortDialogView = inflater.inflate(R.layout.alert_dialog_status_list, null);
                WearableListView sortListView = (WearableListView) sortDialogView.findViewById(R.id.dlg_sort_listview);

                TextView title = (TextView) sortDialogView.findViewById(R.id.dlg_sort_title);
                title.setText("Apply Status to all Patients");
                List<String> listItems = new ArrayList<String>();
                listItems.add("Billable Rounding");
                listItems.add("Signed Off");
                sortListView.setAdapter(new AlertAdapter(getActivity(), listItems));
                WearableListView.ItemDecoration itemDecoration =
                        new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
                sortListView.addItemDecoration(itemDecoration);
                sortListView.setClickListener(new WearableListView.ClickListener() {
                    @Override
                    public void onClick(WearableListView.ViewHolder viewHolder) {
                        RelativeLayout listViewRowView = (RelativeLayout) viewHolder.itemView;
                     //   String tag_clicked = (String)listViewRowView.getTag();
                       /* Toast.makeText(getActivity(),
                                String.format("You selected item #%s",
                                        viewHolder.getLayoutPosition()+1) ,
                                Toast.LENGTH_SHORT).show();*/
                        JSONObject parsedJSONArray;
                        if(viewHolder.getLayoutPosition()+1 == 1){
                             parsedJSONArray = JSONParser.getInstance().getUpdatedPatientList("Billable Rounding");
                        }else{
                             parsedJSONArray = JSONParser.getInstance().getUpdatedPatientList("Signed Off");
                        }

                        String updatePatientList = ""+parsedJSONArray;
                        if(!TextUtils.isEmpty(updatePatientList)){
                            MobileApplication.getInstance().setBulkUpdatedList(updatePatientList);
                        }
                        MessageService.getInstance().startMessageService(getActivity(),"bulk");
                        sortByDialog.dismiss();
                        Log.d("TAG","ParsedJSON"+parsedJSONArray);
                    }

                    @Override
                    public void onTopEmptyRegionClick() {

                    }
                });
                dialogBuilder.setView(sortDialogView);
              /*  dialogBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });*/
                sortByDialog = dialogBuilder.create();
                sortByDialog.show();
            }
        });
        wearableListView.setAdapter(new WorkListAdapter(getActivity(), workList));
        WearableListView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        wearableListView.addItemDecoration(itemDecoration);
        wearableListView.setClickListener(mClickListener);
        wearableListView.addOnScrollListener(mOnScrollListener);


        return view;
    }

    private void populateWorkList(){
        workList = new ArrayList<WorkListDTO>();
        WorkListDTO temp;
        temp = new WorkListDTO();
        temp.setDay("SUN");
        temp.setDate("Oct 20,2015");
        temp.setPhysicianName("Ph. Dr.Physian1");
        temp.setRoomNum("c6");
        temp.setPatientName("Kumar K");
        temp.setBillingStatus("Billable Rounding");
        temp.setHospitalName("BPH-Hospital1");

        workList.add(temp);

         temp = new WorkListDTO();
        temp.setDay("MON");
        temp.setDate("Oct 21,2015");
        temp.setPhysicianName("Ph. Dr.Physian2");
        temp.setRoomNum("c7");
        temp.setPatientName("Kumar K1");
        temp.setBillingStatus("Billable Rounding");
        temp.setHospitalName("BPH-Hospital2");

        workList.add(temp);

       temp = new WorkListDTO();
        temp.setDay("TUE");
        temp.setDate("Oct 22,2015");
        temp.setPhysicianName("Ph. Dr.Physian1");
        temp.setRoomNum("c8");
        temp.setPatientName("Kumaar K");
        temp.setBillingStatus("Billable Rounding");
        temp.setHospitalName("BPH-Hospital2");

        workList.add(temp);

        temp = new WorkListDTO();
        temp.setDay("WED");
        temp.setDate("Oct 20,2015");
        temp.setPhysicianName("Ph. Dr.Physian1");
        temp.setRoomNum("c16");
        temp.setPatientName("Kummar K");
        temp.setBillingStatus("Billable Rounding");
        temp.setHospitalName("BPH-Hospital1");

        workList.add(temp);
    }



    // Handle our Wearable List's click events
    private WearableListView.ClickListener mClickListener =
            new WearableListView.ClickListener() {
                @Override
                public void onClick(WearableListView.ViewHolder viewHolder) {
                    WorkListItemLayout listViewRowView = (WorkListItemLayout) viewHolder.itemView;
                    String tag_clicked = (String)listViewRowView.getTag();
                /*    Toast.makeText(getActivity(),
                            String.format("You selected item #%s",
                                    viewHolder.getLayoutPosition()+1) +"TAG Clicked"+tag_clicked,
                            Toast.LENGTH_SHORT).show();
*/
                    getFragmentManager().beginTransaction()
                            .replace(R.id.framelayout, PatentDetailsFragment.newInstance(tag_clicked,"")).addToBackStack(null)
                            .commit();
                }

                @Override
                public void onTopEmptyRegionClick() {
                    Toast.makeText(getActivity(),
                            "Top empty area tapped", Toast.LENGTH_SHORT).show();
                }
            };

// header.setY(header.getY() - scroll);

    // The following code ensures that the title scrolls as the user scrolls up
    // or down the list
    private WearableListView.OnScrollListener mOnScrollListener =
            new WearableListView.OnScrollListener() {
                @Override
                public void onAbsoluteScrollChange(int i) {
                   /* // Only scroll the title up from its original base position
                    // and not down.
                    if (i > 0) {
                        listHeader.setY(-i);
                    }*/
                }

                @Override
                public void onScroll(int i) {
                    listHeader.setY(listHeader.getY() - i);
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


    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //  String message = intent.getStringExtra("message");
            //Log.v("myTag", "Main activity received message: " + message);


            //
            if(intent.hasExtra("result")) {
               String result_data  = intent.getStringExtra("result");
                if(result_data.equalsIgnoreCase("bulk")){
                    Log.d("TAG", "BulkResponse::::" + MobileApplication.getInstance().getBulkUpdateResponse());
              //      Toast.makeText(getActivity(),MobileApplication.getInstance().getBulkUpdateResponse(),Toast.LENGTH_LONG).show();
                }

            }

        }
    }
}
