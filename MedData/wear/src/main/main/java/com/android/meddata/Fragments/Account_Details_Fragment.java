package com.android.meddata.Fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.meddata.Application.MobileApplication;
import com.android.meddata.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link Account_Details_Fragment#} factory method to
 * create an instance of this fragment.
 */
public class Account_Details_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER



ImageView phyImg;
    View v =null;
EditText mandatory_name,mandatory_email;
    int mContainerId = -1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.account_details_form, container, false);
        mContainerId = container.getId();
        initLayout();
        Toolbar mToolBar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        ImageView back_img = (ImageView)mToolBar.findViewById(R.id.back);
        back_img.setVisibility(View.VISIBLE);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(mContainerId, new DashBoardWearableListFragment()).addToBackStack(null).commit();
            }
        });
        parseJSONForAccountDetails();
        return v;
    }

    private void initLayout(){
        phyImg = (ImageView)v.findViewById(R.id.imageView);
        RelativeLayout mandatory_layout = (RelativeLayout) v.findViewById(R.id.mandatory_name);
        mandatory_name = (EditText)mandatory_layout.findViewById(R.id.editText2);
        RelativeLayout mandatory_email_layout = (RelativeLayout) v.findViewById(R.id.email_layout);
        mandatory_email = (EditText)mandatory_email_layout.findViewById(R.id.email);
    }


private void parseJSONForAccountDetails(){
    String accontJson = MobileApplication.getInstance().getAccountDetails();
    Log.d("TAG", "Account JSON:::" + accontJson);
    if(!TextUtils.isEmpty(accontJson)){
        try {
            JSONObject accountDet = new JSONObject(accontJson);
            String name = accountDet.getString("Name");
            mandatory_name.setText(name);
            String email = accountDet.getString("EmailID");
            mandatory_email.setText(email);
            String imag  = accountDet.getString("ImageBase64");
         /*   byte[] imageAsBytes = Base64.decode(imag.getBytes(), Base64.DEFAULT);
            phyImg.setImageBitmap(
                    BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
            );*/
        }catch (JSONException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}



}
