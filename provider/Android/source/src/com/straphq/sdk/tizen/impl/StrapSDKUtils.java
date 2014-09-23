package com.straphq.sdk.tizen.impl;

import com.straphq.sdk.tizen.dto.StrapMessageDTO;
import com.straphq.sdk.tizen.interfaces.StrapUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Implements the Strap SDK Utils Interface
 */
public class StrapSDKUtils implements StrapUtils {

    StrapHTTPTransport strapHTTPTransport = new StrapHTTPTransport(STRAP_API_URL);

    @Override
    public void processReceivedData(StrapMessageDTO strapMessageDTO) {
        strapHTTPTransport.sendMessage(strapMessageDTO);
    }

    @Override
    public boolean canHandleMessage(byte[] pData) {
        String data = new String(pData);
        JSONObject strapObject;
        try {
            strapObject = new JSONObject(data);
            return strapObject.has("app_id");
        } catch (JSONException e1) {
            return false;
        }
    }

    static StrapSDKUtils bean = null;

    public static StrapSDKUtils getBean() {
        if (bean == null) bean = new StrapSDKUtils();
        return bean;
    }
}
