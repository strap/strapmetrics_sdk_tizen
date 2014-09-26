package com.straphq.sdk.tizen.interfaces;

import com.straphq.sdk.tizen.dto.StrapMessageDTO;
import com.straphq.sdk.tizen.exception.StrapSDKException;

/**
 * Defines the reqired events for Strap messages
 */
public interface StrapMessageEvents {

    void eventOnMessage(byte[] data);

    void eventOnStrapMessage(StrapMessageDTO strapMessageDTO);

    void eventOnError(StrapSDKException strapSDKException);

    void eventOnConnectionLost(StrapSDKException strapSDKException);

}
