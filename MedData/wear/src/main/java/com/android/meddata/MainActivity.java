package com.android.meddata;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.wearable.view.WatchViewStub;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.meddata.Fragments.DashBoardWearableListFragment;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        android.support.v7.widget.Toolbar toolbar = ( android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0,0);
        toolbar.setTitle("");
    //    toolbar.setContentInsetsAbsolute(0,0);
        setSupportActionBar(toolbar);

        LayoutInflater mInflater= LayoutInflater.from(getApplicationContext());
        View mCustomView = mInflater.inflate(R.layout.toolbar_custom_view, null);
        toolbar.addView(mCustomView);
        getFragmentManager().beginTransaction()
                .replace(R.id.framelayout, new DashBoardWearableListFragment())
                .commit();
    }


}
