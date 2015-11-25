package com.android.meddata.Fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.meddata.Application.MobileApplication;
import com.android.meddata.JSONParser.JSONParser;
import com.android.meddata.MedDataDTO.LocationDTO;
import com.android.meddata.MedDataUtils.MedDataConstants;
import com.android.meddata.MessageAPI.MessageService;
import com.android.meddata.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link Account_Details_Fragment#} factory method to
 * create an instance of this fragment.
 */
public class Account_Details_Fragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER



ImageView phyImg;
    View v =null;
EditText mandatory_name,mandatory_email;
    int mContainerId = -1;
    TextView phyName;
    Spinner locationSpinner;
    HashMap<String,String> locationHash;
    HashMap<String,String> locationHashByLocId;
    Button account_save_btn;
    String email=null, name=null,phoneNum=null,userName=null;
    int currentLocation;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Register the local broadcast receiver
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(messageReceiver, messageFilter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.account_details_form, container, false);
        mContainerId = container.getId();
        parseJSONForAccountDetails();
        initLayout();
        Toolbar mToolBar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        ImageView back_img = (ImageView)mToolBar.findViewById(R.id.back);


        TextView toolbarTitle = (TextView)mToolBar.findViewById(R.id.title);
        toolbarTitle.setText("My Account");
        back_img.setVisibility(View.VISIBLE);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(mContainerId, new DashBoardWearableListFragment()).addToBackStack(null).commit();
            }
        });

        return v;
    }

    private void initLayout(){
        String default_location_selection = "";
        phyName = (TextView)v.findViewById(R.id.phy_name);
        if(!TextUtils.isEmpty(name)){
            phyName.setText(name);
        }
        account_save_btn = (Button)v.findViewById(R.id.save_btn);
        account_save_btn.setOnClickListener(this);
        RelativeLayout location_spinner_layout = (RelativeLayout)v.findViewById(R.id.location_layout);
        locationSpinner = (Spinner)location_spinner_layout.findViewById(R.id.location_spinner);
        List<LocationDTO> locList = JSONParser.getInstance().getLocationList(MobileApplication.getInstance().getLocationList());
        List<String> hospitalList = new ArrayList<String>();
        if (locList != null && locList.size() > 0) {
            locationHash = new HashMap<String,String>();
            for (int i = 0; i < locList.size(); i++) {
                hospitalList.add(locList.get(i).getListDesc());
                locationHash.put(locList.get(i).getListDesc(), locList.get(i).getLocId());
                if(locList.get(i).getLocId().equalsIgnoreCase(Integer.toString(currentLocation))){
                    default_location_selection = locList.get(i).getListDesc();
                }
            }
            if (hospitalList != null && hospitalList.size() > 0) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, hospitalList);
                locationSpinner.setAdapter(adapter);
                if(!TextUtils.isEmpty(default_location_selection)){
                    int spinnerPosition = adapter.getPosition(default_location_selection);
                    locationSpinner.setSelection(spinnerPosition);
                }
            }



            locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    String imc_met=locationSpinner.getSelectedItem().toString();
                    if(locationHash!=null && locationHash.size()>0){
                        currentLocation =Integer.parseInt( locationHash.get(imc_met));
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }
            });
        }

       /* phyImg = (ImageView)v.findViewById(R.id.imageView);
        RelativeLayout mandatory_layout = (RelativeLayout) v.findViewById(R.id.mandatory_name);
        mandatory_name = (EditText)mandatory_layout.findViewById(R.id.editText2);
        RelativeLayout mandatory_email_layout = (RelativeLayout) v.findViewById(R.id.email_layout);
        mandatory_email = (EditText)mandatory_email_layout.findViewById(R.id.email);*/
    }


private void parseJSONForAccountDetails(){
    String accontJson = MobileApplication.getInstance().getAccountDetails();
    Log.d("TAG", "Account JSON:::" + accontJson);
    if(!TextUtils.isEmpty(accontJson)){
        try {
            JSONObject accountDet = new JSONObject(accontJson);
             name = accountDet.getString("Name");
//            mandatory_name.setText(name);
            email = accountDet.getString("EmailID");
        //    mandatory_email.setText(email);
            String imag  = accountDet.getString("ImageBase64");
         /*   byte[] imageAsBytes = Base64.decode(imag.getBytes(), Base64.DEFAULT);
            phyImg.setImageBitmap(
                    BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
            );*/
            phoneNum = accountDet.getString("PhoneNumber");
            userName = accountDet.getString("UserName");
            currentLocation = accountDet.getInt("Location");
        }catch (JSONException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_btn:
                 postUpdatedAccount();
                break;
        }
    }

    public void postUpdatedAccount(){
        JSONObject accountUpdateJSON = prepareAccountUpdateJSON();
        if(accountUpdateJSON!=null){
            Log.d("TAG", "Account Details Update JSON:::" + accountUpdateJSON);
            MobileApplication.getInstance().setUpdatedAccountDetails("" + accountUpdateJSON);
            MessageService.getInstance().startMessageService(getActivity(), "accountUpdate");
        }
    }

    private JSONObject prepareAccountUpdateJSON(){
        String globalJSON = MobileApplication.getInstance().getPropertiesJSON();
        String key = "";
        try {
            JSONObject jsonObject = new JSONObject(globalJSON);
            key = jsonObject.getString("Key");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        JSONObject updateJSON = null;
        JSONObject    requestJsonObject = null;
        try {
            updateJSON = new JSONObject();
            updateJSON.put("LoginId", MedDataConstants.LOGIN_ID);
            updateJSON.put("Key",key);
            if(!TextUtils.isEmpty(name)){
                updateJSON.put("Name",name);
            }else{
                updateJSON.put("Name",null);
            }

            if(!TextUtils.isEmpty(email)){
                updateJSON.put("EmailID",email);
            }else{
                updateJSON.put("EmailID",null);
            }
            updateJSON.put("Specialization","Physician");

            if(!TextUtils.isEmpty(userName)){
                updateJSON.put("UserName",userName);
            }else{
                updateJSON.put("UserName",null);
            }

            if(!TextUtils.isEmpty(phoneNum)){
                updateJSON.put("PhoneNumber",phoneNum);
            }else{
                updateJSON.put("PhoneNumber",null);
            }

             updateJSON.put("Location",currentLocation);
            updateJSON.put("ImageID",null);
            updateJSON.put("ReqType","AU");
            updateJSON.put("EntityID","-1");

               requestJsonObject = new JSONObject();
            requestJsonObject.put("request", updateJSON);

        }catch (JSONException e){
            e.printStackTrace();
        }


        return requestJsonObject;
    }


    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //  String message = intent.getStringExtra("message");
            //Log.v("myTag", "Main activity received message: " + message);

            try {
            //
            if (intent.hasExtra("result")) {
                String result_data = intent.getStringExtra("result");
                if (result_data.equalsIgnoreCase("accountUpdate")) {
                    Log.d("TAG", "AccountUpdateResponse::::" + MobileApplication.getInstance().getAccount_update_response());
                    Toast.makeText(getActivity(), MobileApplication.getInstance().getAccount_update_response(), Toast.LENGTH_SHORT).show();
                    //      Toast.makeText(getActivity(),MobileApplication.getInstance().getBulkUpdateResponse(),Toast.LENGTH_LONG).show();
                }

            }
        }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
