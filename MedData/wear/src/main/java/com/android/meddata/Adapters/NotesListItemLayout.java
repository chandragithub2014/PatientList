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
public class NotesListItemLayout extends FrameLayout implements WearableListView.OnCenterProximityListener {


  private TextView phyName,notes,notesType,updatedDate;

    public NotesListItemLayout(Context context) {
        super(context);
        View.inflate(context, R.layout.notes_list_item, this);

        phyName =  (TextView) findViewById(R.id.phy_name);
        notes = (TextView) findViewById(R.id.patientnotes);
        notesType = (TextView) findViewById(R.id.patientnotesType);
        updatedDate = (TextView) findViewById(R.id.updated_date);


    }

    @Override
    public void onCenterPosition(boolean b) {
       // dayName.animate().scaleX(1f).scaleY(1.2f).alpha(0.6f).setDuration(200);
      //  date.animate().scaleX(1f).scaleY(1.2f).alpha(0.6f).setDuration(200);
      //  physician.animate().scaleX(1f).scaleY(1.2f).alpha(0.6f).setDuration(200);
        phyName.animate().scaleX(1f).scaleY(1.2f).alpha(0.6f).setDuration(200);
     //   billing.animate().scaleX(1f).scaleY(1.2f).alpha(0.6f).setDuration(200);
        notes.animate().scaleX(1f).scaleY(1.2f).alpha(0.6f).setDuration(200);
        notesType.animate().scaleX(1f).scaleY(1.2f).alpha(0.6f).setDuration(200);
        updatedDate.animate().scaleX(1f).scaleY(1.2f).alpha(0.6f).setDuration(200);

    }

    @Override
    public void onNonCenterPosition(boolean b) {
     //   dayName.animate().scaleX(1f).scaleY(1f).alpha(0.6f).setDuration(200);
     //   date.animate().scaleX(1f).scaleY(1f).alpha(0.6f).setDuration(200);
     //   physician.animate().scaleX(1f).scaleY(1f).alpha(0.6f).setDuration(200);
        phyName.animate().scaleX(1f).scaleY(1f).alpha(0.6f).setDuration(200);
     //   billing.animate().scaleX(1f).scaleY(1f).alpha(0.6f).setDuration(200);
        notes.animate().scaleX(1f).scaleY(1f).alpha(0.6f).setDuration(200);
        notesType.animate().scaleX(1f).scaleY(1f).alpha(0.6f).setDuration(200);
        updatedDate.animate().scaleX(1f).scaleY(1f).alpha(0.6f).setDuration(200);
    }

    public TextView getPhyName() {
        return phyName;
    }

    public TextView getNotes() {
        return notes;
    }

    public TextView getNotesType() {
        return notesType;
    }

    public TextView getUpdatedDate() {
        return updatedDate;
    }
}
