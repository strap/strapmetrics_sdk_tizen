package com.straphq.sdk.tizen.example.oceansurvey;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.accessory.SA;
import com.samsung.android.sdk.accessory.SAPeerAgent;
import com.samsung.android.sdk.accessory.SASocket;
import com.straphq.sdk.tizen.StrapMetrics;
import com.straphq.sdk.tizen.dto.StrapMessageDTO;
import com.straphq.sdk.tizen.exception.StrapSDKException;
import com.straphq.sdk.tizen.interfaces.StrapTizenSDKMessageListener;

public class OceanSurveyFullyManagedService extends StrapMetrics {
    public static final String TAG = "OceanSurveyFullyService";

    public static final int SERVICE_CONNECTION_RESULT_OK = 0;

    public static final int CHANNEL_ID = 104;


    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public OceanSurveyFullyManagedService getService() {
            return OceanSurveyFullyManagedService.this;
        }
    }

    public OceanSurveyFullyManagedService() {
        super(TAG, CHANNEL_ID);
    }

    @Override
    protected void onFindPeerAgentResponse(SAPeerAgent arg0, int i) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onServiceConnectionResponse(SASocket arg0, int i) {
        // TODO Auto-generated method stub

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
                Log.d("SAP PROVIDER", "ON CREATE TRY BLOCK");
            } catch (SsdkUnsupportedException e) {
                // Error Handling
                Log.d("SAP PROVIDER", "ON CREATE TRY BLOCK ERROR UNSUPPORTED SDK");
            } catch (Exception e1) {
                Log.e(TAG, "Cannot initialize Accessory package.");
                e1.printStackTrace();
                 stopSelf();
            }
            addMessageListener(new StrapTizenSDKMessageListener() {

                @Override
                public void onMessage(StrapMessageDTO strapMessageDTO) {
                    Log.d("OnMessge", "OnMessge");
                    processReceivedData(strapMessageDTO);

                }

                @Override
                public void onError(StrapSDKException e) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onConnectionLost(StrapSDKException e) {
                    // TODO Auto-generated method stub

                }
            });
    }


}

