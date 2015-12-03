package com.android.meddata.Adapters;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.meddata.MedDataDTO.NotesDTO;
import com.android.meddata.MedDataDTO.WorkListDTO;

import java.util.List;

/**
 * Created by 245742 on 12/3/2015.
 */
public class PatientNotesAdapter extends WearableListView.Adapter {

    private List<Integer> mIcons;
    private final LayoutInflater mInflater;
    private List<NotesDTO> notesList;
    Context ctx;

    public PatientNotesAdapter(Context ctx, List<NotesDTO> notesList){
        mInflater = LayoutInflater.from(ctx);
        this.notesList=notesList;
        this.ctx = ctx;
    }

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WearableListView.ViewHolder(new NotesListItemLayout(ctx));
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
        NotesListItemLayout listViewRowView = (NotesListItemLayout) holder.itemView;
        listViewRowView.getNotes().setText(notesList.get(position).getPatientNotes());
        listViewRowView.getNotesType().setText(notesList.get(position).getNotesType());
        listViewRowView.getPhyName().setText(notesList.get(position).getPhysician());
        listViewRowView.getUpdatedDate().setText(notesList.get(position).getUpdatedDate());

    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

}
