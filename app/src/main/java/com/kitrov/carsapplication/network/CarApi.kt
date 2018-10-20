package com.kitrov.carsapplication.network

import com.kitrov.carsapplication.model.CarInfo
import io.reactivex.Observable
import retrofit2.http.GET

interface CarApi{

    @GET("/api/mobile/public/availablecars")
    fun availableCars(): Observable<List<CarInfo>>
}