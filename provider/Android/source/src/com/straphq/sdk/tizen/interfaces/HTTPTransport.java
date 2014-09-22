package com.straphq.sdk.tizen.interfaces;

import com.straphq.sdk.tizen.dto.StrapMessageDTO;

/**
 * This interface defines the methods required for creating an HTTP transport to Strap API
 */
public interface HTTPTransport {

    //Sends message to Strap API.
    public boolean sendMessage(StrapMessageDTO strapMessageDTO);

}
