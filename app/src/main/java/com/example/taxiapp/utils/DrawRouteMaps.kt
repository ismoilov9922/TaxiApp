package com.example.taxiapp.utils

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

class DrawRouteMaps {
    private var context: Context? = null
    fun draw(origin: LatLng, destination: LatLng, googleMap: GoogleMap?): DrawRouteMaps? {
        val url_route: String = FetchUrl.getUrl(origin, destination)
        val drawRoute = DrawRoute(googleMap!!)
        drawRoute.execute(url_route)
        return instance
    }

    companion object {
        private var instance: DrawRouteMaps? = null
        fun getInstance(context: Context?): DrawRouteMaps? {
            instance = DrawRouteMaps()
            instance!!.context = context
            return instance
        }

        fun getContext(): Context? {
            return instance!!.context
        }
    }
}