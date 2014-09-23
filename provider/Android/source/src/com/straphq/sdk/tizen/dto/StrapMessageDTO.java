package com.straphq.sdk.tizen.dto;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * This Data transfer object specified the message received
 */
public class StrapMessageDTO {

    public StrapMessageDTO(String jsonData) throws JSONException {
        JSONObject parsedData = null;
        parsedData = new JSONObject(jsonData);
        appId = parsedData.getString("app_id");
        resolution = parsedData.getString("resolution");
        userAgent = parsedData.getString("useragent");
        action_url = parsedData.getString("action_url");
        act = parsedData.getString("act");
        visitor_timeoffset = parsedData.getString("visitor_timeoffset");
        visitor_id = parsedData.getString("visitor_id");
        accl = parsedData.getString("accl");
    }

    public String appId;
    public String resolution;
    public String userAgent;
    public String action_url;
    public String act;
    public String visitor_timeoffset;
    public String visitor_id;
    public String accl;

    public String toQueryString() throws UnsupportedEncodingException {
        return (
                "?app_id=" + URLEncoder.encode(appId, "UTF-8") +
                        "&resolution=" + URLEncoder.encode(resolution, "UTF-8") +
                        "&useragent=" + URLEncoder.encode(userAgent, "UTF-8") +
                        "&action_url=" + URLEncoder.encode(action_url, "UTF-8") +
                        "&act=" + URLEncoder.encode(act, "UTF-8") +
                        "&visitor_timeoffset=" + URLEncoder.encode(visitor_timeoffset, "UTF-8") +
                        "&visitor_id=" + URLEncoder.encode(visitor_id, "UTF-8") +
                        "&accl=" + URLEncoder.encode(accl, "UTF-8")
        );
    }

}
