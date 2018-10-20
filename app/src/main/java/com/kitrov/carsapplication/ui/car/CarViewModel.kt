package com.kitrov.carsapplication.ui.car

import androidx.lifecycle.MutableLiveData
import com.kitrov.carsapplication.base.BaseViewModel
import com.kitrov.carsapplication.model.CarInfo
import java.net.URL

class CarViewModel : BaseViewModel() {
    private val plateNumber = MutableLiveData<String>()
    private val latitude = MutableLiveData<Double>()
    private val longitude = MutableLiveData<Double>()
    private val address = MutableLiveData<String>()
    private val batteryPercentage = MutableLiveData<Int>()
    private val batteryEstimatedDistance = MutableLiveData<Float>()
    private val title = MutableLiveData<String>()
    private val photoUrl = MutableLiveData<String>()

    fun bind(carInfo: CarInfo) {
        plateNumber.value = carInfo.plateNumber
        latitude.value = carInfo.location.latitude
        longitude.value = carInfo.location.longitude
        address.value = carInfo.location.address
        batteryPercentage.value = carInfo.batteryPercentage
        batteryEstimatedDistance.value = carInfo.batteryEstimatedDistance
        title.value = carInfo.model.title
        photoUrl.value = carInfo.model.photoUrl
    }

    fun getPlateNumber() = plateNumber

    fun getLatitude() = latitude

    fun getLongitude() = longitude

    fun getAdress() = address

    fun getBatteryPercentage() = batteryPercentage

    fun getBatteryEstimatedDistance() = batteryEstimatedDistance

    fun getTitle() = title

    fun getPhotoUrl() = photoUrl
}