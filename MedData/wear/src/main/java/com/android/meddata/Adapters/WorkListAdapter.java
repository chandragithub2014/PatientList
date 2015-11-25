package com.android.meddata.Adapters;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.meddata.MedDataDTO.WorkListDTO;

import java.util.List;

/**
 * Created by CHANDRASAIMOHAN on 10/20/2015.
 */
public class WorkListAdapter extends WearableListView.Adapter {

    private List<Integer> mIcons;
    private final LayoutInflater mInflater;
    private List<WorkListDTO> workList;
    Context ctx;

    public WorkListAdapter(Context ctx, List<WorkListDTO> workList){
        mInflater = LayoutInflater.from(ctx);
       this.workList=workList;
        this.ctx = ctx;
    }
    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WearableListView.ViewHolder(new WorkListItemLayout(ctx));
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
        WorkListItemLayout listViewRowView = (WorkListItemLayout) holder.itemView;

     //   listViewRowView.getDayName().setText(workList.get(position).getDay());
      //  listViewRowView.getDate().setText(workList.get(position).getDate());
      //  listViewRowView.getPhysician().setText(workList.get(position).getPhysicianName());
        listViewRowView.getRoom().setText(workList.get(position).getRoomNum());
        listViewRowView.getPatient().setText(workList.get(position).getPatientName());
       // listViewRowView.getBilling().setText(workList.get(position).getBillingStatus());
        listViewRowView.getHospital().setText(workList.get(position).getHospitalName());
        listViewRowView.setTag(workList.get(position).getEncounterId());

    }

    @Override
    public int getItemCount() {
        return workList.size();
    }

   /* private static class ItemViewHolder extends WearableListView.ViewHolder {
        private CircledImageView mCircledImageView;
        private TextView mItemTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mCircledImageView = (CircledImageView)
                    itemView.findViewById(R.id.circle);
            mItemTextView = (TextView) itemView.findViewById(R.id.name);
        }
    }*/
}
