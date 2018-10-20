package com.kitrov.carsapplication.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kitrov.carsapplication.model.CarDao
import com.kitrov.carsapplication.model.entities.CarEntity

@Database(entities = [CarEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carDao(): CarDao
}