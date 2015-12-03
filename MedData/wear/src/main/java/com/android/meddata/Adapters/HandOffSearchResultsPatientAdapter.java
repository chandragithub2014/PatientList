package com.android.meddata.Adapters;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.meddata.MedDataDTO.HandOffResultDTO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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

        listViewRowView.getPatient_Name().setTag(handOffResultDTOList.get(position).getPatientId());
        listViewRowView.getPhy_name().setTag(handOffResultDTOList.get(position).getEncounterId());
        String encounterDate = handOffResultDTOList.get(position).getEncounterDate();
        listViewRowView.getEncounterDate().setText(encounterDate);
      if(!TextUtils.isEmpty(encounterDate)) {
      //\/Date(1448788245320-0800)\/
          Log.d("TAG", "Julian Date::::" + encounterDate);

          String results = encounterDate.replaceAll("^/Date\\(", "");

          results = results.substring(0, 13);
          long time = Long.parseLong(results);

          Date date = new Date(time);
          SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
          sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
          //  textView.setText(sdf.format(date));
          System.out.println("Time after converiosn::" + sdf.format(date));
          listViewRowView.getEncounterDate().setText(sdf.format(date));
      }


        //   int  patientID  = temp.getPatientId();
        Log.d("TAG","Tagged  Patient Id::"+listViewRowView.getPatient_Name().getTag());


    }

    @Override
    public int getItemCount() {
        return handOffResultDTOList.size();
    }
}
