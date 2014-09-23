package com.straphq.sdk.tizen.impl;

import com.straphq.sdk.tizen.dto.StrapMessageDTO;
import com.straphq.sdk.tizen.exception.StrapSDKException;
import com.straphq.sdk.tizen.interfaces.TizenConnection;
import com.samsung.android.sdk.accessory.SAAgent;
import com.samsung.android.sdk.accessory.SASocket;

import org.json.JSONException;

/**
 * Abstract class implementing few requirements of Tizen connection
 */
public abstract class TizenConnectionImpl extends SAAgent implements TizenConnection {

    public static String TAG;

    private Integer channelId;

    private android.content.Context androidAppContext;

    protected StrapSDKUtils strapSDKUtils = StrapSDKUtils.getBean();

    protected TizenConnectionImpl(String tag) {
        super(tag, TizenConnectionListener.class);
        TAG = tag;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public class TizenConnectionListener extends SASocket {

        protected TizenConnectionListener(String s) {
            super(s);
        }

        @Override
        protected void onServiceConnectionLost(int i) {
            eventOnConnectionLost(new StrapSDKException("Connection Lost. Status: " + i));
        }

        @Override
        public void onReceive(int i, byte[] bytes) {
            if (strapSDKUtils.canHandleMessage(bytes)) {
                try {
                    eventOnMessage(new StrapMessageDTO(new String(bytes)));
                } catch (JSONException exception) {
                    exception.printStackTrace();
                    eventOnError(new StrapSDKException(exception.getMessage()));
                } catch (Exception exception) {
                    exception.printStackTrace();
                    eventOnError(new StrapSDKException(exception.getMessage()));
                }
            }
        }

        @Override
        public void onError(int i, String s, int i2) {
            eventOnError(new StrapSDKException("Error Occurred: " + i + " " + s + " " + i2));
        }
    }

    public abstract void onCreate();
}
