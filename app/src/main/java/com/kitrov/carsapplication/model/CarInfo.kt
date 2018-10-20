package com.kitrov.carsapplication.model

data class CarInfo(
    val id: Int,
    val plateNumber: String,
    val location: LocationInfo,
    val model: ModelInfo,
    val batteryPercentage: Int,
    val batteryEstimatedDistance: Float,
    val isCharging: Boolean
)