package com.kitrov.carsapplication.injection.component

import com.kitrov.carsapplication.injection.module.NetworkModule
import com.kitrov.carsapplication.ui.car.CarListViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetworkModule::class)])
interface ViewModelInjector {

    fun inject(carListViewModel: CarListViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector

        fun networkModule(networkModule: NetworkModule): Builder
    }
}