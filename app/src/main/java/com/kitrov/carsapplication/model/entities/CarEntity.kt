package com.kitrov.carsapplication.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cars")
data class CarEntity(
    val plateNumber: String,
    val latitude: Double,
    val longitude: Double,
    val title: String,
    val photoUrl: String,
    val batteryPercentage: Int
) {
    @field:PrimaryKey
    var id: Int? = null
    get() = field
    set(value) {field = value}
}