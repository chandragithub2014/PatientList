package com.android.meddata.WebServiceHelpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.meddata.Application.MobileApplication;
import com.android.meddata.interfaces.OMSReceiveListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import com.android.meddata.MedDataUtils.MedDataConstants;
/**
 * Created by CHANDRASAIMOHAN on 10/23/2015.
 */
public class MedDataPostAsyncTaskHelper extends AsyncTask<String,Void,String> {
Context ctx;
    public static ProgressDialog pd =null;
    private OMSReceiveListener inReceiveListener;
    String url;
    String jsonObject;
    JSONObject requestjsonObject;
    String type="";
    private boolean isFace=false;


    public MedDataPostAsyncTaskHelper(Context ctx, OMSReceiveListener inReceiveListener,String type){
        this.ctx=ctx;
        this.inReceiveListener=inReceiveListener;
        this.requestjsonObject=requestjsonObject;
        this.type = type;
        isFace = true;

    }


    public MedDataPostAsyncTaskHelper(Context ctx, OMSReceiveListener inReceiveListener,JSONObject requestjsonObject){
        this.ctx=ctx;
        this.inReceiveListener=inReceiveListener;
        this.requestjsonObject=requestjsonObject;
        pd = new ProgressDialog(ctx);
        pd.setMessage("Authorizing...");
        pd.setCancelable(false);
        pd.show();
    }

    public MedDataPostAsyncTaskHelper(Context ctx, OMSReceiveListener inReceiveListener,JSONObject requestjsonObject,String type){
        this.ctx=ctx;
        this.inReceiveListener=inReceiveListener;
        this.requestjsonObject=requestjsonObject;
        this.type = type;
        pd = new ProgressDialog(ctx);
        if(TextUtils.isEmpty(type)) {
            pd.setMessage("Authorizing..." + type);
        }else{
            pd.setMessage("Fetching..." + type);
        }
        pd.setCancelable(false);
        pd.show();
    }
    @Override
    protected String doInBackground(String... params) {
        String response="";
        url = params[0];
        if(!isFace) {
            response = doURLConnectionPost(url);
        }else{
            response = doURLConnectionPostForLogin("");
        }
        return response;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String response) {
        if(!TextUtils.isEmpty(response)){
            if(!isFace) {
                pd.dismiss();
            }
            Log.d("TAG", type+" Response::::" + response);
            String flag = parseResponse(response);
            inReceiveListener.receiveResult(flag);

        }
     //   super.onPostExecute(response);
    }

    private String parseResponse(String response){
        String result="";
        String flag="";
        try {
            response.replace("\\n", "");
            response.replace("\\t", "");
            if(!TextUtils.isEmpty(type) && type.equalsIgnoreCase("worklist")){
                MobileApplication.getInstance().setPatientList(response);
                flag = "worklist";
            }else if(!TextUtils.isEmpty(type) && type.equalsIgnoreCase("reminderlist")){
                MobileApplication.getInstance().setReminderList(response);
                flag = "reminderlist";
            }else if(!TextUtils.isEmpty(type) && type.equalsIgnoreCase("myaccount")){
                MobileApplication.getInstance().setAccountDetails(response);
                flag = "myaccount";
            }else if(!TextUtils.isEmpty(type) && type.equalsIgnoreCase("locations")){
                MobileApplication.getInstance().setLocationList(response);
                flag = "locations";
            }else if(!TextUtils.isEmpty(type) && type.equalsIgnoreCase("physicians")){
                MobileApplication.getInstance().setPhysicianList(response);
                flag = "physicians";
            }else if(!TextUtils.isEmpty(type) && type.equalsIgnoreCase("notestype")){
                MobileApplication.getInstance().setNotesType(response);
                flag = "notestype";
            }else if(!TextUtils.isEmpty(type) && type.equalsIgnoreCase("billing")){
                MobileApplication.getInstance().setBillingList(response);
                flag = "billing";
            }else if(!TextUtils.isEmpty(type) && type.equalsIgnoreCase("disposition")){
                MobileApplication.getInstance().setDispositionList(response);
                flag = "disposition";
            }else if(!TextUtils.isEmpty(type) && type.equalsIgnoreCase("gender")){
                MobileApplication.getInstance().setGender(response);
                flag = "gender";
            }else if(!TextUtils.isEmpty(type) && type.equalsIgnoreCase("bulk")){
                JSONObject responseJsonObject = new JSONObject(response);
                flag = responseJsonObject.getString("Flag");
                if(flag.equalsIgnoreCase("S")){
                    MobileApplication.getInstance().setBulkUpdateResponse("The account status has been updated");
                }else if(flag.equalsIgnoreCase("C")){
                    MobileApplication.getInstance().setBulkUpdateResponse("Please update the earlier encounters before closing this patient record.");
                }else if(flag.equalsIgnoreCase("E")){
                    MobileApplication.getInstance().setBulkUpdateResponse("Error");
                }
                flag="bulk";
            }else if(!TextUtils.isEmpty(type) && type.equalsIgnoreCase("accountUpdate")){
                JSONObject responseJsonObject = new JSONObject(response);
                flag = responseJsonObject.getString("Flag");
                if(flag.equalsIgnoreCase("S")){
                    MobileApplication.getInstance().setAccount_update_response("Transaction is done");
                }else  if(flag.equalsIgnoreCase("A")){
                    MobileApplication.getInstance().setAccount_update_response("Physician alreday exists");
                }else{
                    MobileApplication.getInstance().setAccount_update_response("Error");
                }
                flag="accountUpdate";
            }else if(!TextUtils.isEmpty(type) && type.equalsIgnoreCase("handoffSearch")){
                //handoffSearch
                JSONArray searchResponse = new JSONArray(response);
                if(searchResponse.length()>0){
                    MobileApplication.getInstance().setHandsOffSearchResponse("" + searchResponse);
                }else{
                    MobileApplication.getInstance().setHandsOffSearchResponse("No");
                }
                flag="handoffSearch";
            }else if(!TextUtils.isEmpty(type) && type.equalsIgnoreCase("handoffpatient")){
                //handoffpatient
                JSONObject responseJsonObject = new JSONObject(response);
                flag = responseJsonObject.getString("Flag");
                if(flag.equalsIgnoreCase("S")){
                    MobileApplication.getInstance().setPatientHandOffResponse("Patient handed Off");
                }else{
                    MobileApplication.getInstance().setPatientHandOffResponse("Error");
                }
                flag="handoffpatient";
            }else if(!TextUtils.isEmpty(type) && type.equalsIgnoreCase("revertpatient")){
                //handoffpatient
                JSONObject responseJsonObject = new JSONObject(response);
                flag = responseJsonObject.getString("Flag");
                if(flag.equalsIgnoreCase("S")){
                    MobileApplication.getInstance().setPatientRevertResponse("Patient Reverted");
                }else{
                    MobileApplication.getInstance().setPatientRevertResponse("Error");
                }
                flag="revertpatient";
            }
            //revertpatient
            else if( !TextUtils.isEmpty(type) && type.equalsIgnoreCase("reminderCount")){
                MobileApplication.getInstance().setReminderList(response);
                flag = "reminderCount";
            }
            else if( !TextUtils.isEmpty(type) && type.equalsIgnoreCase("worklistCount")){
                MobileApplication.getInstance().setPatientList(response);
                flag = "worklistCount";
            }else if( !TextUtils.isEmpty(type) && type.equalsIgnoreCase("notes")){
                MobileApplication.getInstance().setPatientNotes(response);
                flag = "notes";
            }
            else {
                JSONObject responseJsonObject = new JSONObject(response);
                flag = responseJsonObject.getString("Flag");
                if (flag.equalsIgnoreCase("Y")) {
                    MobileApplication.getInstance().setPropertiesJSON(response);
                }
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return flag;
    }
    private String doURLConnectionPost(String loginURL){
        /*Login_Id: "test", Password: "test"
        */

        String response="";
        //do this wherever you are wanting to POST
        URL url;
        HttpURLConnection conn;
        //String loginURL = "https://dev-patientlists.meddata.com/UserLoginService.svc/ValidateUser";
        try{
            url=new URL(loginURL);
            //you need to encode ONLY the values of the parameters
            String param="Login_Id=" + URLEncoder.encode("test1","UTF-8")+
            "&Password="+URLEncoder.encode("test@123","UTF-8");

            conn=(HttpURLConnection)url.openConnection();
            //set the output to true, indicating you are outputting(uploading) POST data
            conn.setDoOutput(true);
            conn.setChunkedStreamingMode(0);
//once you set the output to true, you don’t really need to set the request method to post, but I’m doing it anyway
            conn.setRequestMethod("POST");
       //     conn.setFixedLengthStreamingMode(param.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/json");
         //   conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();

        /*    JSONObject loginJsonObject = new JSONObject();
            loginJsonObject.put("Login_Id", "test1");
            loginJsonObject.put("Password", "test@123");
            JSONObject requestJsonObject = new JSONObject();
            requestJsonObject.put("request", loginJsonObject);*/
           /* //send the POST out
            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(URLEncoder.encode(requestJsonObject.toString(),"UTF-8"));
            out.flush();
            out.close();*/

            // Write serialized JSON data to output stream.
            OutputStream out = new BufferedOutputStream(conn.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(requestjsonObject.toString());

            // Close streams and disconnect.
            writer.close();
            out.close();
          //  urlConnection.disconnect();


            //build the string to store the response text from the server
          //  String response= “”;

//start listening to the stream
            Log.d("TAG","Response Code:::"+conn.getResponseCode());
            if(conn.getResponseCode() == 200) {
                Scanner inStream = new Scanner(conn.getInputStream());

//process the stream and store it in StringBuilder
                while (inStream.hasNextLine())
                    response += (inStream.nextLine());
            }
        }
        //catch some error
        catch(MalformedURLException ex){
            Toast.makeText(ctx, ex.toString(), Toast.LENGTH_LONG).show();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return response;
    }


    private String doURLConnectionPostForLogin(String loginURL){
        /*Login_Id: "test", Password: "test"

        */
        Log.d("TAG","In doURLConnectionPostForLogin()");
       if(MedDataConstants.USE_TEST_SERVICE){
       loginURL =   "https://test-patientlists.meddata.com/UserLoginService.svc/ValidateUser";
       }else {
           loginURL = "https://dev-patientlists.meddata.com/UserLoginService.svc/ValidateUser";
       }
        JSONObject requestJsonObject = new JSONObject();//
        try {
            JSONObject loginJsonObject = new JSONObject();
            loginJsonObject.put("Login_Id", MedDataConstants.LOGIN_ID);
            loginJsonObject.put("Password", MedDataConstants.LOGIN_PASS_WORD);
            //  JSONObject requestJsonObject = new JSONObject();//
            requestJsonObject.put("request", loginJsonObject);
            //  new MedDataPostAsyncTaskHelper(MainActivity.this, MainActivity.this, requestJsonObject).execute("https://dev-patientlists.meddata.com/UserLoginService.svc/ValidateUser");
        } catch (JSONException e) {
            e.printStackTrace();
            ;
        } catch (Exception e) {
            e.printStackTrace();
        }

        String response="";
        //do this wherever you are wanting to POST
        URL url;
        HttpURLConnection conn;
        //String loginURL = "https://dev-patientlists.meddata.com/UserLoginService.svc/ValidateUser";
        try{
            url=new URL(loginURL);
            //you need to encode ONLY the values of the parameters
            String param="Login_Id=" + URLEncoder.encode("test1","UTF-8")+
                    "&Password="+URLEncoder.encode("test@123","UTF-8");

            conn=(HttpURLConnection)url.openConnection();
            //set the output to true, indicating you are outputting(uploading) POST data
            conn.setDoOutput(true);
            conn.setChunkedStreamingMode(0);
//once you set the output to true, you don’t really need to set the request method to post, but I’m doing it anyway
            conn.setRequestMethod("POST");
            //     conn.setFixedLengthStreamingMode(param.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/json");
            //   conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();



            // Write serialized JSON data to output stream.
            OutputStream out = new BufferedOutputStream(conn.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(requestJsonObject.toString());

            // Close streams and disconnect.
            writer.close();
            out.close();
            //  urlConnection.disconnect();


            //build the string to store the response text from the server
            //  String response= “”;

//start listening to the stream
            Log.d("TAG","Response Code:::"+conn.getResponseCode());
            if(conn.getResponseCode() == 200) {
                Scanner inStream = new Scanner(conn.getInputStream());

//process the stream and store it in StringBuilder
                while (inStream.hasNextLine())
                    response += (inStream.nextLine());
            }
        }
        //catch some error
        catch(MalformedURLException ex){
            Toast.makeText(ctx, ex.toString(), Toast.LENGTH_LONG).show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        String finalResponse = "";
        Log.d("TAG","Login Response:::"+response);
        if(!TextUtils.isEmpty(response)) {
            finalResponse = parseLoginResponse(response);
        }
        return finalResponse;
    }


    private String parseLoginResponse(String response){
        Log.d("TAG","ParseLoginResponse");
        String result="";
        String flag="";
        String finalResponse="";
        try {
            response.replace("\\n", "");
            response.replace("\\t", "");

            //revertpatient
            //  else {
            JSONObject responseJsonObject = new JSONObject(response);
            flag = responseJsonObject.getString("Flag");
            Log.d("TAG","Login Response Flag::: &&& Type"+flag+"::::"+type);
            if (flag.equalsIgnoreCase("Y")) {
                MobileApplication.getInstance().setPropertiesJSON(response);
                if(!TextUtils.isEmpty(type) && type.equalsIgnoreCase("reminderCount")){
                    try {
                        String  globalJSON = MobileApplication.getInstance().getPropertiesJSON();
                        JSONObject jsonObject = new JSONObject(globalJSON);
                        String key = jsonObject.getString("Key");
                        int entityId = jsonObject.getInt("EntityID");
                        int locationId = jsonObject.getInt("Location");
                        JSONObject loginJsonObject = new JSONObject();
                        loginJsonObject.put("Key", key);
                        loginJsonObject.put("EntityID", -1);
                        loginJsonObject.put("Login_Id", MedDataConstants.LOGIN_ID);
                        loginJsonObject.put("LocationId", locationId);
                        JSONObject remiderRequestJsonObject = new JSONObject();
                        remiderRequestJsonObject.put("request", loginJsonObject);
                        requestjsonObject = remiderRequestJsonObject;
                        if(MedDataConstants.USE_TEST_SERVICE){
                            finalResponse = doURLConnectionPost("https://test-patientlists.meddata.com/PatientDetailsService.svc/GetReminders");
                        }else {
                            finalResponse = doURLConnectionPost("https://dev-patientlists.meddata.com/PatientDetailsService.svc/GetReminders");
                        }
                        //        new MedDataPostAsyncTaskHelper(MainActivity.this,MainActivity.this,requestJsonObject,"reminderlist").execute("https://dev-patientlists.meddata.com/PatientDetailsService.svc/GetReminders");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else if(!TextUtils.isEmpty(type) && type.equalsIgnoreCase("worklistCount")){
                    try {
                        String  globalJSON = MobileApplication.getInstance().getPropertiesJSON();
                        JSONObject jsonObject = new JSONObject(globalJSON);
                        String key = jsonObject.getString("Key");
                        int entityId = jsonObject.getInt("EntityID");
                        JSONObject loginJsonObject = new JSONObject();
                        loginJsonObject.put("Key", key);
                        loginJsonObject.put("EntityID", -1);
                        loginJsonObject.put("Login_Id", MedDataConstants.LOGIN_ID);
                        JSONObject requestJsonObject = new JSONObject();
                        requestJsonObject.put("request", loginJsonObject);
                        requestjsonObject = requestJsonObject;
                        if(MedDataConstants.USE_TEST_SERVICE){
                            finalResponse = doURLConnectionPost("https://test-patientlists.meddata.com/PatientDetailsService.svc/GetPatientsWorkList");
                        }else {
                            finalResponse = doURLConnectionPost("https://dev-patientlists.meddata.com/PatientDetailsService.svc/GetPatientsWorkList");
                        }

                        //        new MedDataPostAsyncTaskHelper(MainActivity.this,MainActivity.this,requestJsonObject,"reminderlist").execute("https://dev-patientlists.meddata.com/PatientDetailsService.svc/GetReminders");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            //  }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return finalResponse;
    }

}
