package com.straphq.sdk.tizen.impl;

import com.straphq.sdk.tizen.dto.StrapMessageDTO;
import com.straphq.sdk.tizen.interfaces.HTTPTransport;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Implements the strap HTTP Transport
 */
public class StrapHTTPTransport implements HTTPTransport {

    private String strapAPIURL;

    public StrapHTTPTransport(String strapAPIURL) {
        this.strapAPIURL = strapAPIURL;
    }

    @Override
    public boolean sendMessage(StrapMessageDTO strapMessageDTO) {
        try {
            sendViaHTTP(strapAPIURL, strapMessageDTO.toQueryString());
            return true;
        } catch (UnsupportedEncodingException exception) {
            System.out.println(exception.getMessage());
            return false;
        }
    }

    private void sendViaHTTP(String url, String query) {
        HttpURLConnection con = null;
        InputStream is = null;
        try {
            con = (HttpURLConnection) (new URL(url + query)).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null)
                buffer.append(line + "\r\n");
            //Todo do something with the string
            is.close();
            con.disconnect();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Throwable t) {
                t.printStackTrace();
            }
            try {
                con.disconnect();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
