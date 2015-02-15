![alt text](https://s3.amazonaws.com/strap-assets/strap-metrics.png "Strap Metrics Logo")

Strap Metrics is a real-time wearable analytics platform for developers. This repository contains the Strap Metrics SDK for Tizen. Strap Metrics is currently in beta, and you'll need an account on the dashboard to use this SDK. Signup today at http://www.straphq.com/register.


##Strap Metrics Tizen SDK Quick Start Guide


We now support both Tizen JS apps and provider apps for Android. Strap Metrics utilizes Sockets to communicate between the watch and the connected phone, and tries to be smart about how often it sends data in order to preserve battery life.

Getting started with the Strap Metrics SDK is pretty straightforward. These steps shouldn't take more than 15-20 minutes.

---
1. Login to the [ Strap Dashboard ](http://www.straphq.com/login) and create an app. You'll need your App ID handy for the next step.
2. Add the Strap Metrics SDK on both provider and consumer side for your Tizen project. Please refer README file for both side with following link:-

## Consumer
Getting Started with the Strap Metrics Tizen SDK for Tizen Wear Apps
========================
1. Login to the Strap Dashboard and create an app. You'll need your App ID handy for the next step.

2. There are 2 approaches to use Strap js in your Tizen Code:

	a) Approach 1 - In this approach Strap js provide connection between wear(consumer) and phone(provider) app. The developer can send 			      strap or non strap related data using methods defined in Strap js.


	b) Approach 2 - In this approach the developer has to make connection between consumer and provider app itself. The developer can send 			      strap or non strap related data using methods defined in Strap2 js.

	Approach 1:

	2.1. Include the Strap js file in your js directory. Then include it in head section of your main html file as follows:

		<head>
			<script src="js/strap.tizen.sdk.0.2.6-rc1.js"></script>
		</head>

	2.2. Paste the following code in your main.js to use Strap methods for sending data to StrapMetrics.

		var APP_ID = "7BMsoLmE9PH8i4hM9";      	 // *** change the app id! *** //
		var CHANNEL_ID = 104;	//your channel id
		var Provider_App_Name = "OceanSurveyProvider"	//your provider app name
		var strapObj = {
			resolution: "144x168",
			useragent: "TIZEN"
		};

		var strap = new Strap(APP_ID, Provider_App_Name, CHANNEL_ID, strapObj);

	2.3 Establish connection between wear and phone device by using connect method provided by strap

		strap.connect();

	2.4 send strap events to StrapMetrics

		strap.strap_log_event("/strapEvent");

	2.5 User should create a reference of socket for sending non strap related data to phone device.

		var Socket = strap.getSASocket();
		Socket.setDataReceiveListener(onreceive);
		Socket.sendData(CHANNEL_ID, "Hello User");

		//call back of provider response for non strap related call
		var onreceive = function(channelId, data) {
				// Do your work with response data
				console.log(data);
			}

	2.6 Remove connection of wear and phone device by using disconnect method provided by strap

		strap.disconnect();


	Approach 2:

	2.1. Include the Strap js file in your js directory. Then include it in head section of your main html file as follows:

		<head>
			<script src="js/strap.tizen.sdk.0.2.6-rc1.js"></script>
		</head>

	2.2. Paste the following code in main.js after the socket gets created so that the developer can use Strap methods for sending data 	 to StrapMetrics.

		var APP_ID = "7BMsoLmE9PH8i4hM9";      	 // *** change the app id! *** //
		var CHANNEL_ID = 104;	//your channel id
		var strapObj = {
			resolution: "144x168",
			useragent: "TIZEN"
		};
		var strap = new Strap(APP_ID, SASocket, CHANNEL_ID, strapObj);


	2.3 send strap events to StrapMetrics

			strap.strap_log_event("/strapEvent");

## Provider

Getting Started with the Strap Metrics Tizen SDK for Android Companion Apps
========================
1. Follow the instructions in the main quick start guide for implementing Strap Metrics into your Tizen Code.
2. Include the library strap-tizen-SDK-0.2.6-RC1-bundle.jar in your Eclipse project.
3. Your AppManifest.xml should declare the following permissions:

		<uses-permission android:name="android.permission.INTERNET"/>


4. There are 2 approaches to use Strap Metrics in your Tizen Code:

	a) Approach 1 - In this approach Strap Metrics handle most of the provider side services. The developer can override
					methods accordingly.


	b) Approach 2 - In this approach developer has to write most of the provider side services. The developer can
					directly use Strap Metrics methods.

	Approach 1:

	4.1 Add the following imports in the class which contains your Tizen logic.

			import java.io.IOException;

			import android.content.Intent;
			import android.os.Binder;
			import android.os.IBinder;
			import android.util.Log;
			import com.samsung.android.sdk.SsdkUnsupportedException;
			import com.samsung.android.sdk.accessory.SA;
			import com.samsung.android.sdk.accessory.SAPeerAgent;
			import com.samsung.android.sdk.accessory.SASocket;
			import com.straphq.sdk.tizen.StrapMetrics;
			import com.straphq.sdk.tizen.dto.StrapMessageDTO;
			import com.straphq.sdk.tizen.exception.StrapSDKException;
			import com.straphq.sdk.tizen.impl.TizenConnectionImpl;
			import com.straphq.sdk.tizen.interfaces.StrapTizenSDKMessageListener;

	4.2 Use Strap Metrics SDK in your Tizen Code:

	a) Extend StrapMetrics Class in your Main Service Class.

		public class OceanSurveyFullyManagedService extends StrapMetrics {
			TizenConnectionImpl.TizenConnectionListener mConnection;   //Clone of Connection Object used for sending non strap data.
			public OceanSurveyFullyManagedService(){
				super();  //Default constructor for creating default TAG and CHANNELID.
				or
				super(TAG);  //Constructor for setting your own TAG of type String and Default CHANNELID.
				or
				super(CHANNELID);   //Constructor for setting your own CHANNELID of type int and Default TAG.
				or
				super(TAG,CHANNELID);  //Constructor for setting your own TAG of type String and CHANNELID of type int.
			}
			// do your work here
		}

	b) Override onCreate Method of StrapMetrics for Socket Connection and bind addMessageListener.

		@Override
		public void onCreate() {
			// TODO Auto-generated method stub
			super.onCreate();
			Log.i(TAG, "onCreate of smart view Provider Service");
			SA mAccessory = new SA();
			try {
				mAccessory.initialize(this);
			} catch (SsdkUnsupportedException e) {
				// Error Handling
			} catch (Exception e1) {
				e1.printStackTrace();
				stopSelf();
			}

			// add message listener here
			addMessageListener(new StrapTizenSDKMessageListener() {

				@Override
				public void onError(StrapSDKException error) {
					Log.e(TAG, "Connection is not alive ERROR: " + error.getMessage());
				}

				@Override
				public void onConnectionLost(StrapSDKException error) {
					Log.e(TAG, "Error on onServiceConectionLost " + error.getMessage());
					mConnection = null;
				}

				public void onMessage(byte[] data) {
					try {
						mConnection.send(CHANNEL_ID, "Send your non strap response");  
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				@Override
				public void onStrapMessage(StrapMessageDTO strapMessageDTO) {
					processReceivedData(strapMessageDTO);
				}
			});
		}

	c) Initialize Socket Connection in onServiceConnectionResponse method, which is used to send non strap related
	response back to tizen.

		@Override
		protected void onServiceConnectionResponse(SASocket thisConnection, int result) {
				if (result == CONNECTION_SUCCESS) {
					if (thisConnection != null) {
						mConnection = (TizenConnectionImpl.TizenConnectionListener) thisConnection;
					} else {
						Log.e(TAG, "SASocket object is null");
					}
				} else if (result == CONNECTION_ALREADY_EXIST) {
					Log.e(TAG, "onServiceConnectionResponse, CONNECTION_ALREADY_EXIST");
				} else {
					Log.e(TAG, "onServiceConnectionResponse result error =" + result);
				}

		}


	Approach 2:

	4.1 Add the following imports in the class which contains your Tizen logic.

		import com.straphq.sdk.tizen.StrapMetrics;
		import org.json.JSONException;

	4.2 Strap Metrics methods that developer can include in his/her logic for this approach are:

	a) canHandleMessage: returns a boolean value true/false depending on the type of data passed to it as argument i.e if the data is strap related then it will return true otherwise false.

	b) logReceivedData: sends strap data to strap metrics.
	Use strap metrics methods inside onReceive() method of your Tizen Logic java file as follows:

		@Override
		public void onReceive(int channelId, byte[] data) {
			if(StrapMetrics.canHandleMessage(data)){
				StrapMessageDTO strapMessageDTO;
				try {
					strapMessageDTO = new StrapMessageDTO(new String(data));
					StrapMetrics.logReceivedData(strapMessageDTO);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				//Do Something with non strap related data
			}
		}
