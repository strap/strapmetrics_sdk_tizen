package com.straphq.sdk.tizen;

import org.json.JSONException;
import org.json.JSONObject;

public class StrapMetrics {
  // base url for strap 
  private String baseUrl = "https://api.straphq.com/create/visit/with/";

  // Check weather given data is related strap or not 
  public boolean canHandleMessage(byte[] pData){
		String data = new String(pData);
	
		JSONObject strapObject = null;
		try {
			strapObject = new JSONObject(data);

			if(strapObject.has("app_id"))
				return true;
			else
				return false;
		} catch (JSONException e1) {
			return false;
		} 
  }
  
  //Send data to starp 
  public void sendLogToStrap(byte[] pData){
	  String data = new String(pData);
	  String query = null;
	  JSONObject strapObject = null;
		try {
			strapObject = new JSONObject(data);
			String app_Id = strapObject.getString("app_id");
			String resolution = strapObject.getString("resolution");
			String useragent = strapObject.getString("useragent");
			String action_url = strapObject.getString("action_url");
			String act = strapObject.getString("act");
			String visitor_id = strapObject.getString("visitor_id");
			query = "?app_id=" + app_Id + "&visitor_id=" + visitor_id + "&resolution=" + resolution + "&useragent=" + useragent + "&action_url=" + action_url + "&act=" + act;
	
			// post a http request to strap with current activity/action
			new PostLog(baseUrl, query);
		} catch (JSONException e1) {
		} 
  }
  
	

}
