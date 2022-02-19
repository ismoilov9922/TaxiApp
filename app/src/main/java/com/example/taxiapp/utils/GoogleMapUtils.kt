package com.example.taxiapp.utils

import android.graphics.Color
import com.example.taxiapp.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import org.json.JSONObject
import java.io.*


object GoogleMapUtils {
    fun getURL(from: LatLng, to: LatLng): String {
        val origin = "origin=" + from.latitude + "," + from.longitude
        val dest = "destination=" + to.latitude + "," + to.longitude
        val sensor = "sensor=false"
        val params = "$origin&$dest&$sensor"
        return "https://maps.googleapis.com/maps/api/directions/json?$params"
    }

    var line: Polyline? = null
    fun makeURL(
        sourcelat: Double, sourcelog: Double, destlat: Double,
        destlog: Double
    ): String {
        val urlString = StringBuilder()
        urlString.append("http://maps.googleapis.com/maps/api/directions/json")
        urlString.append("?origin=") // from
        urlString.append(sourcelat.toString())
        urlString.append(",")
        urlString.append(sourcelog.toString())
        urlString.append("&destination=") // to
        urlString.append(destlat.toString())
        urlString.append(",")
        urlString.append(destlog.toString())
        urlString.append("&sensor=false&mode=driving&alternatives=true")
        return urlString.toString()
    }

    fun drawPath(startLatLng: LatLng, endLatLng: LatLng, myMap: GoogleMap, result: String?) {
        if (line != null) {
            myMap.clear()
        }
//        myMap.addMarker(
//            MarkerOptions().position(endLatLng).icon(
//                BitmapDescriptorFactory.fromResource(R.drawable.ic_location)
//            )
//        )
//        myMap.addMarker(
//            MarkerOptions().position(startLatLng).icon(
//                BitmapDescriptorFactory.fromResource(R.drawable.ic_my_location)
//            )
//        )
        try {
            val json = JSONObject(result)
            val routeArray = json.getJSONArray("routes")
            val routes = routeArray.getJSONObject(0)
            val overviewPolylines = routes
                .getJSONObject("overview_polyline")
            val encodedString = overviewPolylines.getString("points")
            val list: List<LatLng> = decodePoly(encodedString)
            for (z in 0 until list.size - 1) {
                val src = list[z]
                val dest = list[z + 1]
                line = myMap.addPolyline(
                    PolylineOptions()
                        .add(
                            LatLng(src.latitude, src.longitude),
                            LatLng(dest.latitude, dest.longitude)
                        )
                        .width(5f).color(Color.BLUE).geodesic(true)
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun decodePoly(encoded: String): List<LatLng> {
        val poly: MutableList<LatLng> = ArrayList()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val p = LatLng(
                lat.toDouble() / 1E5,
                lng.toDouble() / 1E5
            )
            poly.add(p)
        }
        return poly
    }
}