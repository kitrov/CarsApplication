package com.kitrov.carsapplication.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kitrov.carsapplication.model.entities.CarEntity

@Dao
interface CarDao{
    @get:Query("select * from cars")
    val all: List<CarEntity>

    @Insert
    fun insertAll(vararg cars: CarEntity)
}