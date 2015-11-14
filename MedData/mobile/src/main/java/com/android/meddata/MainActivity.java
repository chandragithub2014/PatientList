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
    String intentExtra = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
if(getIntent().getExtras()!=null){

if(getIntent().getStringExtra("BULK")!=null){
    intentExtra = getIntent().getStringExtra("BULK");
    Log.d("TAG","Extra Message :::"+intentExtra);
}else if(getIntent().getStringExtra("ACCOUNT_UPDATE")!=null){
    intentExtra = getIntent().getStringExtra("ACCOUNT_UPDATE");
    Log.d("TAG","Extra Message :::account_update"+intentExtra);
}else if(getIntent().getStringExtra("HANDOFF_SEARCH")!=null){
    intentExtra = getIntent().getStringExtra("HANDOFF_SEARCH");
    Log.d("TAG","Extra Message :::handoffSearch"+intentExtra);
}else if(getIntent().getStringExtra("HANDOFF_PATIENT")!=null){
    intentExtra = getIntent().getStringExtra("HANDOFF_PATIENT");
    Log.d("TAG","Extra Message :::handoffpatient"+intentExtra);
}else if(getIntent().getStringExtra("REVERT_PATIENT")!=null){
    intentExtra = getIntent().getStringExtra("REVERT_PATIENT");
    Log.d("TAG","Extra Message :::revertpatient"+intentExtra);
}
}
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
        Log.d("TAG","In OnResume()");
        if(intentExtra.equalsIgnoreCase("bulk")){
            try {
                JSONObject bulkList = new JSONObject(MobileApplication.getInstance().getBulkUpdatedList());
                   Log.d("TAG", "Bulk.......");
                new MedDataPostAsyncTaskHelper(MainActivity.this, MainActivity.this, bulkList,"bulk").execute("https://dev-patientlists.meddata.com/PatientDetailsService.svc/SetDispositionAndPhysician");
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }else  if(intentExtra.equalsIgnoreCase("accountUpdate")){
          try{
              JSONObject accountUpdateList = new JSONObject(MobileApplication.getInstance().getUpdatedAccountDetails());
              new MedDataPostAsyncTaskHelper(MainActivity.this, MainActivity.this, accountUpdateList,"accountUpdate").execute("https://dev-patientlists.meddata.com/UserLoginService.svc/UpdateMyAccount");
          }catch (JSONException e){
              e.printStackTrace();
          }
        }else if(intentExtra.equalsIgnoreCase("handoffSearch")){
            try{
                JSONObject accountUpdateList = new JSONObject(MobileApplication.getInstance().getHandOffSearchJSON());
                new MedDataPostAsyncTaskHelper(MainActivity.this, MainActivity.this, accountUpdateList,"handoffSearch").execute("https://dev-patientlists.meddata.com/PatientDetailsService.svc/GetPatientForTransferOrRevert");
            }catch (JSONException e){
                e.printStackTrace();
            }

        }else if(intentExtra.equalsIgnoreCase("handoffpatient")){
              try{
                  JSONObject handOffPatientJSON = new JSONObject(MobileApplication.getInstance().getHandOffPatientJSON());
                  new MedDataPostAsyncTaskHelper(MainActivity.this, MainActivity.this, handOffPatientJSON,"handoffpatient").execute("https://dev-patientlists.meddata.com/PatientDetailsService.svc/CreateTransfers");
                  //https://dev-patientlists.meddata.com/PatientDetailsService.svc/CreateTransfers
              }catch (JSONException e){
                  e.printStackTrace();
              }
        }else if(intentExtra.equalsIgnoreCase("revertpatient")){
            try{
                JSONObject handOffPatientJSON = new JSONObject(MobileApplication.getInstance().getPatientRevertJSON());
                new MedDataPostAsyncTaskHelper(MainActivity.this, MainActivity.this, handOffPatientJSON,"revertpatient").execute("https://dev-patientlists.meddata.com/PatientDetailsService.svc/RevertTransfers");
                //https://dev-patientlists.meddata.com/PatientDetailsService.svc/CreateTransfers
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        else {
            try {
                JSONObject loginJsonObject = new JSONObject();
                loginJsonObject.put("Login_Id", "veereshm");
                loginJsonObject.put("Password", "test@123");
                JSONObject requestJsonObject = new JSONObject();//
                requestJsonObject.put("request", loginJsonObject);
                new MedDataPostAsyncTaskHelper(MainActivity.this, MainActivity.this, requestJsonObject).execute("https://dev-patientlists.meddata.com/UserLoginService.svc/ValidateUser");
            } catch (JSONException e) {
                e.printStackTrace();
                ;
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                 loginJsonObject.put("EntityID", -1);
                 loginJsonObject.put("Login_Id", "veereshm");
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
                 loginJsonObject.put("EntityID", -1);
                 loginJsonObject.put("Login_Id", "veereshm");
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
                 loginJsonObject.put("LoginId", "veereshm");
                 JSONObject requestJsonObject = new JSONObject();
                 requestJsonObject.put("request", loginJsonObject);
                 new MedDataPostAsyncTaskHelper(MainActivity.this,MainActivity.this,requestJsonObject,"myaccount").execute("https://dev-patientlists.meddata.com/UserLoginService.svc/GetMyAccountDetails");
             }
             catch(Exception e){
                 e.printStackTrace();
             }
         }else if(result.equalsIgnoreCase("myaccount")){
             MessageService.getInstance().startMessageService(MainActivity.this,"myaccount");
             try{
                 globalJSON = MobileApplication.getInstance().getPropertiesJSON();
                 JSONObject jsonObject = new JSONObject(globalJSON);
                 String key = jsonObject.getString("Key");
                 JSONObject loginJsonObject = new JSONObject();
                 loginJsonObject.put("Key", key);
                 loginJsonObject.put("Login_Id", "veereshm");
                 loginJsonObject.put("ListType", "LOC");
                 JSONObject requestJsonObject = new JSONObject();
                 requestJsonObject.put("request", loginJsonObject);
                 new MedDataPostAsyncTaskHelper(MainActivity.this,MainActivity.this,requestJsonObject,"locations").execute("https://dev-patientlists.meddata.com/PatientDetailsService.svc/GetList");
             }
             catch(Exception e){
                 e.printStackTrace();
             }
         }else if(result.equalsIgnoreCase("locations")){
             MessageService.getInstance().startMessageService(MainActivity.this,"locations");
             try{
                 globalJSON = MobileApplication.getInstance().getPropertiesJSON();
                 JSONObject jsonObject = new JSONObject(globalJSON);
                 String key = jsonObject.getString("Key");
                 JSONObject loginJsonObject = new JSONObject();
                 loginJsonObject.put("Key", key);
                 loginJsonObject.put("Login_Id", "veereshm");
                 loginJsonObject.put("ListType", "PHYS");
                 JSONObject requestJsonObject = new JSONObject();
                 requestJsonObject.put("request", loginJsonObject);
                 new MedDataPostAsyncTaskHelper(MainActivity.this,MainActivity.this,requestJsonObject,"physicians").execute("https://dev-patientlists.meddata.com/PatientDetailsService.svc/GetList");
             } catch(Exception e){
                 e.printStackTrace();
             }
         }else if(result.equalsIgnoreCase("physicians")){
             MessageService.getInstance().startMessageService(MainActivity.this,"physicians");
             try{
                 globalJSON = MobileApplication.getInstance().getPropertiesJSON();
                 JSONObject jsonObject = new JSONObject(globalJSON);
                 String key = jsonObject.getString("Key");
                 JSONObject loginJsonObject = new JSONObject();
                 loginJsonObject.put("Key", key);
                 loginJsonObject.put("Login_Id", "veereshm");
                 loginJsonObject.put("ListType", "BILLING");
                 JSONObject requestJsonObject = new JSONObject();
                 requestJsonObject.put("request", loginJsonObject);
                 new MedDataPostAsyncTaskHelper(MainActivity.this,MainActivity.this,requestJsonObject,"billing").execute("https://dev-patientlists.meddata.com/PatientDetailsService.svc/GetList");
             } catch(Exception e){
                 e.printStackTrace();
             }
         }else if(result.equalsIgnoreCase("billing")){
             MessageService.getInstance().startMessageService(MainActivity.this,"billing");
             try{
                 globalJSON = MobileApplication.getInstance().getPropertiesJSON();
                 JSONObject jsonObject = new JSONObject(globalJSON);
                 String key = jsonObject.getString("Key");
                 JSONObject loginJsonObject = new JSONObject();
                 loginJsonObject.put("Key", key);
                 loginJsonObject.put("Login_Id", "veereshm");
                 loginJsonObject.put("ListType", "DISPOSITION");
                 JSONObject requestJsonObject = new JSONObject();
                 requestJsonObject.put("request", loginJsonObject);
                 new MedDataPostAsyncTaskHelper(MainActivity.this,MainActivity.this,requestJsonObject,"disposition").execute("https://dev-patientlists.meddata.com/PatientDetailsService.svc/GetList");
             } catch(Exception e){
                 e.printStackTrace();
             }
         }else if(result.equalsIgnoreCase("disposition")){
             MessageService.getInstance().startMessageService(MainActivity.this,"disposition");
             try{
                 globalJSON = MobileApplication.getInstance().getPropertiesJSON();
                 JSONObject jsonObject = new JSONObject(globalJSON);
                 String key = jsonObject.getString("Key");
                 JSONObject loginJsonObject = new JSONObject();
                 loginJsonObject.put("Key", key);
                 loginJsonObject.put("Login_Id", "veereshm");
                 loginJsonObject.put("ListType", "GENDER");
                 JSONObject requestJsonObject = new JSONObject();
                 requestJsonObject.put("request", loginJsonObject);
                 new MedDataPostAsyncTaskHelper(MainActivity.this,MainActivity.this,requestJsonObject,"gender").execute("https://dev-patientlists.meddata.com/PatientDetailsService.svc/GetList");
             } catch(Exception e){
                 e.printStackTrace();
             }
         }else if(result.equalsIgnoreCase("gender")){
             MessageService.getInstance().startMessageService(MainActivity.this,"gender");
             try{
                 globalJSON = MobileApplication.getInstance().getPropertiesJSON();
                 JSONObject jsonObject = new JSONObject(globalJSON);
                 String key = jsonObject.getString("Key");
                 JSONObject loginJsonObject = new JSONObject();
                 loginJsonObject.put("Key", key);
                 loginJsonObject.put("Login_Id", "veereshm");
                 loginJsonObject.put("ListType", "NotesTypes");
                 JSONObject requestJsonObject = new JSONObject();
                 requestJsonObject.put("request", loginJsonObject);
                 new MedDataPostAsyncTaskHelper(MainActivity.this,MainActivity.this,requestJsonObject,"notestype").execute("https://dev-patientlists.meddata.com/PatientDetailsService.svc/GetList");
             } catch(Exception e){
                 e.printStackTrace();
             }

         } else if(result.equalsIgnoreCase("notestype")){
             MessageService.getInstance().startMessageService(MainActivity.this,"notestype");
         }else if(result.equalsIgnoreCase("bulk")){
             MessageService.getInstance().startMessageService(MainActivity.this,"bulk");
         }else if(result.equalsIgnoreCase("accountUpdate")){
             MessageService.getInstance().startMessageService(MainActivity.this,"accountUpdate");
         }else if(result.equalsIgnoreCase("handoffSearch")){
             MessageService.getInstance().startMessageService(MainActivity.this,"handoffSearch");
         }else if(result.equalsIgnoreCase("handoffpatient")){
             MessageService.getInstance().startMessageService(MainActivity.this,"handoffpatient");
         }else if(result.equalsIgnoreCase("revertpatient")){
             MessageService.getInstance().startMessageService(MainActivity.this,"revertpatient");
         }
        //revertpatient
    }
}
