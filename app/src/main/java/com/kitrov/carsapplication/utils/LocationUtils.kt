package com.kitrov.carsapplication.utils

import android.location.Location
import com.kitrov.carsapplication.model.entities.CarEntity
import java.util.*


fun calculateDistance(carEntity: CarEntity, latitude: Double?, longitude: Double?): Float? {
    if (latitude == null || longitude == null) return null
    val results = FloatArray(1)
    Location.distanceBetween(latitude, longitude, carEntity.latitude, carEntity.longitude, results)
    // distance in meter
    return results[0]
}

fun distanceText(distance: Float?): String {
    if (distance == null) return "N/A"

    return if (distance < 1000)
        if (distance < 1)
            String.format(Locale.ENGLISH, "%dm", 1)
        else
            String.format(Locale.ENGLISH, "%dm", Math.round(distance))
    else if (distance > 10000)
        if (distance < 1000000)
            String.format(Locale.ENGLISH, "%dkm", Math.round(distance / 1000))
        else
            "FAR"
    else
        String.format(Locale.ENGLISH, "%.2fkm", distance / 1000)
}