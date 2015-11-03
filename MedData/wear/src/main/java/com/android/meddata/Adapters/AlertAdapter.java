package com.android.meddata.Adapters;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.meddata.R;

import java.util.List;

/**
 * Created by CHANDRASAIMOHAN on 11/4/2015.
 */
public class AlertAdapter extends WearableListView.Adapter {
    private final LayoutInflater mInflater;
    List<String> listItems;

    public AlertAdapter(Context context,List<String> listItems) {
        mInflater = LayoutInflater.from(context);
        this.listItems=listItems;
    }

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WearableListView.ViewHolder(
                mInflater.inflate(R.layout.row_simple_item_layout, null));
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
        TextView view = (TextView) holder.itemView.findViewById(R.id.textView);
        view.setText(listItems.get(position).toString());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
}
