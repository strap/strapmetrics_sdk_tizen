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
