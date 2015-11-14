package com.android.meddata.Adapters;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.meddata.R;


/**
 * Created by CHANDRASAIMOHAN on 10/20/2015.
 */
public class HandOffSearchListItemLayout extends FrameLayout implements WearableListView.OnCenterProximityListener {

  private TextView patient_Name,phy_name,loca_name,patient_Id,encounter_Id,encounterDate;
    ImageView detailImage;
private boolean isRevert;

    public HandOffSearchListItemLayout(Context context,boolean isRevert) {
        super(context);
        this.isRevert =isRevert;
        View.inflate(context, R.layout.handsoff_search_results_list_item_layout, this);
        patient_Name = (TextView) findViewById(R.id.patient_name);
        phy_name = (TextView) findViewById(R.id.doctor_name);
        loca_name = (TextView) findViewById(R.id.loc_name);
        detailImage = (ImageView)findViewById(R.id.detail);
        encounterDate = (TextView)findViewById(R.id.encounter_date);
        if(isRevert) {
            detailImage.setImageResource(R.drawable.revert);
        }
       /* patient_Id = (TextView) findViewById(R.id.patient_id);
        encounter_Id = (TextView) findViewById(R.id.encounter_id);*/

    }

    @Override
    public void onCenterPosition(boolean b) {
        patient_Name.animate().scaleX(1f).scaleY(1.2f).alpha(0.6f).setDuration(200);
        phy_name.animate().scaleX(1f).scaleY(1.2f).alpha(0.6f).setDuration(200);
        loca_name.animate().scaleX(1f).scaleY(1.2f).alpha(0.6f).setDuration(200);
        encounterDate.animate().scaleX(1f).scaleY(1.2f).alpha(0.6f).setDuration(200);
     /*   patient_Id.animate().scaleX(1f).scaleY(1.2f).alpha(0.6f).setDuration(200);
        encounter_Id.animate().scaleX(1f).scaleY(1.2f).alpha(0.6f).setDuration(200);*/




    }

    @Override
    public void onNonCenterPosition(boolean b) {
        patient_Name.animate().scaleX(1f).scaleY(1f).alpha(0.6f).setDuration(200);
        phy_name.animate().scaleX(1f).scaleY(1f).alpha(0.6f).setDuration(200);
        loca_name.animate().scaleX(1f).scaleY(1f).alpha(0.6f).setDuration(200);
        encounterDate.animate().scaleX(1f).scaleY(1f).alpha(0.6f).setDuration(200);

   /*     patient_Id.animate().scaleX(1f).scaleY(1f).alpha(0.6f).setDuration(200);
        encounter_Id.animate().scaleX(1f).scaleY(1f).alpha(0.6f).setDuration(200);*/


    }

    public TextView getPatient_Name() {
        return patient_Name;
    }

    public TextView getPhy_name() {
        return phy_name;
    }

    public TextView getLoca_name() {
        return loca_name;
    }

    public TextView getEncounterDate() {
        return encounterDate;
    }
/* public TextView getPatient_Id() {
        return patient_Id;
    }

    public TextView getEncounter_Id() {
        return encounter_Id;
    }*/
}
