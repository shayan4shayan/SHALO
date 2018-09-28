package com.syfract.sabt

import android.net.Uri

/**
 * Created by shayan4shayan on 4/3/18.
 */
class Model {
    companion object {
        val url = "http://sabt.syfract.com/media"
    }

    data class Application(val id: Int) {
        var name = ""
        var versionName = ""
        var version = 0
        var installedVersion = 0
        var image = ""
            get() = Uri.parse(url).buildUpon().appendEncodedPath(field).build().toString()

        var isInstalled = false
            get() = installedVersion > 0
        var isUpdate = false
            get() = version > installedVersion
        var packageName = ""
        var description = ""
        var download = ""
            get() = Uri.parse(url).buildUpon().appendEncodedPath(field).build().toString()
    }
}