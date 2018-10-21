package com.kitrov.carsapplication.utils

import com.kitrov.carsapplication.model.CarInfo
import com.kitrov.carsapplication.model.entities.CarEntity

fun map(carInfo: CarInfo, userLatitude: Double?, userLongitude: Double?): CarEntity {
    val carEntity = CarEntity(
        carInfo.plateNumber,
        carInfo.location.latitude,
        carInfo.location.longitude,
        carInfo.model.title,
        carInfo.model.photoUrl,
        carInfo.batteryPercentage
    )
    carEntity.distanceToUser = calculateDistance(carEntity, userLatitude, userLongitude)
    return carEntity
}