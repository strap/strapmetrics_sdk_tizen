package com.srph.simplesapprovider.service;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;

import sm.StrapMetrics;

import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.accessory.SA;
import com.samsung.android.sdk.accessory.SAAgent;
import com.samsung.android.sdk.accessory.SAPeerAgent;
import com.samsung.android.sdk.accessory.SASocket;

public class SAPServiceProvider extends SAAgent{

	public static final String TAG = "SAPServiceProvider";
	
	public final static int SERVICE_CONNECTION_RESULT_OK = 0;
	
	public final static int SAP_SERVICE_CHANNEL_ID = 123;
	
	HashMap<Integer, SAPServiceProviderConnection> connectionMap = null;
	
	StrapMetrics sm;
	
	private final IBinder mIBinder = new LocalBinder();
	
		public class LocalBinder extends Binder {
			public SAPServiceProvider getService() {
				Log.d("SAP PROVIDER", "LOCAL BINDER GET SERVICE");
				return SAPServiceProvider.this;
			}
		}

		public SAPServiceProvider() {
			super(TAG, SAPServiceProviderConnection.class);
			Log.d("SAP PROVIDER", "SERVICE CONSTRUCTOR");
		}

		public class SAPServiceProviderConnection extends SASocket {
			private int mConnectionId;

			public SAPServiceProviderConnection() {
				super(SAPServiceProviderConnection.class.getName());
				Log.d("SAP PROVIDER", "SAP CONNECTION CONSTRUCTOR");
			}

			@Override
			public void onError(int channelId, String errorString, int error) {
				Log.e(TAG, "Connection is not alive ERROR: " + errorString + "  "
						+ error);
				
				Log.d("SAP PROVIDER", "SAP CONNECTION ERROR " + errorString + " || " + error);
			}

			@Override
			public void onReceive(int channelId, byte[] data) {
				
				if(sm.canHandleMessage(data)){
					sm.sendLogToStrap(data);
					final SAPServiceProviderConnection uHandler = connectionMap.get(Integer
							.parseInt(String.valueOf(mConnectionId)));
					if(uHandler == null){
						Log.e(TAG,"Error, can not get HelloAccessoryProviderConnection handler");
						return;
					}
					new Thread(new Runnable() {
						public void run() {
							try {
								uHandler.send(SAP_SERVICE_CHANNEL_ID, "Ok from strap".getBytes());
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}).start();		
					}
				
				
				Log.d(TAG, "onReceive");

				final String message;
				
				Time time = new Time();

				time.set(System.currentTimeMillis());

				String timeStr = " " + String.valueOf(time.minute) + ":"
						+ String.valueOf(time.second);

				String strToUpdateUI = new String(data);
				
				message = getDeviceInfo() + strToUpdateUI.concat(timeStr);
				
				Log.d("SAP MESSAGE", message);
				
				final SAPServiceProviderConnection uHandler = connectionMap.get(Integer
						.parseInt(String.valueOf(mConnectionId)));
				if(uHandler == null){
					Log.e(TAG,"Error, can not get HelloAccessoryProviderConnection handler");
					return;
				}
				new Thread(new Runnable() {
					public void run() {
						try {
							uHandler.send(SAP_SERVICE_CHANNEL_ID, message.getBytes());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}

			@Override
			protected void onServiceConnectionLost(int errorCode) {
				Log.e(TAG, "onServiceConectionLost  for peer = " + mConnectionId
						+ "error code =" + errorCode);

				if (connectionMap != null) {
					connectionMap.remove(mConnectionId);
				}
			}
		}
		
	    @Override
	    public void onCreate() {
	        super.onCreate();
	        Log.i(TAG, "onCreate of smart view Provider Service");
	        
	        SA mAccessory = new SA();
	        try {
	        	mAccessory.initialize(this);
	        	Log.d("SAP PROVIDER", "ON CREATE TRY BLOCK");
	        	
	        	
	        	//create instance of strap metrics 
	        	 sm = new StrapMetrics();
	        } catch (SsdkUnsupportedException e) {
	        	// Error Handling
	        	Log.d("SAP PROVIDER", "ON CREATE TRY BLOCK ERROR UNSUPPORTED SDK");
	        } catch (Exception e1) {
	            Log.e(TAG, "Cannot initialize Accessory package.");
	            e1.printStackTrace();
				/*
				 * Your application can not use Accessory package of Samsung
				 * Mobile SDK. You application should work smoothly without using
				 * this SDK, or you may want to notify user and close your app
				 * gracefully (release resources, stop Service threads, close UI
				 * thread, etc.)
				 */
	            stopSelf();
	        }

	    }	

	   
	    @Override 
	    protected void onServiceConnectionRequested(SAPeerAgent peerAgent) { 
	        acceptServiceConnectionRequest(peerAgent);
	        Log.d("SAP PROVIDER", "CONNECTION REQUESTED : " + peerAgent.getAppName());
	    } 
	    
	  
		@Override
		protected void onFindPeerAgentResponse(SAPeerAgent arg0, int arg1) {
			Log.d(TAG, "onFindPeerAgentResponse  arg1 =" + arg1);
		}

		
		@Override
		protected void onServiceConnectionResponse(SASocket thisConnection,
				int result) {
			if (result == CONNECTION_SUCCESS) {
				if (thisConnection != null) {
					SAPServiceProviderConnection myConnection = (SAPServiceProviderConnection) thisConnection;

					if (connectionMap == null) {
						connectionMap = new HashMap<Integer, SAPServiceProviderConnection>();
					}

					myConnection.mConnectionId = (int) (System.currentTimeMillis() & 255);

					Log.d(TAG, "onServiceConnection connectionID = "
							+ myConnection.mConnectionId);

					connectionMap.put(myConnection.mConnectionId, myConnection);

					Toast.makeText(getBaseContext(),
							"CONNECTION ESTABLISHED", Toast.LENGTH_LONG)
							.show();
				} else {
					Log.e(TAG, "SASocket object is null");
				}
			} else if (result == CONNECTION_ALREADY_EXIST) {
				Log.e(TAG, "onServiceConnectionResponse, CONNECTION_ALREADY_EXIST");
			} else {
				Log.e(TAG, "onServiceConnectionResponse result error =" + result);
			}
		}

		@Override
		public IBinder onBind(Intent arg0) {
			Log.d("SAP PROVIDER", "onBIND");
			return mIBinder;
		}
		
		public String getDeviceInfo()
		{
			String manufacturer = Build.MANUFACTURER;
			
			String model = Build.MODEL;
			
			return manufacturer + " " + model ;
		}

}
