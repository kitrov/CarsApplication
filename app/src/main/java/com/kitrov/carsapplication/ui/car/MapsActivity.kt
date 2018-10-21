package com.kitrov.carsapplication.ui.car

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.kitrov.carsapplication.R
import com.kitrov.carsapplication.injection.ViewModelFactory
import com.kitrov.carsapplication.utils.ZOOM_CAMERA
import com.patloew.rxlocation.RxLocation
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.Disposable

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var viewModel: CarListViewModel
    private lateinit var googleMap: GoogleMap
    private lateinit var rxLocation: RxLocation
    private val rxPermissions = RxPermissions(this)
    private val permissionClickListener = View.OnClickListener { askForPermissions() }
    private var permissionsSubscribe: Disposable? = null
    private var locationSubscribe: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        askForPermissions()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        viewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(CarListViewModel::class.java)
        viewModel.setMapsActivity(this)

        rxLocation = RxLocation(this)
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        moveCameraToCurrentLocation()

//        // Add a marker in Sydney and move the camera
//        this.googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
    }

    fun addMarker(latLng: LatLng, title: String) {
        googleMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(title)
                .flat(true)
        )
    }

    private fun askForPermissions() {
        permissionsSubscribe = rxPermissions
            .request(
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .subscribe { granted ->
                if (!granted) {
                    Snackbar.make(
                        findViewById(R.id.map),
                        R.string.ask_permission_error,
                        Snackbar.LENGTH_INDEFINITE
                    )
                        .setAction(R.string.retry, permissionClickListener)
                        .show()
                }
            }
    }

    @SuppressLint("MissingPermission")
    fun moveCameraToCurrentLocation() {
        locationSubscribe?.dispose()
        locationSubscribe = rxLocation.location().lastLocation()
            .toObservable().subscribe {
                googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), ZOOM_CAMERA)

                )
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        permissionsSubscribe?.dispose()
        locationSubscribe?.dispose()
    }
}
