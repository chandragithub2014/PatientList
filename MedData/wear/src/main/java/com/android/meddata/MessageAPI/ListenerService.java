package com.android.meddata.MessageAPI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.android.meddata.Application.MobileApplication;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Created by 245742 on 9/28/2015.
 */
public class ListenerService extends WearableListenerService {

    GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.i("test", "onMessageReceived()");
        if(messageEvent.getPath().equals("/message_path")) {
            final String message = new String(messageEvent.getData());
            Log.d("TAG","Received JSON Message:::"+message);
            if(message.contains("worklist")){
                String workList = message.substring(message.lastIndexOf("$") + 1);
                Log.d("TAG","WorkList:::"+workList);
                MobileApplication.getInstance().setPatientList(workList);
            }else if(message.contains("physicians")){
                String physiciansDetails = message.substring(message.lastIndexOf("$") + 1);
                Log.d("TAG","physiciansDetails:::"+physiciansDetails);
                MobileApplication.getInstance().setPhysicianList(physiciansDetails);
            }
            else if(message.contains("reminderlist")){
                String reminderList = message.substring(message.lastIndexOf("$") + 1);
                Log.d("TAG","reminderList:::"+reminderList);
                MobileApplication.getInstance().setReminderList(reminderList);
            }else if(message.contains("myaccount")){
                String accountDetails = message.substring(message.lastIndexOf("$") + 1);
                Log.d("TAG","accountDetails:::"+accountDetails);
                MobileApplication.getInstance().setAccountDetails(accountDetails);
            }else if(message.contains("locations")){
                String locationDetails = message.substring(message.lastIndexOf("$") + 1);
                Log.d("TAG","locationDetails:::"+locationDetails);
                MobileApplication.getInstance().setLocationList(locationDetails);
            }else if(message.contains("disposition")){
                String dispositionDetails = message.substring(message.lastIndexOf("$") + 1);
                Log.d("TAG","disposition:::"+dispositionDetails);
                MobileApplication.getInstance().setDispositionList(dispositionDetails);
            }else if(message.contains("billing")){
                String billingDetails = message.substring(message.lastIndexOf("$") + 1);
                Log.d("TAG","billingDetails:::"+billingDetails);
                MobileApplication.getInstance().setBillingList(billingDetails);
            }else if(message.contains("notestype")){
                String notestype = message.substring(message.lastIndexOf("$") + 1);
                Log.d("TAG","notestype:::"+notestype);
                MobileApplication.getInstance().setNotesType(notestype);
            }else if(message.contains("gender")){
                String genderDetails = message.substring(message.lastIndexOf("$") + 1);
                Log.d("TAG","genderDetails:::"+genderDetails);
                MobileApplication.getInstance().setGender(genderDetails);
            }else if(message.contains("bulk")){
                String bulkResponse = message.substring(message.lastIndexOf("$") + 1);
                Log.d("TAG","bulkResponse:::"+bulkResponse);
                MobileApplication.getInstance().setBulkUpdateResponse(bulkResponse);
                Intent messageIntent = new Intent();
                messageIntent.setAction(Intent.ACTION_SEND);
                messageIntent.putExtra("result","bulk");
                LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
            }else if(message.contains("accountUpdate")){
                String accountUpdateResponse = message.substring(message.lastIndexOf("$") + 1);
                Log.d("TAG","accountUpdate:::"+accountUpdateResponse);
                MobileApplication.getInstance().setAccount_update_response(accountUpdateResponse);
                Intent messageIntent = new Intent();
                messageIntent.setAction(Intent.ACTION_SEND);
                messageIntent.putExtra("result", "accountUpdate");
                LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
            }else if(message.contains("handoffSearch")){
                String handOffSearchResponse = message.substring(message.lastIndexOf("$") + 1);
                Log.d("TAG","handOffSearchResponse:::"+handOffSearchResponse);
                MobileApplication.getInstance().setHandsOffSearchResponse(handOffSearchResponse);
                Intent messageIntent = new Intent();
                messageIntent.setAction(Intent.ACTION_SEND);
                messageIntent.putExtra("result", "handoffSearch");
                LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
                //handoffSearch
            }else if(message.contains("handoffpatient")){
                String handOffPatientResponse = message.substring(message.lastIndexOf("$") + 1);
                Log.d("TAG","handOffPatientResponse:::"+handOffPatientResponse);
                MobileApplication.getInstance().setPatientHandOffResponse(handOffPatientResponse);
                Intent messageIntent = new Intent();
                messageIntent.setAction(Intent.ACTION_SEND);
                messageIntent.putExtra("result", "handoffpatient");
                LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
            }else if(message.contains("revertpatient")){
                String revertPatientResponse = message.substring(message.lastIndexOf("$") + 1);
                Log.d("TAG","revertpatientPatientResponse:::"+revertPatientResponse);
                MobileApplication.getInstance().setPatientRevertResponse(revertPatientResponse);
                Intent messageIntent = new Intent();
                messageIntent.setAction(Intent.ACTION_SEND);
                messageIntent.putExtra("result", "revertpatient");
                LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
            }else  if(message.contains("reminderCount")){
                String reminderCount = message.substring(message.lastIndexOf("$") + 1);
                Log.d("TAG","reminderCount:::"+reminderCount);
                Intent messageIntent = new Intent();
                messageIntent.setAction(Intent.ACTION_SEND);
                messageIntent.putExtra("reminder", reminderCount);
                LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
            }else  if(message.contains("patientlistCount")){
                String worklistCount = message.substring(message.lastIndexOf("$") + 1);
                Log.d("TAG","patientlistCount:::"+worklistCount);
                Intent messageIntent = new Intent();
                messageIntent.setAction(Intent.ACTION_SEND);
                messageIntent.putExtra("worklist", worklistCount);
                LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
            }
            else{
                MobileApplication.getInstance().setPropertiesJSON(message);
            }
        }else {
            super.onMessageReceived(messageEvent);
        }
    }

    @Override
    public void onPeerConnected(Node peer) {
        Log.d("ListenerService", "onPeerConnected: " + peer);
    }

    @Override
    public void onPeerDisconnected(Node peer) {
        Log.d("ListenerService", "onPeerDisconnected: " + peer);
    }

  @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);
        Log.d("TAG","OnDataChanged Wear");
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED &&
                    event.getDataItem().getUri().getPath().equals("/image")) {
                DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                Asset profileAsset = dataMapItem.getDataMap().getAsset("profileImage");

                  Bitmap bitmap = loadBitmapFromAsset(profileAsset);

                Intent messageIntent = new Intent();
                messageIntent.setAction(Intent.ACTION_SEND);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, bs);
                messageIntent.putExtra("byteArray", bs.toByteArray());

               // messageIntent.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);

       //         ProductDetailFragment.setImage(bitmap);
                // Do something with the bitmap
            }
        }
    }


    public Bitmap loadBitmapFromAsset(Asset asset) {
        if (asset == null) {
            throw new IllegalArgumentException("Asset must be non-null");
        }
        ConnectionResult result =
                mGoogleApiClient.blockingConnect(30, TimeUnit.MILLISECONDS);
        if (!result.isSuccess()) {
            return null;
        }
        // convert asset into a file descriptor and block until it's ready
        InputStream assetInputStream = Wearable.DataApi.getFdForAsset(
                mGoogleApiClient, asset).await().getInputStream();
        mGoogleApiClient.disconnect();

        if (assetInputStream == null) {
            Log.w("ListenerService", "Requested an unknown Asset.");
            return null;
        }
        // decode the stream into a bitmap
        return BitmapFactory.decodeStream(assetInputStream);
    }




}
