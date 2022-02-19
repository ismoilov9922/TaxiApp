package com.example.taxiapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


/**
 * Created by ocittwo on 11/14/16.
 *
 * @Author Ahmad Rosid
 * @Email ocittwo@gmail.com
 * @Github https://github.com/ar-android
 * @Web http://ahmadrosid.com
 */
class DrawMarker internal constructor(context: Context) {
    private val context: Context = context
    fun draw(googleMap: GoogleMap, location: LatLng?, resDrawable: Int, title: String?) {
        val circleDrawable = context.let { ContextCompat.getDrawable(it, resDrawable) }
        val markerIcon = getMarkerIconFromDrawable(circleDrawable)
        googleMap.addMarker(
            MarkerOptions()
                .position(location!!)
                .title(title)
                .icon(markerIcon)
        )
    }

    private fun getMarkerIconFromDrawable(drawable: Drawable?): BitmapDescriptor {
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    companion object {
        var INSTANCE: DrawMarker? = null
        fun getInstance(context: Context): DrawMarker? {
            INSTANCE = DrawMarker(context)
            return INSTANCE
        }
    }

}