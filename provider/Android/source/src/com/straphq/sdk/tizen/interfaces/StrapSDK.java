package com.straphq.sdk.tizen.interfaces;

import com.straphq.sdk.tizen.dto.StrapMessageDTO;

/**
 * This Interface defines the basic methods for the Strap Android SDK
 * */
public interface StrapSDK {


    public void addMessageListener(StrapTizenSDKMessageListener strapTizenSDKMessageListener);

    //Send data to strap
    public void processReceivedData(StrapMessageDTO strapMessageDTO);

}
