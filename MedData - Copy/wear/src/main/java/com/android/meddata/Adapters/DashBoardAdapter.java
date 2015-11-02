package com.android.meddata.Adapters;

import android.content.Context;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.meddata.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CHANDRASAIMOHAN on 10/20/2015.
 */
public class DashBoardAdapter extends WearableListView.Adapter {

    private List<Integer> mIcons;
    private final LayoutInflater mInflater;
    private List<String> mTitles;
    Context ctx;

    public DashBoardAdapter(Context ctx,List<Integer> mIcons,List<String> mTitles){
        mInflater = LayoutInflater.from(ctx);
        this.mIcons = mIcons;
        this.mTitles = mTitles;
        this.ctx = ctx;
    }
    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WearableListView.ViewHolder(new WearableListItemLayout(ctx));
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
        WearableListItemLayout listViewRowView = (WearableListItemLayout) holder.itemView;


        listViewRowView.getImage().setImageResource(mIcons.get(position));
        listViewRowView.getText().setText(mTitles.get(position));
    }

    @Override
    public int getItemCount() {
        return mTitles.size();
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
