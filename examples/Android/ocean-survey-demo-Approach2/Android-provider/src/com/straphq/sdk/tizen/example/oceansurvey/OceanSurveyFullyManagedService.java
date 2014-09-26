package com.straphq.sdk.tizen.example.oceansurvey;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONException;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.accessory.SA;
import com.samsung.android.sdk.accessory.SAAgent;
import com.samsung.android.sdk.accessory.SAPeerAgent;
import com.samsung.android.sdk.accessory.SASocket;
import com.straphq.sdk.tizen.StrapMetrics;
import com.straphq.sdk.tizen.dto.StrapMessageDTO;
import com.straphq.sdk.tizen.exception.StrapSDKException;
import com.straphq.sdk.tizen.interfaces.StrapTizenSDKMessageListener;

public class OceanSurveyFullyManagedService extends SAAgent{

      public static final String TAG = "OceanSurveyFullyManagedService";

      public final static int CHANNEL_ID = 123;

      HashMap<Integer, OceanSurveyFullyManagedServiceConnection> connectionMap = null;

      public static final String SEALION = "SEALION";
          public static final String DOLPHIN = "DOLPHIN";
          public static final String PELICAN = "PELICAN";

          int SEALION_COUNT = 0;
          int DOLPHIN_COUNT = 0;
          int PELICAN_COUNT = 0;

      private final IBinder mIBinder = new LocalBinder();

      public class LocalBinder extends Binder {
            public OceanSurveyFullyManagedService getService() {
            Log.d("OceanSurveyFullyManagedService", "LOCAL BINDER GET SERVICE");
            return OceanSurveyFullyManagedService.this;
            }
      }

      public OceanSurveyFullyManagedService() {
      super(TAG, OceanSurveyFullyManagedServiceConnection.class);
      Log.d("OceanSurveyFullyManagedService", "SERVICE CONSTRUCTOR");
      }

      public class OceanSurveyFullyManagedServiceConnection extends SASocket {
            private int mConnectionId;

            public OceanSurveyFullyManagedServiceConnection() {
            super(OceanSurveyFullyManagedServiceConnection.class.getName());
            Log.d("OceanSurveyFullyManagedService", "OCEANSURVEY CONNECTION CONSTRUCTOR");
            }

            @Override
            public void onError(int channelId, String errorString, int error) {
            Log.e(TAG, "Connection is not alive ERROR: " + errorString + "  " + error);
            Log.d("OceanSurveyFullyManagedService", "OCEANSURVEY CONNECTION ERROR " + errorString + " || " + error);
            }

            @Override
            public void onReceive(int channelId, byte[] data) {
            Log.d(TAG, "onReceive");
            if(StrapMetrics.canHandleMessage(data)){
            StrapMessageDTO strapMessageDTO;
            try {
            strapMessageDTO = new StrapMessageDTO(new String(data));
            StrapMetrics.logReceivedData(strapMessageDTO);
            } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            }
            }
            else
            {
            String message = new String(data);
            if(message.equals(SEALION)){
            SEALION_COUNT++;
            message += SEALION_COUNT;
            }else {
            }if(message.equals(DOLPHIN)){
            DOLPHIN_COUNT++;
            message += DOLPHIN_COUNT;
            }else if(message.equals(PELICAN)){
            PELICAN_COUNT++;
            message += PELICAN_COUNT;
            }

            final OceanSurveyFullyManagedServiceConnection uHandler = connectionMap.get(Integer.parseInt(String.valueOf(mConnectionId)));
            if(uHandler == null){
            Log.e(TAG,"Error, can not get OceanSurveyFullyManagedServiceConnection handler");
            return;
            }
            new Thread(new Runnable() {
            public void run() {
            try {
            uHandler.send(CHANNEL_ID, message.getBytes());
            } catch (IOException e) {
            e.printStackTrace();
            }
            }
            }).start();
            }
            }

            @Override
            protected void onServiceConnectionLost(int errorCode) {
            Log.e(TAG, "onServiceConectionLost  for peer = " + mConnectionId + "error code =" + errorCode);

            if (connectionMap != null) {
            connectionMap.remove(mConnectionId);
            }
            }
      }

            @Override
            public void onCreate() {
            super.onCreate();
            Log.i(TAG, "onCreate of Provider Service");
   	        SA mAccessory = new SA();
            try {
            mAccessory.initialize(this);
            Log.d("OceanSurveyFullyManagedService", "ON CREATE TRY BLOCK");
            } catch (SsdkUnsupportedException e) {
            // Error Handling
            Log.d("OceanSurveyFullyManagedService", "ON CREATE TRY BLOCK ERROR UNSUPPORTED SDK");
            } catch (Exception e1) {
            Log.e(TAG, "Cannot initialize Accessory package.");
            e1.printStackTrace();
            /*
            * Your application can not use Accessory package of Samsung
            * Mobile SDK. You application should work smoothly without using
            * gracefully (release resources, stop Service threads, close UI
            * thread, etc.)
            */
            stopSelf();                                                                    				                                                         				 * this SDK, or you may want to notify user and close your app
            }
            }


            @Override
            protected void onServiceConnectionRequested(SAPeerAgent peerAgent) {
            acceptServiceConnectionRequest(peerAgent);
            Log.d("OceanSurveyFullyManagedService", "CONNECTION REQUESTED : " + peerAgent.getAppName());
            }

            @Override
            protected void onFindPeerAgentResponse(SAPeerAgent peerAgent, int i) {
            Log.d(TAG, "onFindPeerAgentResponse  i =" + i);
            }


            @Override
            protected void onServiceConnectionResponse(SASocket thisConnection, int result) {
            if (result == CONNECTION_SUCCESS) {
            if (thisConnection != null) {
            OceanSurveyFullyManagedServiceConnection myConnection = (OceanSurveyFullyManagedServiceConnection) thisConnection;
            if (connectionMap == null) {
            connectionMap = new HashMap<Integer, OceanSurveyFullyManagedServiceConnection>();
            }
            myConnection.mConnectionId = (int) (System.currentTimeMillis() & 255);
            Log.d(TAG, "onServiceConnection connectionID = " + myConnection.mConnectionId);
            connectionMap.put(myConnection.mConnectionId, myConnection);
			Toast.makeText(getBaseContext(),"CONNECTION ESTABLISHED", Toast.LENGTH_LONG).show();
            } else {
            Log.e(TAG, "SASocket object is null");
            }
            } else if (result == CONNECTION_ALREADY_EXIST) {
            Log.e(TAG, "onServiceConnectionResponse, CONNECTION_ALREADY_EXIST");
            } else {
            Log.e(TAG, "onServiceConnectionResponse result error =" + result);
            }
            }

            @Override
            public IBinder onBind(Intent intent) {
            Log.d("OceanSurveyFullyManagedService", "onBIND");
            return mIBinder;
            }
}


