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
public class ReminderListItemLayout extends FrameLayout implements WearableListView.OnCenterProximityListener {

  private TextView dayName,date,primphysician,secphysician,patient,hospital;


    public ReminderListItemLayout(Context context) {
        super(context);
        View.inflate(context, R.layout.reminder_list_item, this);
        dayName = (TextView) findViewById(R.id.day);
        date = (TextView) findViewById(R.id.date);
        primphysician = (TextView) findViewById(R.id.prim_physician);
        secphysician = (TextView) findViewById(R.id.sec_physician);
        patient = (TextView) findViewById(R.id.patientName);
        hospital = (TextView) findViewById(R.id.hospitial_name);

    }

    @Override
    public void onCenterPosition(boolean b) {
        dayName.animate().scaleX(1f).scaleY(1.2f).alpha(0.6f).setDuration(200);
        date.animate().scaleX(1f).scaleY(1.2f).alpha(0.6f).setDuration(200);
        primphysician.animate().scaleX(1f).scaleY(1.2f).alpha(0.6f).setDuration(200);
        secphysician.animate().scaleX(1f).scaleY(1.2f).alpha(0.6f).setDuration(200);
        patient.animate().scaleX(1f).scaleY(1.2f).alpha(0.6f).setDuration(200);
        hospital.animate().scaleX(1f).scaleY(1.2f).alpha(0.6f).setDuration(200);


    }

    @Override
    public void onNonCenterPosition(boolean b) {
        dayName.animate().scaleX(1f).scaleY(1f).alpha(0.6f).setDuration(200);
        date.animate().scaleX(1f).scaleY(1f).alpha(0.6f).setDuration(200);
        primphysician.animate().scaleX(1f).scaleY(1f).alpha(0.6f).setDuration(200);
        patient.animate().scaleX(1f).scaleY(1f).alpha(0.6f).setDuration(200);
        secphysician.animate().scaleX(1f).scaleY(1f).alpha(0.6f).setDuration(200);
        hospital.animate().scaleX(1f).scaleY(1f).alpha(0.6f).setDuration(200);

    }

    public TextView getDayName() {
        return dayName;
    }

    public TextView getDate() {
        return date;
    }

    public TextView getPhysician() {
        return primphysician;
    }

    public TextView getPatient() {
        return patient;
    }

    public TextView getSecPhysician() {
        return secphysician;
    }

    public TextView getHospital() {
        return hospital;
    }


}
