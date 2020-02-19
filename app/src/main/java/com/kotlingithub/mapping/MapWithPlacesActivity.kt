package com.kotlingithub.mapping

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity

import com.codemybrainsout.placesearch.PlaceSearchDialog
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.PlacePhotoMetadata
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer
import com.google.android.gms.location.places.PlacePhotoMetadataResponse
import com.google.android.gms.location.places.PlacePhotoResponse
import com.google.android.gms.location.places.Places
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.kotlingithub.R

class MapWithPlacesActivity : FragmentActivity(), OnMapReadyCallback {

    //Our Map
    private var mMap: GoogleMap? = null

    //Google ApiClient
    private var googleApiClient: GoogleApiClient? = null

    private var mGeoDataClient: GeoDataClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_places)
        init()
    }

    private fun init() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        //Initializing google api client
        googleApiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .build()

        // code is for places
        val autocompleteFragment =
            fragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as PlaceAutocompleteFragment
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                mMap!!.clear()
                mMap!!.addMarker(MarkerOptions().position(place.latLng).title(place.name.toString()))
                mMap!!.moveCamera(CameraUpdateFactory.newLatLng(place.latLng))
                mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(place.latLng, 12.0f))
                Log.d("mPhoto", "onPlaceSelected: place id : " + place.id)
                getPhotos(place.id)
            }

            override fun onError(status: Status) {

            }
        })
    }

    override fun onStart() {
        googleApiClient!!.connect()
        super.onStart()
    }

    override fun onStop() {
        googleApiClient!!.disconnect()
        super.onStop()

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val latLng = LatLng(-34.0, 151.0)
        mMap!!.addMarker(MarkerOptions().position(latLng).draggable(true))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    /* this function show dialog of search location call this function when you want to search location - library used */
    private fun googlePlacesDialog() {
        val placeSearchDialog = PlaceSearchDialog.Builder(this)
            .setHintText("Enter location name")
            .setHintTextColor(R.color.mt_gray6)
            .setNegativeText("CANCEL")
            .setNegativeTextColor(R.color.colorPrimary)
            .setPositiveText("OK")
            .setPositiveTextColor(R.color.colorAccent)
            .setLocationNameListener { locationName ->
                //set textView or editText
                Toast.makeText(this@MapWithPlacesActivity, "" + locationName, Toast.LENGTH_SHORT)
                    .show()
            }
            .build()
        placeSearchDialog.show()
    }


    // Request photos and metadata for the specified place.
    private fun getPhotos(placeId: String) {
        //        final String placeId = "ChIJa147K9HX3IAR-lwiGIQv9i4";
        //        final String placeId = "ChIJrTLr-GyuEmsRBfy61i59si0";
        mGeoDataClient = Places.getGeoDataClient(this, null)
        val photoMetadataResponse = mGeoDataClient!!.getPlacePhotos(placeId)
        photoMetadataResponse.addOnCompleteListener { task ->
            // Get the list of photos.
            val photos = task.result
            // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
            val photoMetadataBuffer = photos!!.photoMetadata
            // Get the first photo in the list.
            if (photoMetadataBuffer != null) {
                val photoMetadata = photoMetadataBuffer.get(0)

                // Get the attribution text.
                val attribution = photoMetadata.attributions
                // Get a full-size bitmap for the photo.
                val photoResponse = mGeoDataClient!!.getPhoto(photoMetadata)
                photoResponse.addOnCompleteListener { task1 ->
                    val photo = task1.result
                    val bitmap = photo!!.bitmap
                    val ivImages = findViewById<ImageView>(R.id.ivImage)
                    if (bitmap != null) {
                        ivImages.setImageBitmap(bitmap)
                        Log.d("mPhoto", "onComplete: bitmap : $bitmap")
                    }
                }
            }
        }
    }
}

// Nearby places api use -
// dependency ~ implementation 'com.google.android.gms:play-services-places:15.0.0'
// add PlaceAutocompleteFragment code - above given in init().
// you can find photo from place id, for that getPhoto() available and photoId get from place.