var Strap = (function() {

    var Strap = function (app_id, channel_id, SocketOrAppName, strapdata) {
        if(app_id) {
            if (SocketOrAppName instanceof  Object)
                return new StrapWithOutManageConnection();
            else
                return new StrapWithManageConnection();
        }
    }
    Strap.prototype = {
        constructor: Strap
    }

    return Strap;
})();