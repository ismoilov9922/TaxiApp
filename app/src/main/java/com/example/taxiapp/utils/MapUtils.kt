package com.example.taxiapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.MailTo.parse
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.taxiapp.R
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task
import java.io.IOException
import java.util.*


object MapUtils {
//
//    fun drawPath(
//        sour_lat: Double, sour_lng: Double, dest_lat: Double, dest_lng: Double, mMap: GoogleMap,
//        context: Context
//    ) {
//        try {
//            val LatLongB = LatLngBounds.Builder()
//            val current = LatLng(sour_lat, sour_lng)
//            val destination = LatLng(dest_lat, dest_lng)
//            val options = PolylineOptions()
//            val colorVal: Int = Color.parseColor("#3690E9")
//            options.color(colorVal)
//            options.width(15f)
//            val url = getURL(LatLng(sour_lat, sour_lng), LatLng(dest_lat, dest_lng))
//            kotlin.run {
//                val result = URL(url).readText()
//                val parser = Parser()
//                val stringBuilder: StringBuilder = StringBuilder(result)
//                val json: JsonObject = parser.parse(stringBuilder) as JsonObject
//
//                // get to the correct element in JsonObject
//                val routes = json.array<JsonObject>("routes")
//
//                if (routes!!["legs"]["steps"].size > 0) {
//
//
//                    val points = routes!!["legs"]["steps"][0] as JsonArray<JsonObject>
//
//                    //-----------to get distance and duration
//                    val getDuration = routes!!["legs"]["duration"][0] as JsonObject
//                    val getDistance = routes!!["legs"]["distance"][0] as JsonObject
//                    val duration = getDuration.get("text").toString()
//                    val distance = getDistance.get("text").toString()
//
//                    Log.e("printNowGH", distance)
//                    Log.e("printDis1", duration)
//                    //-----------to get total distance-----------------------------------
//                    try {
//                        if (distance.contains(",")) {
//                            var amtDemo = distance.replace(",", "")
//                            var amt = amtDemo.replace("km", "")
//                            TOTAL_DISTANCE = amt.toDouble()
//                        } else if (distance.contains("km")) {
//                            var amt = distance.replace("km", "")
//                            TOTAL_DISTANCE = amt.toDouble()
//                        }
//
//                    } catch (ex: Exception) {
//
//                    }
//
//                    //--------------------to get duration---------------------------
//                    try {
//
//                        TOTAL_DURATION = 0
//
//                        if (duration.contains("day") || duration.contains("days")) {
//                            val minArray = duration.split(" ")
//
//                            if (minArray.size == 6) {
//                                TOTAL_DURATION = minArray.get(0).toInt() * 24 * 60 + minArray.get(2)
//                                    .toInt() * 60 + minArray.get(4).toInt()
//                            } else if (minArray.size == 4) {
//                                if (duration.contains("hour") || duration.contains("hours"))
//                                    TOTAL_DURATION =
//                                        minArray.get(0).toInt() * 24 * 60 + minArray.get(2)
//                                            .toInt() * 60
//                                else if (duration.contains("mins"))
//                                    TOTAL_DURATION =
//                                        minArray.get(0).toInt() * 24 * 60 + minArray.get(2).toInt()
//                            } else if (minArray.size == 2) {
//                                TOTAL_DURATION = minArray.get(0).toInt() * 24 * 60
//                            }
//
//
//                        } else if (duration.contains("hour") || duration.contains("hours")) {
//                            val minArray = duration.split(" ")
//                            if (minArray.size == 4) {
//                                TOTAL_DURATION =
//                                    minArray.get(0).toInt() * 60 + minArray.get(2).toInt()
//                            } else if (minArray.size == 2) {
//                                TOTAL_DURATION = minArray.get(0).toInt() * 60
//                            }
//
//                        } else if (duration.contains("mins")) {
//                            val minArray = duration.split(" ")
//                            if (minArray.size == 2)
//                                TOTAL_DURATION = minArray.get(0).toInt()
//                        }
//
//                    } catch (ex: Exception) {
//                    }
//
//
//                    // For every element in the JsonArray, decode the polyline string and pass all points to a List
//                    val polypts =
//                        points.flatMap { decodePoly(it.obj("polyline")?.string("points")!!) }
//                    // Add  points to polyline and bounds
//
//                    var newLatLng: ArrayList<LatLng> = ArrayList()
//
//
//                    options.add(current)
//                    LatLongB.include(current)
//                    for (point in polypts) {
//                        options.add(point)
//                        LatLongB.include(point)
//                        newLatLng!!.add(point)
//
//                    }
//                    Log.e("printList", newLatLng.toString())
//                    NewListLatLng = newLatLng!!
//                    options.add(destination)
//                    LatLongB.include(destination)
//                    // build bounds
//                    val bounds = LatLongB.build()
//                    // add polyline to the map
//                    //  mMap!!.addPolyline(options)
//                    // show map with route centered
//                    mMap!!.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
//
//                    addMarkerSource(mMap, LatLng(sour_lat, sour_lng), context, duration)
//                    addMarkerDest(mMap, LatLng(dest_lat, dest_lng), context, duration)
//
//
//
//                    options.width(15f)
//                    options.color(context.resources.getColor(R.color.path_color))
//                    options.startCap(SquareCap())
//                    options.endCap(SquareCap())
//                    options.jointType(JointType.ROUND)
//                    blackPolyLine = mMap.addPolyline(options)
//
//                    /* val greyOptions = PolylineOptions()
//                     greyOptions.width(15f)
//                     // greyOptions.color(Color.GRAY)
//                     greyOptions.color(context.resources.getColor(R.color.very_light_gray))
//                     greyOptions.startCap(SquareCap())
//                     greyOptions.endCap(SquareCap())
//                     greyOptions.jointType(ROUND)
//                     greyPolyLine = mMap.addPolyline(greyOptions)
//
//                     animatePolyLine()*/
//
//
//                    /*   val greyOptions = PolylineOptions()
//                       greyOptions.width(25f)
//                       // greyOptions.color(Color.GRAY)
//                       greyOptions.color(context.resources.getColor(R.color.red))
//                       greyOptions.startCap(SquareCap())
//                       greyOptions.endCap(SquareCap())
//                       greyOptions.jointType(ROUND)
//                       greyOptions.add(LatLng(30.71485,76.69125), LatLng(30.72447,76.72061))
//                       greyPolyLine = mMap.addPolyline(greyOptions)*/
//
//
//                }
//            }
//        } catch (ex: IndexOutOfBoundsException) {
//        }
//        //-----------------------------------------------------------------------
//
//    }

    const val ENCODED_POINTS = "encodedPoints"
    const val LAT_LNG_POINT = "latLngPoint"
    const val MARKER = "marker"
//
//    fun readEncodedPolyLinePointsFromCSV(context: Context, lineKeyword: String): List<LatLng> {
//        val `is`: InputStream =
//            context.resources.openRawResource(com.google.maps.android.R.raw.amu_basic_folder)
//        val reader = BufferedReader(InputStreamReader(`is`, Charset.forName("UTF-8")))
//        var line = ""
//        val latLngList: MutableList<LatLng> = ArrayList()
//        try {
//            while (reader.readLine().also { line = it } != null) {
//                val tokens = line.split(",").toTypedArray()
//                if (tokens[0].trim { it <= ' ' } == lineKeyword && tokens[1].trim { it <= ' ' } == ENCODED_POINTS) {
//                    latLngList.addAll(PolyUtil.decode(tokens[2].trim { it <= ' ' }
//                        .replace("\\\\", "\\")))
//                    for (lat in latLngList) {
//                    }
//                } else {
//                }
//            }
//        } catch (e1: IOException) {
//            e1.printStackTrace()
//        }
//        return latLngList
//    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(context: Context): LatLng {
        var latLng = LatLng(41.433, 69.35423)
        val client = LocationServices.getFusedLocationProviderClient(context)
        val task: Task<Location> = client.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                Log.d(
                    "MY_LOCATION",
                    "getCurrentLocation: ${location.latitude} , ${location.longitude}"
                )
                Toast.makeText(
                    context,
                    "${location.latitude} , ${location.longitude}",
                    Toast.LENGTH_SHORT
                ).show()
                latLng = LatLng(location.latitude, location.longitude)
            }
        }
        return latLng
    }

    fun getCarBitmap(context: Context): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, R.drawable.dest_marker)
        vectorDrawable!!.setBounds(
            0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            20, 20, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    fun getDestinationBitmap(): Bitmap {
        val height = 20
        val width = 20
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), paint)
        return bitmap
    }

    fun getCompleteAddressFromLatLng(lat: Double, lng: Double, context: Context): String {
        var strAdd = ""
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            if (addresses != null) {
                val returnedAddress = addresses[0]
                val strReturnedAddress = StringBuilder("")

                for (i in 0..returnedAddress.maxAddressLineIndex) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                }
                strAdd = strReturnedAddress.toString()
                Log.w("Current_loction_address", strReturnedAddress.toString())
            } else {
                Log.w("Current_loction_address", "No Address returned!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.w("Current_loction_address", "Canont get Address!")
        }
        return strAdd
    }

    fun getLatLngFromAddress(context: Context, strAddress: String): LatLng? {
        val coder = Geocoder(context)
        val address: List<Address>?
        var p1: LatLng? = null

        try {
            address = coder.getFromLocationName(strAddress, 5)
            if (address == null) {
                return null
            }
            val location = address[0]
            p1 = LatLng(location.latitude, location.longitude)

        } catch (ex: IOException) {
            //  Log.e("exception:",ex.message)
            ex.printStackTrace()
        }
        return p1
    }

    private fun getURL(from: LatLng, to: LatLng): String {
        val origin = "origin=" + from.latitude + "," + from.longitude
        val dest = "&destination=" + to.latitude + "," + to.longitude
        // val sensor = "sensor=false"
        val key = "&key=AIzaSyDKpkPtlJLZC0KR-p-cvb4QXG_5JtXFL40"
        val params = "$origin&$dest&$key"
        return "https://maps.googleapis.com/maps/api/directions/json?$params"
    }

    fun addMarkerSource(mMap: GoogleMap, latLng: LatLng, context: Context, duration: String) {
        mMap.addMarker(
            MarkerOptions()
                .position(LatLng(latLng.latitude, latLng.longitude))
                .title("")
                .snippet("")
                .icon(
                    BitmapDescriptorFactory.fromBitmap(
                        getMarkerBitmapFromViewSource(
                            LatLng(
                                latLng.latitude,
                                latLng.longitude
                            ), context, duration
                        )
                    )
                )
        )

    }

    fun addMarkerDest(mMap: GoogleMap, latLng: LatLng, context: Context, duration: String) {
        mMap.addMarker(
            MarkerOptions()
                .position(LatLng(latLng.latitude, latLng.longitude))
                .title("")
                .snippet("")
                .icon(
                    BitmapDescriptorFactory.fromBitmap(
                        getMarkerBitmapFromViewDest(
                            LatLng(
                                latLng.latitude,
                                latLng.longitude
                            ), context, duration
                        )
                    )
                )
        )
    }

    fun getMarkerBitmapFromViewSource(latLng: LatLng, context: Context, duration: String): Bitmap {

        val customMarkerView =
            (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.map_infowindow,
                null
            )
        val sAddress = customMarkerView.findViewById(R.id.sAddress_marker) as TextView
        val sTime = customMarkerView.findViewById(R.id.sTime_infoWindow) as TextView
        sTime.text = duration
        sAddress.text = getCompleteAddressFromLatLng(latLng.latitude, latLng.longitude, context)
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        customMarkerView.layout(
            0,
            0,
            customMarkerView.measuredWidth,
            customMarkerView.measuredHeight
        )
        customMarkerView.buildDrawingCache()
        val returnedBitmap = Bitmap.createBitmap(
            customMarkerView.measuredWidth, customMarkerView.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(returnedBitmap)
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
        val drawable = customMarkerView.background
        if (drawable != null)
            drawable.draw(canvas)
        customMarkerView.draw(canvas)
        return returnedBitmap
    }

    fun getMarkerBitmapFromViewDest(latLng: LatLng, context: Context, duration: String): Bitmap {

        val customMarkerView =
            (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.dest_view,
                null
            )

        var durCount: String = ""
        var durUnit: String = ""
        val durArray = duration.split(" ")
        if (durArray.size == 2) {
            durCount = durArray.get(0)
            if (durArray.get(1).equals("mins"))
                durUnit = "MIN"
            else if (durArray.get(1).equals("hour"))
                durUnit = "Hr"
            else if (durArray.get(1).equals("hours"))
                durUnit = "Hrs"
        } else if (durArray.size == 4) {
            durCount = durArray.get(0)
            if (durArray.get(1).equals("hour"))
                durUnit = "Hr"
            else if (durArray.get(1).equals("hours"))
                durUnit = "Hrs"
        }

        val destTime_marker = customMarkerView.findViewById(R.id.destTime_marker) as TextView
        val destTime2_marker = customMarkerView.findViewById(R.id.destTime2_marker) as TextView
        destTime_marker.text = duration
        //   destTime_marker.text = durCount
        //   destTime2_marker.text = durUnit
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        customMarkerView.layout(
            0,
            0,
            customMarkerView.measuredWidth,
            customMarkerView.measuredHeight
        )
        customMarkerView.buildDrawingCache()
        val returnedBitmap = Bitmap.createBitmap(
            customMarkerView.measuredWidth, customMarkerView.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(returnedBitmap)
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
        val drawable = customMarkerView.background
        if (drawable != null)
            drawable.draw(canvas)
        customMarkerView.draw(canvas)
        return returnedBitmap
    }

//    fun drawPath(myMap: GoogleMap, result: String?) {
//        var line = greyPolyLine
//        if (line != null) {
//            myMap.clear()
//        }
//        myMap.addMarker(
//            MarkerOptions().position(endLatLng).icon(
//                BitmapDescriptorFactory.fromResource(R.drawable.redpin_marker)
//            )
//        )
//        myMap.addMarker(
//            MarkerOptions().position(startLatLng).icon(
//                BitmapDescriptorFactory.fromResource(R.drawable.redpin_marker)
//            )
//        )
//        try {
//            val json = JSONObject(result)
//            val routeArray = json.getJSONArray("routes")
//            val routes = routeArray.getJSONObject(0)
//            val overviewPolylines = routes
//                .getJSONObject("overview_polyline")
//            val encodedString = overviewPolylines.getString("points")
//            val list = decodePoly(encodedString)
//            for (z in 0 until list.size - 1) {
//                val src = list[z]
//                val dest = list[z + 1]
//                line = myMap.addPolyline(
//                    PolylineOptions()
//                        .add(
//                            LatLng(src.latitude, src.longitude),
//                            LatLng(dest.latitude, dest.longitude)
//                        )
//                        .width(5f).color(Color.BLUE).geodesic(true)
//                )
//            }
//        } catch (e: java.lang.Exception) {
//            e.printStackTrace()
//        }
//    }

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