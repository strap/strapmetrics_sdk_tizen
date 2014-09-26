package com.straphq.sdk.tizen.interfaces;

import com.straphq.sdk.tizen.dto.StrapMessageDTO;
import com.straphq.sdk.tizen.exception.StrapSDKException;

/**
 * Defines the reqired events for Strap messages
 */
public interface StrapMessageEvents {

    //fire event for non strap related data
    void eventOnMessage(byte[] data);

    //fire event for strap related data
    void eventOnStrapMessage(StrapMessageDTO strapMessageDTO);

    //fire event on error
    void eventOnError(StrapSDKException strapSDKException);

    //fire event on connection lost
    void eventOnConnectionLost(StrapSDKException strapSDKException);

}
