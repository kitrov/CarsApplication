package com.kitrov.carsapplication.ui.car

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.SearchView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.LocationRequest
import com.google.android.material.snackbar.Snackbar
import com.kitrov.carsapplication.R
import com.kitrov.carsapplication.databinding.ActivityCarListBinding
import com.kitrov.carsapplication.injection.ViewModelFactory
import com.patloew.rxlocation.RxLocation
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.Disposable

class CarListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCarListBinding
    private lateinit var viewModel: CarListViewModel
    private val rxPermissions = RxPermissions(this)

    private var errorSnackbar: Snackbar? = null
    private var subscribe: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_list)
        askForPermissions()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_car_list)
        binding.carList.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        viewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(CarListViewModel::class.java)
        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) showError(errorMessage) else hideError()
        })
        binding.viewModel = viewModel
        searchInit()
        locationListener()
    }

    private fun showError(@StringRes errorMessage: Int) {
        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.setAction(R.string.retry, viewModel.errorClickListener)
        errorSnackbar?.show()
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }

    private fun searchInit() {
        val searchView = findViewById<SearchView>(R.id.search_bar)
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener,
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setQuery(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setQuery(newText)
                return false
            }

        })
    }

    private fun askForPermissions() {
        rxPermissions
            .request(
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .subscribe { granted ->
                run {
                    if (!granted) {
                        showError(R.string.ask_permission_error)
                    }
                }
            }.dispose()
    }

    @SuppressLint("CheckResult")
    fun locationListener() {
        val rxLocation = RxLocation(this)
        val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(5000)

        try {
            subscribe = rxLocation.location().updates(locationRequest)
                .flatMap { location -> rxLocation.geocoding().fromLocation(location).toObservable() }
                .subscribe { address -> viewModel.setLocation(address.latitude, address.longitude) }
        } catch (e: SecurityException) {
            showError(R.string.error_no_location_perm)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        subscribe?.dispose()
    }
}
