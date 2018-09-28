package com.syfract.sabt.api

import android.content.Context
import android.net.Uri
import com.android.volley.Request
import com.base.api.BaseApiConnection

/**
 * Created by shayan4shayan on 4/7/18.
 */
class RegisterApi(context:Context ,val name:String,val last:String,val email:String,val username:String,val password:String) : BaseApiConnection(context){
    override fun getUrl() = Uri.parse(baseUrl).buildUpon().appendPath("signup").toString() +"/"

    override fun start() {
        setMethod(Request.Method.POST)
        addPostParam("first_name",name)
        addPostParam("last_name",last)
        addPostParam("username",username)
        addPostParam("email",email)
        addPostParam("password",password)
        super.start()
    }
}