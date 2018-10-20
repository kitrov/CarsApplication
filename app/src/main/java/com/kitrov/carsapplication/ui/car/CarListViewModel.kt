package com.kitrov.carsapplication.ui.car

import android.view.View
import android.widget.SearchView
import androidx.lifecycle.MutableLiveData
import com.kitrov.carsapplication.R
import com.kitrov.carsapplication.base.BaseViewModel
import com.kitrov.carsapplication.model.CarDao
import com.kitrov.carsapplication.model.entities.CarEntity
import com.kitrov.carsapplication.network.CarApi
import com.kitrov.carsapplication.utils.map
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CarListViewModel(private val carDao: CarDao) : BaseViewModel(){
    @Inject
    lateinit var carApi: CarApi

    private lateinit var subscription: Disposable

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadAvailableCars(null) }
    val carListAdapter = CarListAdapter()

    init {
        loadAvailableCars(null)
    }

    private fun filterList(carEntity: CarEntity, query: String?): Boolean {
        if (query == null) {
            return true
        }
        return carEntity.plateNumber.contains(query, true) ||
                carEntity.batteryPercentage.toString().contains(query, true) ||
                carEntity.title.contains(query, true)
    }

    fun loadAvailableCars(query: String?) {
        subscription = Observable.fromCallable { carDao.all }.concatMap { dbCarList ->
            if (dbCarList.isEmpty()) {
                carApi.availableCars().concatMap { apiCarList ->
                    carDao.insertAll(*apiCarList.map { map(it) }.toTypedArray())
                    Observable.just(
                        apiCarList.map { map(it) }
                            .filter { filterList(it, query) })
                }
            } else {
                Observable.just(dbCarList.filter { filterList(it, query) })
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