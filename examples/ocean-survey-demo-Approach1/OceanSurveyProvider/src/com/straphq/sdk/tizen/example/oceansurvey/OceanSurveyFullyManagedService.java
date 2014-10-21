package com.straphq.sdk.tizen.example.oceansurvey;

import java.io.IOException;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.format.Time;
import android.util.Log;
import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.accessory.SA;
import com.samsung.android.sdk.accessory.SAPeerAgent;
import com.samsung.android.sdk.accessory.SASocket;
import com.straphq.sdk.tizen.StrapMetrics;
import com.straphq.sdk.tizen.dto.StrapMessageDTO;
import com.straphq.sdk.tizen.exception.StrapSDKException;
import com.straphq.sdk.tizen.impl.TizenConnectionImpl;
import com.straphq.sdk.tizen.interfaces.StrapTizenSDKMessageListener;

public class OceanSurveyFullyManagedService extends StrapMetrics {
    public static final String TAG = "OceanSurveyFullyManagedService";

    public static final int CHANNEL_ID = 104;

    TizenConnectionImpl.TizenConnectionListener mConnection;



    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public OceanSurveyFullyManagedService getService() {
            return OceanSurveyFullyManagedService.this;
        }
    }

    public OceanSurveyFullyManagedService() {
        super(TAG, CHANNEL_ID);
        Log.d("yo","hahahaha");
    }

    @Override
    protected void onFindPeerAgentResponse(SAPeerAgent saPeerAgent, int i) {
        // TODO Auto-generated method stub
        Log.d(TAG,"onFindPeerAgentResponse " + i);

    }

     @Override
     protected void onServiceConnectionResponse(SASocket thisConnection, int result) {
            if (result == CONNECTION_SUCCESS) {
                if (thisConnection != null) {
                    mConnection = (TizenConnectionImpl.TizenConnectionListener) thisConnection;
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
        // TODO Auto-generated method stub
        return mBinder;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        
         Log.i(TAG, "onCreate of smart view Provider Service");

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
                 stopSelf();
            }
            addMessageListener(new StrapTizenSDKMessageListener() {

                @Override
                public void onError(StrapSDKException error) {
                Log.e(TAG, "Connection is not alive ERROR: " + error.getMessage());

                }

                @Override
                public void onConnectionLost(StrapSDKException error) {
                Log.e(TAG, "Error on onServiceConectionLost " + error.getMessage());
                mConnection = null;
                }

                public void onMessage(byte[] data) {
                //return data from provider to consumer
                Time time = new Time();

    			time.set(System.currentTimeMillis());

                String timeStr = " " + String.valueOf(time.hour) + ":" + String.valueOf(time.minute) + ":"
                						+ String.valueOf(time.second);
                String strToUpdateUI = new String(data);

                final String message = strToUpdateUI.concat(timeStr);

                try {
                mConnection.send(CHANNEL_ID, message.getBytes());
                } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                }
                }

                @Override
                public void onStrapMessage(StrapMessageDTO strapMessageDTO) {
                processReceivedData(strapMessageDTO);
                }
            });
    }


}
