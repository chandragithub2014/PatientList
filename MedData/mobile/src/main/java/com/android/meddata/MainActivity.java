package com.android.meddata;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.android.meddata.Application.MobileApplication;
import com.android.meddata.MessageAPI.MessageService;
import com.android.meddata.WebServiceHelpers.MedDataPostAsyncTaskHelper;
import com.android.meddata.interfaces.OMSReceiveListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements OMSReceiveListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            JSONObject loginJsonObject = new JSONObject();
            loginJsonObject.put("Login_Id", "test1");
            loginJsonObject.put("Password", "test@123");
            JSONObject requestJsonObject = new JSONObject();
            requestJsonObject.put("request", loginJsonObject);
            new MedDataPostAsyncTaskHelper(MainActivity.this, MainActivity.this,requestJsonObject ).execute("https://dev-patientlists.meddata.com/UserLoginService.svc/ValidateUser");
        }
        catch(JSONException e){
            e.printStackTrace();;
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void receiveResult(String result) {
        String globalJSON="";
        Log.d("TAG", "Result:::" + result);
         if(result.equalsIgnoreCase("Y")){
             try {
                 globalJSON = MobileApplication.getInstance().getPropertiesJSON();
                  //new MessageService(MainActivity.this).startMessageService();
                 MessageService.getInstance().startMessageService(MainActivity.this,"");
                 JSONObject jsonObject = new JSONObject(globalJSON);
                 String key = jsonObject.getString("Key");
                 int entityId = jsonObject.getInt("EntityID");
                 JSONObject loginJsonObject = new JSONObject();
                 loginJsonObject.put("Key", key);
                 loginJsonObject.put("EntityID", entityId);
                 loginJsonObject.put("Login_Id", "test1");
                 JSONObject requestJsonObject = new JSONObject();
                 requestJsonObject.put("request", loginJsonObject);
                 new MedDataPostAsyncTaskHelper(MainActivity.this,MainActivity.this,requestJsonObject,"worklist").execute("https://dev-patientlists.meddata.com/PatientDetailsService.svc/GetPatientsWorkList");
             }catch (Exception e){
                 e.printStackTrace();
             }
         }else if(result.equalsIgnoreCase("worklist")){
             MessageService.getInstance().startMessageService(MainActivity.this,"worklist");
             try {
                 globalJSON = MobileApplication.getInstance().getPropertiesJSON();
                 JSONObject jsonObject = new JSONObject(globalJSON);
                 String key = jsonObject.getString("Key");
                 int entityId = jsonObject.getInt("EntityID");
                 int locationId = jsonObject.getInt("Location");
                 JSONObject loginJsonObject = new JSONObject();
                 loginJsonObject.put("Key", key);
                 loginJsonObject.put("EntityID", entityId);
                 loginJsonObject.put("Login_Id", "test1");
                 loginJsonObject.put("LocationId", locationId);
                 JSONObject requestJsonObject = new JSONObject();
                 requestJsonObject.put("request", loginJsonObject);
                 new MedDataPostAsyncTaskHelper(MainActivity.this,MainActivity.this,requestJsonObject,"reminderlist").execute("https://dev-patientlists.meddata.com/PatientDetailsService.svc/GetReminders");
             }catch (Exception e){
                 e.printStackTrace();
             }
         }else if(result.equalsIgnoreCase("reminderlist")){
             MessageService.getInstance().startMessageService(MainActivity.this,"reminderlist");
             try{
                 globalJSON = MobileApplication.getInstance().getPropertiesJSON();
                 JSONObject jsonObject = new JSONObject(globalJSON);
                 String key = jsonObject.getString("Key");
                 JSONObject loginJsonObject = new JSONObject();
                 loginJsonObject.put("Key", key);
                 loginJsonObject.put("LoginId", "test1");
                 JSONObject requestJsonObject = new JSONObject();
                 requestJsonObject.put("request", loginJsonObject);
                 new MedDataPostAsyncTaskHelper(MainActivity.this,MainActivity.this,requestJsonObject,"myaccount").execute("https://dev-patientlists.meddata.com/UserLoginService.svc/GetMyAccountDetails");
             }
             catch(Exception e){
                 e.printStackTrace();
             }
         }else if(result.equalsIgnoreCase("myaccount")){
             MessageService.getInstance().startMessageService(MainActivity.this,"myaccount");
         }
    }
}
