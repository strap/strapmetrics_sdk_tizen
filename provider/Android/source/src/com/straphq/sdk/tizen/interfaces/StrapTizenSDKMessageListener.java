package com.straphq.sdk.tizen.interfaces;

import com.straphq.sdk.tizen.dto.StrapMessageDTO;
import com.straphq.sdk.tizen.exception.StrapSDKException;

/**
 * this interface will be implemented by the user client for handling the data.
 */
public interface StrapTizenSDKMessageListener {

    public void onMessage(StrapMessageDTO strapMessageDTO);

    public void onError(StrapSDKException strapSDKException);

    public void onConnectionLost(StrapSDKException strapSDKException);

}
