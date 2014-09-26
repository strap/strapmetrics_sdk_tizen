package com.straphq.sdk.tizen.interfaces;

import com.straphq.sdk.tizen.dto.StrapMessageDTO;
import com.straphq.sdk.tizen.exception.StrapSDKException;

/**
 * this interface will be implemented by the user client for handling the data.
 */
public interface StrapTizenSDKMessageListener {

    //handler for non strap related data
    public void onMessage(byte[] data);

    //handler for strap related data
    public void onStrapMessage(StrapMessageDTO strapMessageDTO);

    //handler for on error
    public void onError(StrapSDKException strapSDKException);

    //handler for on connection lost
    public void onConnectionLost(StrapSDKException strapSDKException);

}
