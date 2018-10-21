package com.kitrov.carsapplication.utils

import org.junit.Test

import org.junit.Assert.*

class LocationUtilsKtTest {

    @Test
    fun `distance is show properly when null`() {
        val text = distanceText(null)
        assertTrue("N/A".contains(text))
    }

    @Test
    fun `distance is show properly when less than 1`() {
        val text = distanceText(0.1f)
        assertTrue("0.1m".contains(text))
    }

    @Test
    fun `distance is show properly when less than 1000 and more than 1`() {
        val text = distanceText(50f)
        assertTrue("50m".contains(text))
    }

    @Test
    fun `distance is show properly when more than 1000 and less than 10000`() {
        val text = distanceText(5000f)
        assertTrue("5.00km".contains(text))
    }

    @Test
    fun `distance is show properly when more than 10000 and less than 1000000`() {
        val text = distanceText(50000f)
        assertTrue("50km".contains(text))
    }

    @Test
    fun `distance is show properly when more than 1000000`() {
        val text = distanceText(5000000f)
        assertTrue("FAR".contains(text))
    }
}