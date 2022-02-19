package com.example.taxiapp.utils

import android.R
import android.os.AsyncTask
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import org.json.JSONObject


/**
 * Created by ocittwo on 11/14/16.
 *
 * @Author Ahmad Rosid
 * @Email ocittwo@gmail.com
 * @Github https://github.com/ar-android
 * @Web http://ahmadrosid.com
 */
class RouteDrawerTask(private val mMap: GoogleMap?) :
    AsyncTask<String?, Int?, List<List<HashMap<String, String>>>?>() {
    private var lineOptions: PolylineOptions? = null
    private var routeColor = 0
    override fun doInBackground(vararg jsonData: String?): List<List<HashMap<String, String>>>? {
        val jObject: JSONObject
        var routes: List<List<HashMap<String, String>>>? = null
        try {
            jObject = JSONObject(jsonData[0])
            val parser = DataRouteParser()
            Log.d("RouteDrawerTask", parser.toString())

            // Starts parsing data
            routes = parser.parse(jObject)
            Log.d("RouteDrawerTask", "Executing routes")
            Log.d("RouteDrawerTask", routes.toString())
        } catch (e: Exception) {
            Log.d("RouteDrawerTask", e.toString())
            e.printStackTrace()
        }
        return routes
    }

    override fun onPostExecute(result: List<List<HashMap<String, String>>>?) {
        result?.let { drawPolyLine(it) }
    }

    private fun drawPolyLine(result: List<List<HashMap<String, String>>>) {
        var points: ArrayList<LatLng?>
        lineOptions = null
        for (i in result.indices) {
            points = ArrayList()
            lineOptions = PolylineOptions()

            // Fetching i-th route
            val path = result[i]

            // Fetching all the points in i-th route
            for (j in path.indices) {
                val point = path[j]
                val lat = point["lat"]!!.toDouble()
                val lng = point["lng"]!!.toDouble()
                val position = LatLng(lat, lng)
                points.add(position)
            }

            // Adding all the points in the route to LineOptions
            lineOptions!!.addAll(points)
            lineOptions!!.width(6f)
            routeColor =
                ContextCompat.getColor(DrawRouteMaps.getContext()!!, R.color.holo_red_dark)
            if (routeColor == 0) lineOptions!!.color(-0xf570f8) else lineOptions!!.color(routeColor)
        }

        // Drawing polyline in the Google Map for the i-th route
        if (lineOptions != null && mMap != null) {
            mMap.addPolyline(lineOptions as PolylineOptions)
        } else {
            Log.d("onPostExecute", "without Polylines draw")
        }
    }
}