package com.example.taxiapp.utils

import com.google.android.gms.maps.model.LatLng

object FetchUrl {
    fun getUrl(origin: LatLng, dest: LatLng): String {
        val str_origin = "origin=" + origin.latitude + "," + origin.longitude
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude
        val sensor = "sensor=false"
        val parameters = "$str_origin&$str_dest&$sensor"
        val output = "json"
        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters"
    }
}