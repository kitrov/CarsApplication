package com.kitrov.carsapplication.ui.car

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.kitrov.carsapplication.R
import com.kitrov.carsapplication.base.BaseViewModel
import com.kitrov.carsapplication.model.CarDao
import com.kitrov.carsapplication.model.entities.CarEntity
import com.kitrov.carsapplication.network.CarApi
import com.kitrov.carsapplication.utils.calculateDistance
import com.kitrov.carsapplication.utils.map
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CarListViewModel(private val carDao: CarDao) : BaseViewModel() {
    @Inject
    lateinit var carApi: CarApi

    private lateinit var subscription: Disposable

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadAvailableCars() }
    val carListAdapter = CarListAdapter()

    private var query: MutableLiveData<String> = MutableLiveData()
    private var longitude: MutableLiveData<Double> = MutableLiveData()
    private var latitude: MutableLiveData<Double> = MutableLiveData()
    private var mapsActivity: MapsActivity? = null

    fun setQuery(query: String?) {
        this.query.value = query
        reloadAvailableCars()
    }

    fun setLocation(latitude: Double, longitude: Double) {
        if (this.latitude.value != latitude || this.longitude.value != longitude) {
            this.latitude.value = latitude
            this.longitude.value = longitude
            reloadAvailableCars()
        }
    }

    fun setMapsActivity(mapsActivity: MapsActivity) {
        this.mapsActivity = mapsActivity
        reloadAvailableCars()
    }

    init {
        loadAvailableCars()
    }

    private fun filterList(carEntity: CarEntity): Boolean {
        val query = this.query.value ?: return true
        return carEntity.plateNumber.contains(query, true) ||
                carEntity.batteryPercentage.toString().contains(query, true) ||
                carEntity.title.contains(query, true)
    }

    private fun reloadAvailableCars() {
        subscription.dispose()
        loadAvailableCars()
    }

    private fun loadAvailableCars() {
        subscription = Observable.fromCallable { carDao.all }.concatMap { dbCarList ->
            if (dbCarList.isEmpty()) {
                carApi.availableCars().concatMap { apiCarList ->
                    carDao.insertAll(*apiCarList.map { map(it, latitude.value, longitude.value) }.toTypedArray())
                    Observable.just(
                        apiCarList
                            .asSequence()
                            .map { map(it, latitude.value, longitude.value) }
                            .filter { filterList(it) }
                            .sortedBy { it.distanceToUser }
                            .onEach { mapsActivity?.addMarker(LatLng(it.latitude, it.longitude), it.displayTitle()) }
                            .toList()
                    )
                }
            } else {
                Observable.just(dbCarList
                    .asSequence()
                    .onEach { it.distanceToUser = calculateDistance(it, latitude.value, longitude.value) }
                    .filter { filterList(it) }
                    .sortedBy { it.distanceToUser }
                    .onEach {
                        mapsActivity?.runOnUiThread {
                            mapsActivity?.addMarker(
                                LatLng(it.latitude, it.longitude),
                                it.displayTitle()
                            )
                        }
                    }
                    .toList())
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrievePostListStart() }
            .doOnTerminate { onRetrievePostListFinish() }
            .subscribe(
                { result -> onRetrievePostListSuccess(result) },
                { onRetrievePostListError() }
            )
    }

    private fun onRetrievePostListStart() {
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
    }

    private fun onRetrievePostListFinish() {
        loadingVisibility.value = View.GONE
    }

    private fun onRetrievePostListSuccess(carList: List<CarEntity>) {
        carListAdapter.updateCarList(carList)
    }

    private fun onRetrievePostListError() {
        errorMessage.value = R.string.car_error
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

}