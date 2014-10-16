var Strap = (function () {
    var SASocket;
    var CHANNELID;
    var strap_obj;

    // Accelerometer code
    var acclBufferData = [];
    var sampleData = [];
    const NUM_SAMPLES = 200; // Bath of Samples to be send at a time

    // Constructor for strap
    var Strap = function (app_id, channel_id, SASocketObj, strapdata) {
        SASocket = SASocketObj;
        CHANNELID = channel_id || 835462;
        strap_obj = {
            app_id: app_id ,
            resolution: "144x168",
            useragent: "TIZEN",
            act: "UNKNOWN",
            action_url: "/",
            accl: [],
            visitor_id: ""
        }
        for (var attr in strapdata) {
            if (strap_obj.hasOwnProperty(attr))
                strap_obj[attr] = strapdata[attr];
        }
        
        strap_start();
        
        if (!acclBufferData) {
            acclBufferData = window.localStorage["strap_accl"];
        }

        // Update local storage for strap_accl
        setInterval(function () {
            window.localStorage["strap_accl"] = acclBufferData;
        }, 1000 * 60);
    }

    var strap_api_clone = function (obj) {
        if (null == obj || "object" != typeof obj)return obj;
        var copy = {};
        for (var attr in obj) {
            if (obj.hasOwnProperty(attr))copy[attr] = obj[attr]
        }
        return copy;
    };

    // Receive listener's handler for strap related data
    var onStrapReceive = function (channelId, data) {
        // TODO
    }
    
    // sends data from tizen side to android side
    var strap_send_data = function (data) {
        SASocket.setDataReceiveListener(onStrapReceive);
        SASocket.sendData(CHANNELID, JSON.stringify(data));
    }

    // Method for logging error
    var onerror = function (err) {
        console.log("ONERROR: err [" + err.name + "] msg[" + err.message + "]");
    }
   
    
    // Receive listener's handler for accelerometer data
    var onAcclReceive = function (channelId, data) {
        // do something
    }
    
    // send event log to strap
    var strap_log_accl = function (data) {
        var strap_obj_copy = strap_api_clone(strap_obj);
        strap_obj_copy['action_url'] = "STRAP_API_ACCL";
        strap_obj_copy['accl'] = data;
        SASocket.setDataReceiveListener(onAcclReceive);
        SASocket.sendData(CHANNELID, JSON.stringify(strap_obj_copy));
    }
    
    // adding devicemotion event listener to collect accelerometer data
    window.addEventListener("devicemotion", handleMotionEvent, true);
    
    // handler for devicemotion event listener for sending accelerometer data
    var handleMotionEvent = function (event) {
        var x_c = event.accelerationIncludingGravity.x;
        var y_c = -event.accelerationIncludingGravity.y;
        var z_c = -event.accelerationIncludingGravity.z;
        var ts = event.timeStamp;

        // change unit from meter/square second to milliGâ€™s
        x_c = x_c * 101.97;
        y_c = y_c * 101.97;
        z_c = z_c * 101.97;

        var data = {
            x: x_c,
            y: y_c,
            z: z_c,
            ts: ts
        }
        acclBufferData.push(data);
        if (acclBufferData.length >= NUM_SAMPLES) {
            sampleData = acclBufferData.splice(0, NUM_SAMPLES);
            // send data to strap
            strap_log_accl(sampleData.splice(0, NUM_SAMPLES));
        }
    }

    Strap.prototype = {
        constructor: Strap,
       
        // send activity log to strap
        strap_log_activity: function (data) {
            var strap_obj_copy = strap_api_clone(strap_obj);
            strap_obj_copy['act'] = data;
            strap_send_data(strap_obj_copy);
        },

        // send event log to strap
        strap_log_event: function (data) {
            var strap_obj_copy = strap_api_clone(strap_obj);
            strap_obj_copy['action_url'] = data;
            strap_send_data(strap_obj_copy);
        },
        
        // deinitialize or release strap object
        strap_finish : function () {
         	if(SASocket){
         		Strap.prototype.strap_log_event.call(this,"/STRAP_FINISH");
         		delete this;
         	}
         }
    }
    
 // Send app start log to strap
    var strap_start = function () {
     	if(SASocket){
     		Strap.prototype.strap_log_event.call(this,"/STRAP_START");
     	}
     }
    
    return Strap;
})();