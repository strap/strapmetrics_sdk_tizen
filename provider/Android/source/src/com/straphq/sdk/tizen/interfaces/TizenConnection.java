package com.straphq.sdk.tizen.interfaces;

import android.content.Context;

/**
 * This interface defines the tizen connection creation
 */
public interface TizenConnection extends StrapMessageEvents {

    //sets channelId for peer communication
    void setChannelId(Integer channelId);

}
