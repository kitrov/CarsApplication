package com.kitrov.carsapplication.ui.car

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.kitrov.carsapplication.R
import com.kitrov.carsapplication.base.BaseViewModel
import com.kitrov.carsapplication.model.CarInfo
import com.kitrov.carsapplication.network.CarApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CarListViewModel: BaseViewModel() {
    @Inject
    lateinit var carApi: CarApi

    private lateinit var subscription: Disposable

    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadAvailableCars() }
    val carListAdapter = CarListAdapter()

    init{
        loadAvailableCars()
    }

    private fun loadAvailableCars(){
        subscription = carApi.availableCars()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrievePostListStart() }
            .doOnTerminate { onRetrievePostListFinish() }
            .subscribe(
                { result -> onRetrievePostListSuccess(result) },
                { onRetrievePostListError() }
            )
    }

    private fun onRetrievePostListStart(){
        loadingVisibility.value = View.VISIBLE
        errorMessage.value = null
    }

    private fun onRetrievePostListFinish(){
        loadingVisibility.value = View.GONE
    }

    private fun onRetrievePostListSuccess(carList:List<CarInfo>){
        carListAdapter.updateCarList(carList)
    }

    private fun onRetrievePostListError(){
        errorMessage.value = R.string.car_error
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }
}