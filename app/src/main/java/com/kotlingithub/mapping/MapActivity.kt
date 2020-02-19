package com.kotlingithub.mapping

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.kotlingithub.R
import com.kotlingithub.routeDraw.GoogleMapsPath

class MapActivity : FragmentActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerDragListener,
    GoogleMap.OnMapLongClickListener, View.OnClickListener {

    /* Our Map */
    private var mMap: GoogleMap? = null

    /* To store longitude and latitude from map */
    private var longitude: Double = 0.toDouble()
    private var latitude: Double = 0.toDouble()

    /* Buttons */
    private var buttonSave: ImageButton? = null
    private var buttonCurrent: ImageButton? = null
    private var buttonView: ImageButton? = null

    /* Google ApiClient */
    private var googleApiClient: GoogleApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        init()
    }

    private fun init() {
        /* Obtain the SupportMapFragment and get notified when the map is ready to be used. */
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        /* Initializing google api client */
        googleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        /* Initializing views and adding onclick listeners */
        buttonSave = findViewById(R.id.buttonSave)
        buttonCurrent = findViewById(R.id.buttonCurrent)
        buttonView = findViewById(R.id.buttonView)
        buttonSave!!.setOnClickListener(this)
        buttonCurrent!!.setOnClickListener(this)
        buttonView!!.setOnClickListener(this)
    }

    override fun onStart() {
        googleApiClient!!.connect()
        super.onStart()
    }

    override fun onStop() {
        googleApiClient!!.disconnect()
        super.onStop()
    }

    /* Getting current location */
    private fun getCurrentLocation() {
        mMap!!.clear()
        /* Creating a location object */
        @SuppressLint("MissingPermission") val location =
            LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
        if (location != null) {
            /* Getting longitude and latitude */
            longitude = location.longitude
            latitude = location.latitude

            /* moving the map to location */
            moveMap()
        }
    }

    /* Function to move the map */
    private fun moveMap() {
        /* String to display current latitude and longitude */
        val msg = "$latitude, $longitude"

        /* Creating a LatLng Object to store Coordinates */
        val latLng = LatLng(latitude, longitude)

        /* Adding marker on map to current location */
        mMap!!.addMarker(
            MarkerOptions()
                .position(latLng) //setting position
                .draggable(true) //Making the marker draggable
                .title("Current Location")
        ) //Adding a title

        //Moving the camera
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))

        /* Animating the camera */
        mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15f))

        /* Displaying current coordinates in toast */
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
//        drawPollyLine()
    }

    override fun onClick(v: View) {
        if (v === buttonCurrent) {
            getCurrentLocation()
            moveMap()
        }
    }

    override fun onConnected(bundle: Bundle?) {
        Log.d("onMap", "onConnected: ")
        getCurrentLocation()
    }

    override fun onConnectionSuspended(i: Int) {
        Log.d("onMap", "onConnected: ")
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.d("onMap", "onConnected: ")
    }

    override fun onMapLongClick(latLng: LatLng) {
        /* Clearing all the markers */
        mMap!!.clear()
        /* Adding a new marker to the current pressed position */
        mMap!!.addMarker(
            MarkerOptions()
                .position(latLng)
                .draggable(true)
        )
    }

    override fun onMarkerDragStart(marker: Marker) {

    }

    override fun onMarkerDrag(marker: Marker) {

    }

    override fun onMarkerDragEnd(marker: Marker) {
        /* Getting the coordinates */
        latitude = marker.position.latitude
        longitude = marker.position.longitude

        /* Moving the map */
        moveMap()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        /* this line change map styling and json file is used for it. */
        //        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MapActivity.this, R.raw.map));

        val latLng = LatLng(-34.0, 151.0)
        mMap!!.addMarker(MarkerOptions().position(latLng).draggable(true))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap!!.setOnMarkerDragListener(this)
        mMap!!.setOnMapLongClickListener(this)

        mMap!!.setOnInfoWindowCloseListener { marker ->
            marker.setIcon(
                BitmapDescriptorFactory.fromResource(
                    R.drawable.ic_map_pin
                )
            )
        }
    }

    /* this function draw polyline using routeDraw api */
    private fun drawPollyLine() {
        /* used for route draw */
        GoogleMapsPath(
            this@MapActivity,
            mMap!!,
            LatLng(22.2904654, 70.7850509),
            LatLng(22.3504888, 70.7848999)
        )
        mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15f))
    }
}