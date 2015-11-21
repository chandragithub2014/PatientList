package com.android.meddata.MessageAPI;

import android.content.Intent;
import android.util.Log;

import com.android.meddata.Application.MobileApplication;
import com.android.meddata.MainActivity;
import com.android.meddata.WebServiceHelpers.MedDataPostAsyncTaskHelper;
import com.android.meddata.interfaces.OMSReceiveListener;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by 245742 on 9/28/2015.
 */
public class ListenerService extends WearableListenerService  implements OMSReceiveListener {
    private  String webServiceMessage;
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.i("test", "onMessageReceived()");
       if(messageEvent.getPath().equals("/message_path")) {
            final String message = new String(messageEvent.getData());
           webServiceMessage = message;
            Log.d("TAG", "Received url:::" + message);
            MobileApplication.getInstance().setMessage(message);
          if(message.equalsIgnoreCase("companion")) {
               // Bitmap bitmap  = getBitmapFromURL(message);
               Intent i = new Intent(this, MainActivity.class);
              i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(i);
          } else  if(message.contains("bulk")){
              String bulkList = message.substring(message.lastIndexOf("$") + 1);
              Log.d("TAG","bulkList:::"+bulkList);
              Intent i = new Intent(this, MainActivity.class);

              i.putExtra("BULK", "bulk");
              i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(i);
              MobileApplication.getInstance().setBulkUpdatedList(bulkList);
          } else  if(message.contains("accountUpdate")){
              String account_updated_details = message.substring(message.lastIndexOf("$") + 1);
              MobileApplication.getInstance().setUpdatedAccountDetails(account_updated_details);
              Log.d("TAG", "account_updated_details:::" + account_updated_details);
              Intent i = new Intent(this, MainActivity.class);

              i.putExtra("ACCOUNT_UPDATE", "accountUpdate");
              i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(i);


          }else  if(message.contains("handoffSearch")){
              String handsOff_search_json  = message.substring(message.lastIndexOf("$") + 1);
              MobileApplication.getInstance().setHandOffSearchJSON(handsOff_search_json);
              Log.d("TAG", "handsOff_search_json:::" + handsOff_search_json);
              Intent i = new Intent(this, MainActivity.class);

              i.putExtra("HANDOFF_SEARCH", "handoffSearch");
              i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(i);
          }else if(message.contains("handoffpatient")){
              String handoffpatient_json =  message.substring(message.lastIndexOf("$") + 1);
              MobileApplication.getInstance().setHandOffPatientJSON(handoffpatient_json);
              Log.d("TAG", "handoffpatient_json:::" + handoffpatient_json);
              Intent i = new Intent(this, MainActivity.class);
              i.putExtra("HANDOFF_PATIENT", "handoffpatient");
              i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(i);
          }else if(message.contains("revertpatient")){
              String  revertpatient_json =  message.substring(message.lastIndexOf("$") + 1);
              MobileApplication.getInstance().setPatientRevertJSON(revertpatient_json);
              Log.d("TAG", "revertpatient_json:::" + revertpatient_json);
              Intent i = new Intent(this, MainActivity.class);
              i.putExtra("REVERT_PATIENT", "revertpatient");
              i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(i);
          }else if(message.contains("reminderCount")){
              makeWebServiceCalls();
          }

        }else {
           super.onMessageReceived(messageEvent);
        }
    }

    /*@Override
    public void onDataChanged(DataEventBuffer dataEvents) {

        super.onDataChanged(dataEvents);
    }
*/
    private void makeWebServiceCalls(){
        if(webServiceMessage.equalsIgnoreCase("reminderCount")){
            new MedDataPostAsyncTaskHelper(ListenerService.this,ListenerService.this,"reminderCount").execute("https://dev-patientlists.meddata.com/PatientDetailsService.svc/GetReminders");
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("ListenerService","ListenerService Mobile:::::");
    }

    @Override
    public void receiveResult(String result) {
        Log.d("TAG","Receive Result:::"+result);
        if(result.equalsIgnoreCase("reminderCount")) {
            String reminderResponse = MobileApplication.getInstance().getReminderList();
            try {
                JSONArray jsonArray1 = new JSONArray(reminderResponse);
                MobileApplication.getInstance().setReminderCount(jsonArray1.length());
                MessageService.getInstance().startMessageService(getApplicationContext(), "reminderCount");
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
