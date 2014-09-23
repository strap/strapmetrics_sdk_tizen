package com.straphq.sdk.tizen.example.oceansurvey;

import android.content.Intent;
import android.os.IBinder;
import com.samsung.android.sdk.accessory.SAPeerAgent;
import com.samsung.android.sdk.accessory.SASocket;
import com.straphq.sdk.tizen.StrapMetrics;
import com.straphq.sdk.tizen.dto.StrapMessageDTO;
import com.straphq.sdk.tizen.exception.StrapSDKException;
import com.straphq.sdk.tizen.interfaces.StrapTizenSDKMessageListener;


public class OceanSurveyFullyManagedService extends StrapMetrics {

    public OceanSurveyFullyManagedService() {
        super("OceansSurveyAppProviderService", 23476);
    }

    @Override
    public void onCreate() {
        addMessageListener(new StrapTizenSDKMessageListener() {
            @Override
            public void onMessage(StrapMessageDTO strapMessageDTO) {
                processReceivedData(strapMessageDTO);
            }

            @Override
            public void onError(StrapSDKException e) {

            }

            @Override
            public void onConnectionLost(StrapSDKException e) {

            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onServiceConnectionResponse(SASocket saSocket, int i) {

    }

    @Override
    protected void onFindPeerAgentResponse(SAPeerAgent saPeerAgent, int i) {

    }
}
