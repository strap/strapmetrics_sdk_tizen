var Strap = (function() {
    var SAAgent;
    var SASocket;
    var CHANNELID = 835462;
    var ProviderAppName;
    var strap_obj;

    // Accelerometer code
    var acclBufferData = [];
    var sampleData = [];
    const NUM_SAMPLES = 200; // Bath of Samples to be send at a time

    var Strap = function(channel_id, Provider_App_Name, strapdata) {
        SAAgent = null;
        SASocket = null;
        if (channel_id)
            CHANNELID = channel_id;
        ProviderAppName = Provider_App_Name;
        strap_obj = {
            app_id : "",
            resolution : "144x168",
            useragent : "TIZEN",
            act : "UNKNOWN",
            action_url : "/",
            accl : [],
            visitor_id : ""
        }
        for ( var attr in strapdata) {
            if (strap_obj.hasOwnProperty(attr))
                strap_obj[attr] = strapdata[attr];
        }
        if (!acclBufferData) {
            acclBufferData = window.localStorage["strap_accl"];
        }

        // Update local storage for strap_accl
        setInterval(function(){
            window.localStorage["strap_accl"] = acclBufferData;
        }, 1000 * 60 * 2);
    }
    // Receive listener's handler for strap related data
    var onStrapReceive = function(channelId, data) {
        // TODO
    }

    // sends data from tizen side to android side
    var strap_send_data = function(data) {
        SASocket.setDataReceiveListener(onStrapReceive);
        SASocket.sendData(CHANNELID, JSON.stringify(data));
    }

    // set strap activity
    var strap_set_activity = function(activity) {
        strap_obj.act = activity;
    }
    // set strap event
    var strap_set_event = function(event) {
        strap_obj.action_url = event;
    }
    // set strap event
    var strap_set_accl = function(accl) {
        strap_obj.accl = accl;
    }

    // Method for logging error
    var onerror = function(err) {
        console.log("ONERROR: err [" + err.name + "] msg[" + err.message + "]");
    }
    // onsuccess of SAAgent request
    var onsuccess = function(agents) {
        try {
            if (agents.length > 0) {
                SAAgent = agents[0];
                SAAgent.setPeerAgentFindListener(peerAgentFindCallback);
                SAAgent.findPeerAgents();
                console.log(" onsuccess " + SAAgent.name);
                console.log(JSON.stringify(SAAgent));
            } else {
                alert("Not found SAAgent!!");
                console.log(" onsuccess else");
            }
        } catch (err) {
            console.log("onsuccess exception [" + err.name + "] msg["
                + err.message + "]");
        }
    }
    // handler for peerAgentFindListener
    var peerAgentFindCallback = {
        onpeeragentfound : function(peerAgent) {
            try {
                if (peerAgent.appName == ProviderAppName) {
                    console.log(" peerAgentFindCallback::onpeeragentfound "
                        + peerAgent.appname + " || " + ProviderAppName);
                    SAAgent.setServiceConnectionListener(agentCallback);
                    SAAgent.requestServiceConnection(peerAgent);
                } else {
                    alert("Not expected app!! : " + peerAgent.appName);
                }
            } catch (err) {
                console
                    .log(" peerAgentFindCallback::onpeeragentfound exception ["
                        + err.name + "] msg[" + err.message + "]");
            }
        },
        onerror : onerror
    }
    // handler for serviceConnectionListener
    var agentCallback = {
        onconnect : function(socket) {
            console.log("agentCallback onconnect" + socket);
            console.log(socket);
            console.log(JSON.stringify(socket));
            SASocket = socket;
            alert("SAP Connection established with RemotePeer");
            createHTML("startConnection");

            SASocket.setSocketStatusListener(function(reason) {
                console.log("Service connection lost, Reason : [" + reason
                    + "]");
                disconnect();
            });
        },
        onerror : onerror
    };
    // Receive listener's handler for accelerometer data
    var onAcclReceive = function(channelId, data) {
        // do something
    }
    // send event log to strap
    var strap_log_accl = function(data) {
        strap_set_accl(data);
        strap_set_event("STRAP_API_ACCL");
        strap_set_activity("UNKNOWN");
        SASocket.setDataReceiveListener(onAcclReceive);
        SASocket.sendData(CHANNELID, JSON.stringify(strap_obj));
    }
    // adding devicemotion event listener to collect accelerometer data
    window.addEventListener("devicemotion", handleMotionEvent, true);
    // handler for devicemotion event listener for sending accelerometer data
    var handleMotionEvent = function(event) {
        var x_c = event.accelerationIncludingGravity.x;
        var y_c = -event.accelerationIncludingGravity.y;
        var z_c = -event.accelerationIncludingGravity.z;
        var ts = event.timeStamp;

        // change unit from m/s to milliG’s
        x_c = x_c * 101.97;
        y_c = y_c * 101.97;
        z_c = z_c * 101.97;

        var data = {
            x : x_c,
            y : y_c,
            z : z_c,
            ts : ts
        }
        acclBufferData.push(data);
        if (acclBufferData.length >= NUM_SAMPLES) {
            sampleData = acclBufferData.splice(0, NUM_SAMPLES);
            // send data to strap
            strap_log_accl(sampleData.splice(0, NUM_SAMPLES));
        }
    }

    Strap.prototype = {
        constructor : Strap,
        // Receive listener's handler for non strap related data
        onreceive : function(channelId, data) {
            // TODO
        },
        // send activity log to strap
        strap_log_activity : function(data) {
            strap_set_activity(data);
            strap_set_event("/");
            strap_send_data(strap_obj);
        },

        // send event log to strap
        strap_log_event : function(data) {
            strap_set_event(data);
            strap_set_activity("UNKNOWN");
            strap_send_data(strap_obj);
        },
        // deinitialize or release strap object
        strap_deinit : function() {
            delete this;
        },
        /**
         * Communication related methods with phone
         */
        // Send a connect request from tizen to phone
        connect : function() {
            if (SASocket) {
                alert('Already connected!');
                return false;
            }
            try {
                webapis.sa.requestSAAgent(onsuccess, onerror);
            } catch (err) {
                console.log("exception [" + err.name + "] msg[" + err.message
                    + "]");
            }
        },
        // Disconnect communication link
        disconnect : function() {
            try {
                if (SASocket != null) {
                    console.log(" DISCONNECT SASOCKET NOT NULL");
                    SASocket.close();
                    SASocket = null;
                    createHTML("closeConnection");
                }
            } catch (err) {
                console.log(" DISCONNECT ERROR: exception [" + err.name
                    + "] msg[" + err.message + "]");
            }
        }
    }
    return Strap;
})();