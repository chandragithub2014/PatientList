package com.android.meddata.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.WearableListView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.meddata.R;


/**
 * Created by CHANDRASAIMOHAN on 10/20/2015.
 */
public class WearableListItemLayout extends FrameLayout implements WearableListView.OnCenterProximityListener {

    private ImageView image;
    private TextView text;

    public WearableListItemLayout(Context context) {
        super(context);
        View.inflate(context, R.layout.list_item, this);
        image = (ImageView) findViewById(R.id.image);
        text = (TextView) findViewById(R.id.text);
    }

    @Override
    public void onCenterPosition(boolean b) {
        image.animate().scaleX(1.2f).scaleY(1.2f).alpha(1).setDuration(200);;
        text.animate().scaleX(1.2f).scaleY(1.2f).alpha(1).setDuration(200);;
    }

    @Override
    public void onNonCenterPosition(boolean b) {
        image.animate().scaleX(1f).scaleY(1f).alpha(0.6f).setDuration(200);
        text.animate().scaleX(1f).scaleY(1f).alpha(0.6f).setDuration(200);
    }

    public ImageView getImage() {
        return image;
    }

    public TextView getText() {
        return text;
    }
}
