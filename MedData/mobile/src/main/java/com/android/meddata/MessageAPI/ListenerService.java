package com.android.meddata.MessageAPI;

import android.content.Intent;
import android.util.Log;

import com.android.meddata.Application.MobileApplication;
import com.android.meddata.CompanionActivity;
import com.android.meddata.MainActivity;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by 245742 on 9/28/2015.
 */
public class ListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.i("test", "onMessageReceived()");
       if(messageEvent.getPath().equals("/message_path")) {
            final String message = new String(messageEvent.getData());
            Log.d("TAG", "Received url:::" + message);
            MobileApplication.getInstance().setMessage(message);
          if(message.equalsIgnoreCase("companion")) {
               // Bitmap bitmap  = getBitmapFromURL(message);
               Intent i = new Intent(this, MainActivity.class);
              i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(i);
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

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("ListenerService","ListenerService Mobile:::::");
    }
}
