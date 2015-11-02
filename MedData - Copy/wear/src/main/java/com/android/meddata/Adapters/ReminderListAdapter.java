package com.android.meddata.Adapters;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.meddata.MedDataDTO.RemindersDTO;
import com.android.meddata.MedDataDTO.WorkListDTO;

import java.util.List;

/**
 * Created by CHANDRASAIMOHAN on 10/20/2015.
 */
public class ReminderListAdapter extends WearableListView.Adapter {

    private List<Integer> mIcons;
    private final LayoutInflater mInflater;
    private List<RemindersDTO> remindersDTOList;
    Context ctx;

    public ReminderListAdapter(Context ctx, List<RemindersDTO> remindersDTOList){
        mInflater = LayoutInflater.from(ctx);
       this.remindersDTOList=remindersDTOList;
        this.ctx = ctx;
    }
    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WearableListView.ViewHolder(new ReminderListItemLayout(ctx));
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
        ReminderListItemLayout listViewRowView = (ReminderListItemLayout) holder.itemView;

        listViewRowView.getDayName().setText(remindersDTOList.get(position).getDay());
        listViewRowView.getDate().setText(remindersDTOList.get(position).getDateOfDay());
        listViewRowView.getPhysician().setText(remindersDTOList.get(position).getPrimaryPhysician());
        listViewRowView.getSecPhysician().setText(remindersDTOList.get(position).getSecPhysician());
        listViewRowView.getPatient().setText(remindersDTOList.get(position).getPatientName());
        listViewRowView.getHospital().setText(remindersDTOList.get(position).getHospitalName());

    }

    @Override
    public int getItemCount() {
        return remindersDTOList.size();
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
