//create and initialize strap object 
function Strap(socket, channel_id, strapdata) {
    var SASocket = socket;
    var CHANNELID = channel_id;
    var strap_obj = {
        app_id: "",
        resolution: "144x168",
        useragent: "Tizen",
        act: "UNKNOWN",
        action_url: "/"
    }
    for (var key in strapdata) {
        strap_obj.key = strapdata[key];
    }
    //sends data from tizen side to android side
    var strap_send_data = function(data) {
            SASocket.sendData(CHANNELID, JSON.stringify(data));
        }
        //set strap activity
    var strap_set_activity = function(activity) {
            strap_obj.act = activity;
        }
        //set strap event
    var strap_set_event = function(event) {
            strap_obj.action_url = event;
        }
        //send activity log to strap 
    this.strap_log_activity = function(path) {
        strap_set_activity(path);
        strap_set_event("/");
        console.log(JSON.stringify(strap_obj));
        //strap_send_data(strap_obj);
    }
    //send event log to strap
    this.strap_log_event = function(path) {
        strap_set_event(path);
        strap_set_activity("UNKNOWN");
        strap_send_data(strap_obj);
    }
    //deinitialize or release strap object
    this.strap_deinit = function() {
        delete this;
    }
}
console.log("Framework FILE");
console.log("CORE FILE");