package com.kitrov.carsapplication.base

import androidx.lifecycle.ViewModel
import com.kitrov.carsapplication.injection.component.DaggerViewModelInjector
import com.kitrov.carsapplication.injection.component.ViewModelInjector
import com.kitrov.carsapplication.injection.module.NetworkModule
import com.kitrov.carsapplication.ui.car.CarListViewModel

abstract class BaseViewModel:ViewModel() {
    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .networkModule(NetworkModule)
        .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is CarListViewModel -> injector.inject(this)
        }
    }
}