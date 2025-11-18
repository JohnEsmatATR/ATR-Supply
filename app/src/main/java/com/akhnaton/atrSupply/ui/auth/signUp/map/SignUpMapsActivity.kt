package com.akhnaton.atrSupply.ui.auth.signUp.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.akhnaton.atrSupply.R
import com.akhnaton.atrSupply.databinding.ActivitySignUpMapsBinding
import com.akhnaton.atrSupply.shared.BaseActivity
import com.akhnaton.atrSupply.ui.auth.signUp.pdf.SignUpPdfActivity
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        init()
        onClick()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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


        googleMap.uiSettings.isZoomGesturesEnabled = true
        googleMap.uiSettings.isScrollGesturesEnabled = true
        googleMap.uiSettings.isTiltGesturesEnabled = true
        googleMap.uiSettings.isRotateGesturesEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled = true


        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
        }
    }

    private fun startPlacePicker() {
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.FULLSCREEN, fields
        ).build(this)

        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    private fun showMarkerLocation() {
        val mapAddress = binding.spWriteCustomerLocation.text.toString()
        if (mapAddress.isEmpty()){
            showToastSnack("برجاء ادخال العنوان", true)
        }
        else{
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
            intent.putExtra("mapLocation",mapAddress)
            startActivity(intent)

        }

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showCurrentLocation() {

        if (!::googleMap.isInitialized) {
           // Toast.makeText(this, "Google Map not initialized", Toast.LENGTH_SHORT).show()
            return
        }


        if (checkLocationPermission()) {

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        val currentLatLng = LatLng(location.latitude, location.longitude)
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))



                        getAddressFromLatLng(
                            this@SignUpMapsActivity,
                            location.latitude,
                            location.longitude
                        ) { address ->
                            lifecycleScope.launch(Dispatchers.Main) {
                                binding.spWriteCustomerLocation.setText(address ?: "No address found")
                            }

                        }

                    } else {

                        Toast.makeText(this, "Unable to retrieve current location", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->

                    Toast.makeText(this, "Error getting current location: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {

            requestLocationPermission()
        }
    }


    private fun getAddress(location: LatLng): String {
        val geocoder = Geocoder(this, Locale.getDefault())

        return try {
            val addresses: List<Address> = geocoder
                .getFromLocation(location.latitude, location.longitude, 1)!!

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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun getAddressFromLatLng(context: Context, latitude: Double, longitude: Double, onResult: (String?) -> Unit) {
        val geocoder = Geocoder(context, Locale("ar"))

        geocoder.getFromLocation(
            latitude,
            longitude,
            1,
            object : Geocoder.GeocodeListener {
                override fun onGeocode(addresses: MutableList<Address>) {
                    if (addresses.isNotEmpty()) {
                        val address = addresses[0]
                        val fullAddress = (0..address.maxAddressLineIndex).joinToString(", ") { index ->
                            address.getAddressLine(index)
                        }
                        onResult(fullAddress)
                    } else {
                        onResult(null)
                    }
                }

                override fun onError(errorMessage: String?) {
                    onResult(null)
                }
            }
        )
    }

}