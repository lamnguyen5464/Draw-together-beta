package com.example.canvas_android.utils


import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class JsonHelper {
    companion object {

        fun getJsonObjFromString(string: String): JSONObject {
            return JSONObject(string)
        }

        fun getJsonArrayromString(string: String): JSONArray {
            try {
                return JSONArray(string)
            } catch (ioException: IOException) {
                return JSONArray("[]")
            }
        }

        fun getFieldSafely(obj: JSONObject?, fieldName: String): String {
            if (obj == null) return ""
            return if (obj.has(fieldName)) obj.getString(fieldName) else "";
        }

    }
}