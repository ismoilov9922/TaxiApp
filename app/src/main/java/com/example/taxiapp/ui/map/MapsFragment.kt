package com.example.taxiapp.ui.map

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.taxiapp.databinding.FragmentMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.example.taxiapp.R
import com.example.taxiapp.map.AnimatedPolyline
import com.example.taxiapp.utils.*


class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnPolylineClickListener,
    ActivityCompat.OnRequestPermissionsResultCallback {

    private lateinit var binding: FragmentMapsBinding
    private lateinit var map: GoogleMap
    private val DEFOULT_ZOOM = 13f
    private var addressLatLng: LatLng = LatLng(46.4423, 69.3257463)
    private lateinit var fusedLocationProvider: FusedLocationProviderClient
    private var myCurrentLocation = LatLng(41.4423, 60.3257463)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        map.isMyLocationEnabled = true
        map.uiSettings.isZoomControlsEnabled = false
        binding.myLocation.setOnClickListener {
            getDeviseLocation()
        }
        val sydney = LatLng(41.23, 69.327)
        val a = LatLng(41.0246, 69.9233)
        val b = LatLng(41.544, 69.45543)
        map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        map.addMarker(MarkerOptions().position(a).title("Marker2 in Sydney"))
        map.addMarker(MarkerOptions().position(b).title("Marker3 in Sydney"))
        Dexter.withContext(binding.root.context)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    getDeviseLocation()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?,
                ) {
                    p1?.continuePermissionRequest()
                }
            }).check()
        map.setOnCameraMoveListener {
            binding.polyLine.visibility = View.GONE
        }
        map.setOnMarkerClickListener(object : GoogleMap.OnMapClickListener,
            GoogleMap.OnMarkerClickListener {
            override fun onMapClick(p0: LatLng) {
                addressLatLng = p0
                binding.polyLine.visibility = View.VISIBLE
            }

            override fun onMarkerClick(marker: Marker): Boolean {
                DrawRouteMaps.getInstance(requireContext())
                    ?.draw(myCurrentLocation, marker.position, map)
                return false
            }
        })
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myCurrentLocation, DEFOULT_ZOOM))
    }


    private fun getDeviseLocation() {
        fusedLocationProvider =
            LocationServices.getFusedLocationProviderClient(binding.root.context)
        try {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            val lastLocation = fusedLocationProvider.lastLocation
            lastLocation.addOnCompleteListener { task ->
                val result = task.result
                if (result != null) {
                    myCurrentLocation = LatLng(result.latitude, result.longitude)
                    map.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            myCurrentLocation, DEFOULT_ZOOM
                        )
                    )
//                    addOriginDestinationMarkerAndGet(myCurrentLocation)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPolylineClick(p0: Polyline) {

    }

}