package com.android.meddata;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.meddata.WebServiceHelpers.MedDataPostAsyncTaskHelper;
import com.android.meddata.interfaces.OMSReceiveListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by CHANDRASAIMOHAN on 10/23/2015.
 */
public class CompanionActivity extends AppCompatActivity implements OMSReceiveListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent().getExtras()!=null) {
            String companionMessage = getIntent().getStringExtra("message");
            Log.d("TAG", "Companion Message:::" + companionMessage);
            if(companionMessage.equalsIgnoreCase("worklist")){
                try {
                    JSONObject loginJsonObject = new JSONObject();
                    loginJsonObject.put("Login_Id", "test1");
                    loginJsonObject.put("Password", "test@123");
                    JSONObject requestJsonObject = new JSONObject();
                    requestJsonObject.put("request", loginJsonObject);
                    new MedDataPostAsyncTaskHelper(CompanionActivity.this, CompanionActivity.this,requestJsonObject ).execute("https://dev-patientlists.meddata.com/UserLoginService.svc/ValidateUser");
                }
                catch(JSONException e){
                    e.printStackTrace();;
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void receiveResult(String result) {

    }
}
