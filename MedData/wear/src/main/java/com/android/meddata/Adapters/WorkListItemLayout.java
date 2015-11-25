package com.android.meddata.Adapters;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.meddata.R;


/**
 * Created by CHANDRASAIMOHAN on 10/20/2015.
 */
public class WorkListItemLayout extends FrameLayout implements WearableListView.OnCenterProximityListener {

  private TextView dayName,date,physician,patient,billing,hospital,room;


    public WorkListItemLayout(Context context) {
        super(context);
        View.inflate(context, R.layout.work_list_item, this);
     //   dayName = (TextView) findViewById(R.id.day);
      //  date = (TextView) findViewById(R.id.date);
      //  physician = (TextView) findViewById(R.id.physician);
        patient = (TextView) findViewById(R.id.patientName);
     //   billing = (TextView) findViewById(R.id.billing);
        hospital = (TextView) findViewById(R.id.hospitial_name);
        room = (TextView) findViewById(R.id.room);
    }

    @Override
    public void onCenterPosition(boolean b) {
       // dayName.animate().scaleX(1f).scaleY(1.2f).alpha(0.6f).setDuration(200);
      //  date.animate().scaleX(1f).scaleY(1.2f).alpha(0.6f).setDuration(200);
      //  physician.animate().scaleX(1f).scaleY(1.2f).alpha(0.6f).setDuration(200);
        patient.animate().scaleX(1f).scaleY(1.2f).alpha(0.6f).setDuration(200);
     //   billing.animate().scaleX(1f).scaleY(1.2f).alpha(0.6f).setDuration(200);
        hospital.animate().scaleX(1f).scaleY(1.2f).alpha(0.6f).setDuration(200);
        room.animate().scaleX(1f).scaleY(1.2f).alpha(0.6f).setDuration(200);

    }

    @Override
    public void onNonCenterPosition(boolean b) {
     //   dayName.animate().scaleX(1f).scaleY(1f).alpha(0.6f).setDuration(200);
     //   date.animate().scaleX(1f).scaleY(1f).alpha(0.6f).setDuration(200);
     //   physician.animate().scaleX(1f).scaleY(1f).alpha(0.6f).setDuration(200);
        patient.animate().scaleX(1f).scaleY(1f).alpha(0.6f).setDuration(200);
     //   billing.animate().scaleX(1f).scaleY(1f).alpha(0.6f).setDuration(200);
        hospital.animate().scaleX(1f).scaleY(1f).alpha(0.6f).setDuration(200);
        room.animate().scaleX(1f).scaleY(1f).alpha(0.6f).setDuration(200);
    }

    public TextView getDayName() {
        return dayName;
    }

    public TextView getDate() {
        return date;
    }

    public TextView getPhysician() {
        return physician;
    }

    public TextView getPatient() {
        return patient;
    }

    public TextView getBilling() {
        return billing;
    }

    public TextView getHospital() {
        return hospital;
    }

    public TextView getRoom() {
        return room;
    }
}
