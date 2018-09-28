package com.syfract.sabt.api

import android.content.Context
import android.net.Uri
import com.android.volley.Request
import com.base.api.BaseApiConnection

/**
 * Created by shayan4shayan on 4/5/18.
 */
class LoginApi(context: Context, val username: String, val password: String) : BaseApiConnection(context) {
    override fun getUrl() = Uri.parse(baseUrl).buildUpon()
            .appendPath("login").build().toString() + "/"

    override fun start() {
        setMethod(Request.Method.POST)
        addPostParam("username", username)
        addPostParam("password", password)
        super.start()
    }
}