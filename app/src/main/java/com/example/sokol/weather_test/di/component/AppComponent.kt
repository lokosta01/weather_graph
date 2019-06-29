package com.example.sokol.weather_test.di.component

import com.example.sokol.weather_test.di.module.DbModule
import com.example.sokol.weather_test.di.module.RepositoryModule
import com.example.sokol.weather_test.di.module.ViewModelModule
import com.example.sokol.weather_test.network.ApiModel

import com.example.sokol.weather_test.realm.RealmModule
import com.example.sokol.weather_test.ui.fragment.weather_list.WeatherDaysFragment
import com.example.sokol.weather_test.ui.fragment.weather_list.WeatherListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModel::class, DbModule::class, ViewModelModule::class, RepositoryModule::class,
     RealmModule::class])
interface AppComponent {

    fun inject(fragment: WeatherListFragment)
    fun inject(fragment: WeatherDaysFragment)
}