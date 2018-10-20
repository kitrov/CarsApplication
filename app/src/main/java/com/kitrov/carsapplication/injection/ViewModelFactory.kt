package com.kitrov.carsapplication.injection

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.kitrov.carsapplication.model.database.AppDatabase
import com.kitrov.carsapplication.ui.car.CarListViewModel

class ViewModelFactory(private val activity: AppCompatActivity): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarListViewModel::class.java)) {
            val db = Room.databaseBuilder(activity.applicationContext, AppDatabase::class.java, "cars").build()
            @Suppress("UNCHECKED_CAST")
            return CarListViewModel(db.carDao()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}