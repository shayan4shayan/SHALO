package com.base.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.base.R;
import com.extra.util.AccountManager;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by shayan on 6/30/2017.
 * base class for send request to server
 */

public abstract class BaseApiConnection implements Response.ErrorListener, Response.Listener<String> {
    private static final String AUTHORISATION = "Authorization";
    private static String TOKEN = "token";

    /**
     * base url of the api
     */
    protected String baseUrl = "http://sabt.syfract.com";

    /**
     * TAG for log
     */
    private final String TAG = getClass().getSimpleName();
    /**
     * context for create RequestQueue
     */
    private final Context context;
    /**
     * interface to send error or response to activity
     */
    protected BaseApiInterface mApiInterface = new BaseApiInterface() {
        @Override
        public void onResponse(String response) {
            Log.d(TAG, "onResponse: no Listener set");
        }

        @Override
        public void onError(String message) {
            Log.d(TAG, "onError: no listener set");
        }

        @Override
        public void onLogout() {
            Log.d(TAG, "onError: no listener set");
        }
    };

    RetryPolicy policy = new RetryPolicy() {
        @Override
        public int getCurrentTimeout() {
            return 60000;
        }

        @Override
        public int getCurrentRetryCount() {
            return 0;
        }

        @Override
        public void retry(VolleyError error) throws VolleyError {

        }
    };

    /**
     * volley RequestQueue
     */
    private RequestQueue mRequestQueue;
    /**
     * request method
     */
    private int method = Request.Method.GET;
    /**
     * headers of the request
     */
    private Map<String, String> headers = new HashMap<>();
    /**
     * post or patch parameters
     */
    private Map<String, String> postParams = new HashMap<>();

    /**
     * constructor for Api connection
     *
     * @param context for create RequestQueue
     */
    public BaseApiConnection(Context context) {

        this.context = context;
    }

    protected Context getContext() {
        return context;
    }

    public BaseApiConnection setmApiInterface(BaseApiInterface mApiInterface) {
        this.mApiInterface = mApiInterface;
        return this;
    }

    /**
     * adds a new header
     *
     * @param key   is header key
     * @param value is header value
     */
    protected void addHeader(String key, String value) {
        headers.put(key, value);
    }

    /**
     * adds a new parameter to post or patch request
     *
     * @param key   is parameter key
     * @param value is parameter value
     */
    public void addPostParam(String key, String value) {
        postParams.put(key, value);
        Log.d("postParams", postParams.toString());
    }

    protected Map<String, String> getPostParams() {
        return postParams;
    }

    /**
     * sets header
     *
     * @param headers is a HashMap
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * sets postParams
     *
     * @param postParams is a HashMap
     */
    public void setPostParams(Map<String, String> postParams) {
        this.postParams = postParams;
    }


    public abstract String getUrl();

    /**
     * sets request method
     *
     * @param method is a integer and can be any of Method.GET or Method.POST or Method.DELETE or
     *               Method.PATCH or Method.PUT or ...
     */
    public void setMethod(int method) {
        this.method = method;

    }

    /**
     * Singleton for RequestQueue
     *
     * @return requestQueue
     */
    protected RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return mRequestQueue;
    }

    /**
     * starts to send request to server
     */
    public void start() {
        Log.d(TAG, "Method " + (method == Request.Method.POST ? "post" : "GET"));
        StringRequest request = new StringRequest(method, getUrl(), this, this) {
            @Override
            public Map<String, String> getHeaders() {
                return getDefaultHeader();
            }

            @Override
            protected Map<String, String> getParams() {
                Log.d("postParams", postParams.toString());
                return getDefultPostParams();
            }
        };

        //request.setRetryPolicy(policy);

        Log.d(TAG, request.toString());

        request.setShouldCache(false);

        getRequestQueue().add(request);

    }

    /**
     * resets the Request method and header and post parameters
     */
    private void reset() {
        method = Request.Method.GET;
        headers.clear();
        postParams.clear();
    }

    /**
     * when request fails this method is called
     *
     * @param error is error of request that should be determined
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        reset();
        if (error != null && error.networkResponse != null)
            Log.d("ApiConnection", "OnError : " + new String(error.networkResponse.data));

        if (error instanceof NoConnectionError) {
            Log.d(TAG, "onErrorResponse: no connection");
            mApiInterface.onError(context.getString(R.string.error_noConnection));
            return;
        }
        if (error instanceof TimeoutError) {
            Log.d(TAG, "onErrorResponse: Timeout Error");
            mApiInterface.onError(context.getString(R.string.error_timeout));
            return;
        }
        if (error instanceof ServerError) {
            Log.d(TAG, "onErrorResponse: server error");
            mApiInterface.onError(context.getString(R.string.error_server));
            return;
        }
        if (error instanceof NetworkError) {
            Log.d(TAG, "onErrorResponse: network error");
            mApiInterface.onError(context.getString(R.string.error_network));
            return;
        }
        if (error == null || error.networkResponse == null) {
            Log.d(TAG, "onErrorResponse: unknown error");
            mApiInterface.onError(context.getString(R.string.error_unknown));
            return;
        }

        if (error.networkResponse.statusCode == 401) {
            mApiInterface.onLogout();
            return;
        }

        mApiInterface.onError("");
    }

    /**
     * when request send successfully this method will be called
     *
     * @param response id response of request
     */
    @Override
    public void onResponse(String response) {
        reset();
        Log.d(TAG, "onResponse: " + response);
        mApiInterface.onResponse(response);
    }

    public Map<String, String> getDefultPostParams() {
        if (AccountManager.getInstanse(getContext()).isLogin()) {
            postParams.put("mobile", AccountManager.getInstanse(getContext()).getPhoneNumber());
        }
        Log.d(TAG, "getDefaultPostParams: " + postParams);
        return postParams;
    }

    protected Map<String, String> getDefaultHeader() {
        if (AccountManager.getInstanse(context).isLogin()) {
            headers.put(AUTHORISATION, AccountManager.getInstanse(context).getToken());
        }
        Log.d(TAG, "getDefaultHeader: " + headers);
        return headers;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
