/*
 * Copyright (c) 2014 Samsung Electronics Co., Ltd.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *     * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following disclaimer
 *       in the documentation and/or other materials provided with the
 *       distribution.
 *     * Neither the name of Samsung Electronics Co., Ltd. nor the names of its
 *       contributors may be used to endorse or promote products derived from
 *       this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

;
var CHANNELID = 104;
var strap_app_id = "9Zrc7PqpFq5jpL3HP";
var ProviderAppName = "OceanSurveyProvider";
var strap = null;
var sealionCount = 0;
var dolphinCount = 0;
var pelicanCount = 0;

window.onload = function() {
    // add eventListener for tizenhwkey
    document.addEventListener('tizenhwkey', function(e) {
        if (e.keyName == "back")
            tizen.application.getCurrentApplication().exit();
    });

    // initialize strap object on app load
    var strapdata = {
        resolution : "144x168",
        useragent : "TIZEN"
    };
    strap = new Strap(strap_app_id, CHANNELID, ProviderAppName, strapdata);
}

//Establish connection between wear and phone device
function connect() {
    strap.connect();
}

//Disconnect connection between wear and phone device
function disconnect() {
    strap.disconnect();
}

// send event to strap on sealion button click and increment in count bye 1
function sealion_click() {
    if (strap.getSASocket()) {
        var log = document.getElementById('sealion');
        log.innerHTML = ++sealionCount;
        strap.strap_log_event("/sealion");
    }
}

//send event to strap on dolphin button click and increment in count bye 1
function dolphin_click() {
    if (strap.getSASocket()) {
        var log = document.getElementById('dolphin');
        log.innerHTML = ++dolphinCount;
        strap.strap_log_event("/dolphin");
    }
}

//send event to strap on dolphin pelican click and increment in count bye 1
function pelican_click() {
    if (strap.getSASocket()) {
        var log = document.getElementById('pelican');
        log.innerHTML = ++pelicanCount;
        strap.strap_log_event("/pelican");
    }
}

// show non strap related communication with provider
function time_click() {
    var sasocket = strap.getSASocket();
    sasocket.setDataReceiveListener(onreceive);
    sasocket.sendData(CHANNELID, "Hello User");

}

// call back of provider response for non strap related call 
var onreceive = function(channelId, data) {
    // Do your work with response data
    console.log(data);
    var timeDiv = document.getElementById('currentTime');
    timeDiv.innerHTML = data;
}
