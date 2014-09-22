package com.straphq.sdk.tizen.interfaces;

import com.straphq.sdk.tizen.dto.StrapMessageDTO;

/**
 * Defines Strap SDK utils
 */
public interface StrapUtils {

    public final String STRAP_API_URL = "https://api.straphq.com/create/visit/with/";

    //This processes the message and sends it to the Strap HQ
    public void processReceivedData(StrapMessageDTO strapMessageDTO);

    // Check weather given data is related strap or not
    public boolean canHandleMessage(byte[] pData);

}
