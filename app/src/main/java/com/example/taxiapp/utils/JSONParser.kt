package com.example.taxiapp.utils

import android.util.Log
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class JSONParser {
    @Throws(IOException::class)
    fun getJsonRoutePoint(strUrl: String): String {
        var data = ""
        var iStream: InputStream? = null
        var urlConnection: HttpURLConnection? = null
        try {
            val url = URL(strUrl)

            // Creating an http connection to communicate with url
            urlConnection = url.openConnection() as HttpURLConnection

            // Connecting to url
            urlConnection.connect()

            // Reading data from url
            iStream = urlConnection.inputStream
            val br = BufferedReader(InputStreamReader(iStream))
            val sb = StringBuffer()
            var line: String? = ""
            while (br.readLine().also { line = it } != null) {
                sb.append(line)
            }
            data = sb.toString()
            Log.d("getJsonRoutePoint", data)
            br.close()
        } catch (e: Exception) {
            Log.d("Exception", e.toString())
        } finally {
            iStream?.close()
            urlConnection?.disconnect()
        }
        return data
    }
}