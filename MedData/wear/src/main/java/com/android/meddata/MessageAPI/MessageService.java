package com.android.meddata.MessageAPI;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.android.meddata.Application.MobileApplication;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by CHANDRASAIMOHAN on 10/23/2015.
 */
public class MessageService implements  GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener  {
    Context ctx;
    GoogleApiClient mGoogleApiClient;
    private static MessageService instance;
   String message="companion";
    private MessageService(){

    }

    public static MessageService getInstance(){
        if(instance == null){
            instance = new MessageService();
        }
        return instance;
    }
 /*  public MessageService(Context ctx){
       this.ctx=ctx;
   }*/
    @Override
    public void onConnected(Bundle bundle) {
        if(mGoogleApiClient.isConnected()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
                    for(Node node : nodes.getNodes()) {
                        MessageApi.SendMessageResult result = null;
                        if(message.equalsIgnoreCase("bulk")){
                            Log.d("TAG","Send message when bulk");
                            result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/message_path", ("bulk"+"$"+ MobileApplication.getInstance().getBulkUpdatedList()).getBytes()).await();
                        }else if(message.equalsIgnoreCase("accountUpdate")){
                            Log.d("TAG","Send message when accountUpdate");
                            result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/message_path", ("accountUpdate"+"$"+ MobileApplication.getInstance().getUpdatedAccountDetails()).getBytes()).await();
                        }else if(message.equalsIgnoreCase("handoffSearch")){
                            //handoffSearch
                            Log.d("TAG","Send message when handoffSearch");
                            result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/message_path", ("handoffSearch"+"$"+ MobileApplication.getInstance().getHandOffSearchJSON()).getBytes()).await();
                        }else  if(message.equalsIgnoreCase("handoffpatient")){
                            Log.d("TAG","Send message when handoffpatient");
                            result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/message_path", ("handoffpatient"+"$"+ MobileApplication.getInstance().getHandOffPatientJSON()).getBytes()).await();
                        }else  if(message.equalsIgnoreCase("revertpatient")){
                            Log.d("TAG","Send message when revertpatient");
                            result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/message_path", ("revertpatient"+"$"+ MobileApplication.getInstance().getPatientRevertJSON()).getBytes()).await();
                        }else  if(message.equalsIgnoreCase("reminderCount")) {
                            result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/message_path", message.getBytes()).await();
                        }else if(message.equalsIgnoreCase("notes")){
                            Log.d("TAG","Send message when Notes");
                            result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/message_path", ("notes"+"$"+ MobileApplication.getInstance().getPatientNotes()).getBytes()).await();
                        }
                        else {
                            result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/message_path", message.getBytes()).await();
                        }
                        if(!result.getStatus().isSuccess()){
                            Log.e("test", "error");
                        } else {
                            Log.i("test", "success!! sent to: " + node.getDisplayName());
                        }
                    }
                }
            }).start();

        } else {
            Log.e("test", "not connected");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void startMessageService(Context ctx,String message){
        this.ctx=ctx;
        this.message=message;
        mGoogleApiClient = new GoogleApiClient.Builder(ctx)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    public void stopMessageService(Context ctx){
        this.ctx=ctx;
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }
}
