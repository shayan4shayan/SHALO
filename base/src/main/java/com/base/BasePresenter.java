package com.base;

import android.content.Context;

import com.base.api.BaseApiConnection;
import com.base.api.BaseApiInterface;


/**
 * Created by shayan on 6/30/2017.
 */

public class BasePresenter implements BaseApiInterface {

    private final Context context;
    private final BaseUIInterface baseUIInterface;
    BaseApiConnection apiConnection;

    public BasePresenter(Context context, BaseUIInterface baseUIInterface, BaseApiConnection apiConnection) {

        this.context = context;
        this.baseUIInterface = baseUIInterface;
        this.apiConnection = apiConnection;
    }

    @Override
    public void onResponse(String response) {

    }

    public Context getContext() {
        return context;
    }

    /**
     * display snackBar by default
     *
     * @param message is Error message to display
     */
    @Override
    public void onError(String message) {
        baseUIInterface.displaySnackBar(message);
    }

    @Override
    public void onLogout() {

    }
}
