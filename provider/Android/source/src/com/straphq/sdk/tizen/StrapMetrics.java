package com.straphq.sdk.tizen;


import com.straphq.sdk.tizen.dto.StrapMessageDTO;
import com.straphq.sdk.tizen.exception.StrapSDKException;
import com.straphq.sdk.tizen.impl.StrapSDKUtils;
import com.straphq.sdk.tizen.impl.TizenConnectionImpl;
import com.straphq.sdk.tizen.interfaces.StrapSDK;
import com.straphq.sdk.tizen.interfaces.StrapTizenSDKMessageListener;

import java.util.ArrayList;

public abstract class StrapMetrics extends TizenConnectionImpl implements StrapSDK {

    private static final Integer DEFAULT_CHANNEL_ID = 835462;

    private static final String DEFAULT_TAG = "StrapMetricsTizenSDKConnectionTag";

    public StrapMetrics(String tag, Integer channelId) {
        super(tag);
        setChannelId(channelId);
    }

    public StrapMetrics(Integer channelId) {
        super(DEFAULT_TAG);
        setChannelId(channelId);
    }

    public StrapMetrics(String tag) {
        super(tag);
        setChannelId(DEFAULT_CHANNEL_ID);
    }

    public StrapMetrics() {
        super(DEFAULT_TAG);
        setChannelId(DEFAULT_CHANNEL_ID);
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
    public void eventOnMessage(byte[] data) {
        for (StrapTizenSDKMessageListener listener : messageListeners) {
            listener.onMessage(data);
        }
    }

    @Override
        public void eventOnStrapMessage(StrapMessageDTO strapMessageDTO) {
            for (StrapTizenSDKMessageListener listener : messageListeners) {
                listener.onStrapMessage(strapMessageDTO);
        }
    }

    @Override
    public void eventOnError(StrapSDKException strapSDKException) {
        for (StrapTizenSDKMessageListener listener : messageListeners) {
            listener.onError(strapSDKException);
        }
    }

    @Override
    public void eventOnConnectionLost(StrapSDKException strapSDKException) {
        for (StrapTizenSDKMessageListener listener : messageListeners) {
            listener.onConnectionLost(strapSDKException);
        }
    }

    //check if the data is related to strap or not
    public static boolean canHandleMessage(byte[] messageByteArray) {
        return StrapSDKUtils.getBean().canHandleMessage(messageByteArray);
    }

    //send strap related data to strap metrics
    public static void logReceivedData(StrapMessageDTO strapMessageDTO) {
        StrapSDKUtils.getBean().processReceivedData(strapMessageDTO);
    }
}