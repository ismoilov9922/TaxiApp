package com.example.taxiapp.utils

import android.os.AsyncTask
import android.util.Log
import com.google.android.gms.maps.GoogleMap
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


/**
 * Created by ocittwo on 11/14/16.
 *
 * @Author Ahmad Rosid
 * @Email ocittwo@gmail.com
 * @Github https://github.com/ar-android
 * @Web http://ahmadrosid.com
 */
class DrawRoute(private val mMap: GoogleMap) :
    AsyncTask<String?, Void?, String>() {
    override fun doInBackground(vararg url: String?): String {
        var data = ""
        try {
            data = getJsonRoutePoint(url[0].toString())
            Log.d("Background Task data", data)
        } catch (e: Exception) {
            Log.d("Background Task", e.toString())
        }
        return data
    }

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)
        val routeDrawerTask = RouteDrawerTask(mMap)
        routeDrawerTask.execute(result)
    }

    /**
     * A method to download json data from url
     */
    @Throws(IOException::class)
    private fun getJsonRoutePoint(strUrl: String): String {
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
            iStream!!.close()
            urlConnection!!.disconnect()
        }
        return data
    }
}