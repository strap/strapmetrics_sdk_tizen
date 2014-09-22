package com.straphq.sdk.tizen;


import com.straphq.sdk.tizen.dto.StrapMessageDTO;
import com.straphq.sdk.tizen.exception.StrapSDKException;
import com.straphq.sdk.tizen.impl.TizenConnectionImpl;
import com.straphq.sdk.tizen.interfaces.StrapSDK;
import com.straphq.sdk.tizen.interfaces.StrapTizenSDKMessageListener;

import java.util.ArrayList;

public class StrapMetrics extends TizenConnectionImpl implements StrapSDK {

    private static final Integer DEFAULT_CHANNEL_ID = 835462;

    public StrapMetrics (android.content.Context context, Integer channelId){
        setChannelId(channelId);
        setContext(context);
    }

    public StrapMetrics(android.content.Context context){
        setChannelId(DEFAULT_CHANNEL_ID);
        setContext(context);
    }

    private ArrayList<StrapTizenSDKMessageListener> messageListeners = new ArrayList<StrapTizenSDKMessageListener>();

    @Override
    public void addMessageListener(StrapTizenSDKMessageListener strapTizenSDKMessageListener) {
        messageListeners.add(strapTizenSDKMessageListener);
    }

    @Override
    public void processReceivedData(StrapMessageDTO strapMessageDTO) {
       strapSDKUtils.processReceivedData(strapMessageDTO);
    }

    @Override
    public void eventOnMessage(StrapMessageDTO strapMessageDTO) {
        for(StrapTizenSDKMessageListener listener : messageListeners ){
            listener.onMessage(strapMessageDTO);
        }
    }

    @Override
    public void eventOnError(StrapSDKException strapSDKException) {
        for(StrapTizenSDKMessageListener listener : messageListeners ){
            listener.onError(strapSDKException);
        }
    }
}