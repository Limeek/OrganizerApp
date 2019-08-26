package ru.limeek.organizer.views

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import ru.limeek.organizer.R
import ru.limeek.organizer.data.model.location.Location
import ru.limeek.organizer.viewmodels.PlacePickerViewModel

class PlacePickerActivity: AppCompatActivity() {

    private val DEFAULT_ZOOM = 15f

    lateinit var mapFragment: SupportMapFragment
    private lateinit var viewModel: PlacePickerViewModel
    lateinit var googleMap: GoogleMap
    lateinit var currentLocation: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placepicker)
        viewModel = ViewModelProviders.of(this).get(PlacePickerViewModel::class.java)

        mapFragment = SupportMapFragment()
        supportFragmentManager.beginTransaction().replace(R.id.mapFragContainer, mapFragment).commit()

        mapFragment.getMapAsync {
            googleMap = it
            getCurrentLocation()
        }

    }

    private fun getCurrentLocation(){
        val client = FusedLocationProviderClient(this)
        if(PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED &&
                PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED)
            client.lastLocation.addOnCompleteListener {
                if(it.isComplete){
                    val result = it.result
                    if(result != null) {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(result.latitude, result.longitude), DEFAULT_ZOOM))
                    }
                    else{
                        Toast.makeText(this, "ERROR LOCATING", Toast.LENGTH_LONG).show()
                    }
                }
            }
    }

    override fun onPause() {
        super.onPause()
    }
}