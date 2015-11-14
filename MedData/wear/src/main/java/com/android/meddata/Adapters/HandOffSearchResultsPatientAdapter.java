package com.android.meddata.Adapters;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.meddata.MedDataDTO.HandOffResultDTO;

import java.util.List;

/**
 * Created by CHANDRASAIMOHAN on 11/13/2015.
 */
public class HandOffSearchResultsPatientAdapter extends WearableListView.Adapter{
    private final LayoutInflater mInflater;
    List<String> listItems;
    private List<HandOffResultDTO> handOffResultDTOList;
    Context ctx;
    boolean isRevert;

    public HandOffSearchResultsPatientAdapter(Context ctx, List<HandOffResultDTO> handOffResultDTOList,boolean isRevert) {
        mInflater = LayoutInflater.from(ctx);
        this.handOffResultDTOList=handOffResultDTOList;
        this.ctx = ctx;
        this.isRevert = isRevert;
    }

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WearableListView.ViewHolder(new HandOffSearchListItemLayout(ctx,isRevert));
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
        HandOffSearchListItemLayout listViewRowView = (HandOffSearchListItemLayout) holder.itemView;

        listViewRowView.getPatient_Name().setText(handOffResultDTOList.get(position).getPatientName());
        listViewRowView.getPhy_name().setText(handOffResultDTOList.get(position).getPrimPhy());
        listViewRowView.getLoca_name().setText(handOffResultDTOList.get(position).getHospitalLocation());
   //     Log.d("TAG", "Encounter Date Adapter::::" + handOffResultDTOList.get(position).getEncounterDate());
        listViewRowView.getEncounterDate().setText(handOffResultDTOList.get(position).getEncounterDate());
        listViewRowView.setTag(handOffResultDTOList.get(position));

    }

    @Override
    public int getItemCount() {
        return handOffResultDTOList.size();
    }
}
