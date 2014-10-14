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

        public TizenConnectionListener(String s) {
            super(s);
        }

         public TizenConnectionListener() {
             super(TizenConnectionListener.class.getName());
         }

        @Override
        protected void onServiceConnectionLost(int errorCode) {
            eventOnConnectionLost(new StrapSDKException("Connection Lost. Status: " + errorCode));
        }

        @Override
        public void onReceive(int channelId, byte[] bytes) {
            if (strapSDKUtils.canHandleMessage(bytes)) {
                //process strap related data
                try {
                    eventOnStrapMessage(new StrapMessageDTO(new String(bytes)));
                } catch (JSONException exception) {
                    exception.printStackTrace();
                    eventOnError(new StrapSDKException(exception.getMessage()));
                } catch (Exception exception) {
                    exception.printStackTrace();
                    eventOnError(new StrapSDKException(exception.getMessage()));
                }
            }
            else {
               //process non strap data
               try {
                    eventOnMessage(bytes);
               } catch (Exception exception) {
                    exception.printStackTrace();
                    eventOnError(new StrapSDKException(exception.getMessage()));
               }
            }
        }

        @Override
        public void onError(int channelId, String errorString, int error) {
            eventOnError(new StrapSDKException("Error Occurred: " + channelId + " " + errorString + " " + error));
        }
    }

    public void onCreate(){
    super.onCreate();
    };
}
