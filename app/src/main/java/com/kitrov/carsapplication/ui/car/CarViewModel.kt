package com.kitrov.carsapplication.ui.car

import androidx.lifecycle.MutableLiveData
import com.kitrov.carsapplication.base.BaseViewModel
import com.kitrov.carsapplication.model.entities.CarEntity

class CarViewModel : BaseViewModel() {
    private val plateNumber = MutableLiveData<String>()
    private val latitude = MutableLiveData<Double>()
    private val longitude = MutableLiveData<Double>()
    private val batteryPercentage = MutableLiveData<Int>()
    private val title = MutableLiveData<String>()
    private val photoUrl = MutableLiveData<String>()

    fun bind(carInfo: CarEntity) {
        plateNumber.value = carInfo.plateNumber
        latitude.value = carInfo.latitude
        longitude.value = carInfo.longitude
        batteryPercentage.value = carInfo.batteryPercentage
        title.value = carInfo.title
        photoUrl.value = carInfo.photoUrl
    }

    fun getPlateNumber() = plateNumber

    fun getLatitude() = latitude

    fun getLongitude() = longitude

    fun getBatteryPercentage() = batteryPercentage

    fun getTitle() = title

    fun getPhotoUrl() = photoUrl
}