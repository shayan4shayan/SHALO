package com.syfract.sabt.api

import android.content.Context
import android.net.Uri
import com.base.api.BaseApiConnection

/**
 * Created by shayan4shayan on 4/9/18.
 */
class PackagesListApi(context: Context) : BaseApiConnection(context) {
    override fun getUrl() = Uri.parse(baseUrl).buildUpon().appendPath("apps").build().toString() + "/"
}