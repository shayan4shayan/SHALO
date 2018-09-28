package com.syfract.sabt.parser

import com.syfract.sabt.Model
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by shayan4shayan on 4/9/18.
 */
class Parser {
    companion object {
        fun parsePackage(res: String): List<Model.Application> {
            val list = JSONArray(res)
            return (0 until list.length()).map { list.getJSONObject(it) }.map { parsePackage(it) }
        }

        private fun parsePackage(res: JSONObject): Model.Application {
            val app = Model.Application(res.getInt("id"))
            app.packageName = res.getString("package_name")
            app.version = res.getInt("version")
            app.description = res.getString("description")
            app.image = res.getString("icon")
            app.versionName = res.getString("version_name")
            app.download = res.getString("file")
            return app
        }
    }
}