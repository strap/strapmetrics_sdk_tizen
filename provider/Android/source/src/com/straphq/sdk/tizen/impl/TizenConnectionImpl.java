package com.straphq.sdk.tizen.impl;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.straphq.sdk.tizen.interfaces.TizenConnection;
import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.accessory.SA;
import com.samsung.android.sdk.accessory.SAAgent;
import com.samsung.android.sdk.accessory.SAPeerAgent;
import com.samsung.android.sdk.accessory.SASocket;

/**
 * Abstract class implementing few requirements of Tizen connection
 */
public abstract class TizenConnectionImpl extends SAAgent implements TizenConnection {

    public static final String TAG = "StrapTizenSDKConnectionService";

    private Integer channelId;

    private android.content.Context androidAppContext;

    protected StrapSDKUtils strapSDKUtils = new StrapSDKUtils();

    protected TizenConnectionImpl() {
        super(TAG, TizenConnectionListener.class);
    }

    public void setChannelId(Integer channelId){
        this.channelId = channelId;
    }

    public void setContext(Context context){
        this.androidAppContext = context;
    }

    @Override
    protected void onServiceConnectionResponse(SASocket saSocket, int i) {

    }

    @Override
    protected void onFindPeerAgentResponse(SAPeerAgent saPeerAgent, int i) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class TizenConnectionListener extends SASocket {

        protected TizenConnectionListener(String s) {
            super(s);
        }

        @Override
        protected void onServiceConnectionLost(int i) {

        }

        @Override
        public void onReceive(int i, byte[] bytes) {

        }

        @Override
        public void onError(int i, String s, int i2) {

        }
    }

    //Todo
}
