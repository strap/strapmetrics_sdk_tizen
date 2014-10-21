var Strap = (function() {

    var Strap = function (app_id, channel_id, SocketOrAppName, strapdata) {
        if(app_id) {
            if (SocketOrAppName instanceof  Object)
                return new StrapWithOutManageConnection(app_id, channel_id, SocketOrAppName, strapdata);
            else
                return new StrapWithManageConnection(app_id, channel_id, SocketOrAppName, strapdata);
        }
    }
    Strap.prototype = {
        constructor: Strap
    }

    return Strap;
})();