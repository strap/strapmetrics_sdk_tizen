var Strap = (function() {

    var Strap = function (app_id, SocketOrAppName, channel_id, strapdata) {
        if(app_id) {
            if (SocketOrAppName instanceof  Object)
                return new StrapWithOutManageConnection(app_id, SocketOrAppName, channel_id, strapdata);
            else
                return new StrapWithManageConnection(app_id, SocketOrAppName, channel_id, strapdata);
        }
    }
    Strap.prototype = {
        constructor: Strap
    }

    return Strap;
})();