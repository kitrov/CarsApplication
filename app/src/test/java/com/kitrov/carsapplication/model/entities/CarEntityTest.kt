package com.kitrov.carsapplication.model.entities

import com.kitrov.carsapplication.*
import org.junit.Assert.assertTrue
import org.junit.Test

class CarEntityTest {

    @Test
    fun `displayTitle shows correct value`() {
        val carEntity = CarEntity(PLATE_NUMBER, LATITUDE, LONGITUDE, TITLE, PHOTO_URL, BATTERY_PERCENTAGE)
        assertTrue(carEntity.displayTitle().contentEquals("$TITLE-$PLATE_NUMBER"))
    }
}