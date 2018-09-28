package com.base.api;

/**
 * Created by shayan on 6/30/2017.
 */

public interface BaseApiInterface {
    /**
     * response for request to api
     *
     * @param response is String response from server
     */
    void onResponse(String response);

    /**
     * error for requesto to api
     *
     * @param message is Error message to display
     */
    void onError(String message);

    void onLogout();
}
