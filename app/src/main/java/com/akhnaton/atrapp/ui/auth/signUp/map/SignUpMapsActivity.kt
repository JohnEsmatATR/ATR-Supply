package com.akhnaton.atrapp.ui.auth.signUp.map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.ui.auth.waiting.WaitingActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.akhnaton.atrapp.databinding.ActivitySignUpMapsBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.ui.auth.signUp.pdf.SignUpPdfActivity
import java.io.IOException
import java.util.Locale

class SignUpMapsActivity : BaseActivity(), OnMapReadyCallback {
    lateinit var binding: ActivitySignUpMapsBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var placesClient: PlacesClient
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var firstName = ""
    var lastName = ""
    var email = ""
    var password = ""
    var phone = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
    }

    private fun init() {
        firstName = intent.getStringExtra("firstName")?:""
        lastName = intent.getStringExtra("lastName")?:""
        email = intent.getStringExtra("email")?:""
        password = intent.getStringExtra("password")?:""
        phone = intent.getStringExtra("phone")?:""


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Initialize Places API
        Places.initialize(applicationContext, getString(R.string.google_map_key))
        placesClient = Places.createClient(this)

        // Get the map fragment from the layout
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        showCurrentLocation()


    }

    private fun onClick() {
        binding.btnConfirmLocation.setOnClickListener {
            showMarkerLocation()
        }
        binding.btnMyCurrentLocation.setOnClickListener {
            showCurrentLocation()
        }

    }


    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Enable the user's location
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        googleMap.isMyLocationEnabled = false

        // Set default location (e.g., city center)
        val defaultLocation = CameraUpdateFactory.newLatLngZoom(LatLng(37.7749, -122.4194), 15f)
        googleMap.moveCamera(defaultLocation)
    }

    private fun startPlacePicker() {
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.FULLSCREEN, fields
        ).build(this)

        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    private fun showMarkerLocation() {
        val markerPosition = googleMap.cameraPosition.target
        val intent = Intent(this@SignUpMapsActivity, SignUpPdfActivity::class.java)
        intent.putExtra("firstName", firstName)
        intent.putExtra("lastName", lastName)
        intent.putExtra("email", email)
        intent.putExtra("password", password)
        intent.putExtra("phone", phone)
        intent.putExtra("latitude", markerPosition.latitude.toString())
        intent.putExtra("longitude", markerPosition.longitude.toString())
        intent.putExtra("title", getArea(markerPosition))
        intent.putExtra("address", getAddress(markerPosition))

        startActivity(intent)
    }

    private fun showCurrentLocation() {
        // Check for location permissions before requesting the location
        if (checkLocationPermission()) {
            // Request the last known location
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        val currentLatLng = LatLng(location.latitude, location.longitude)
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))

                    } else {
                        // Handle the case where the location is not available
                        Toast.makeText(this, "Unable to retrieve current location", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    // Handle the failure to get location
                    Toast.makeText(this, "Error getting current location: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Request location permissions if not granted
            requestLocationPermission()
        }
    }

    private fun getAddress(location: LatLng): String {
        val geocoder = Geocoder(this, Locale.getDefault())

        return try {
            val addresses: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)!!

            if (addresses.isNotEmpty()) {
                val address: String = addresses[0].getAddressLine(0)
                // 'address' now contains the human-readable address of the current location
                address
            } else {
                "Address not found"
            }
        } catch (e: IOException) {
            "Error to get your current address"
        }
    }

    private fun getArea(location: LatLng): String {
        val geocoder = Geocoder(this, Locale.getDefault())

        return try {
            val addresses: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)!!

            if (addresses.isNotEmpty()) {
                val address: String = addresses[0].locality ?: "City not found"

                // 'address' now contains the human-readable address of the current location
                address
            } else {
                "City not found"
            }
        } catch (e: IOException) {
            "Error to get your current City"
        }
    }


    // Request location permission
    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    // Check if location permission is granted
    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            AUTOCOMPLETE_REQUEST_CODE -> {
                when (resultCode) {
                    RESULT_OK -> {
                        val place = Autocomplete.getPlaceFromIntent(data!!)
                        val latLng = place.latLng

                        // Move the camera to the selected location
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    }

                    AutocompleteActivity.RESULT_ERROR -> {
                        val status = Autocomplete.getStatusFromIntent(data!!)
                        showToastSnack(status.statusMessage.toString(), true)
                    }

                    RESULT_CANCELED -> {
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, show current location
                    showCurrentLocation()
                } else {
                    // Permission denied, handle accordingly (e.g., show a message)
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }


    companion object {
        private const val AUTOCOMPLETE_REQUEST_CODE = 1
        private const val LOCATION_PERMISSION_REQUEST_CODE = 2
    }
}