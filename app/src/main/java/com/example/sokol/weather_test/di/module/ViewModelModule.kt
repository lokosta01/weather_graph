package com.example.sokol.weather_test.di.module

import androidx.lifecycle.ViewModel
import com.example.sokol.weather_test.di.ViewModelKey
import com.example.sokol.weather_test.ui.fragment.weather_list.WeatherListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(WeatherListViewModel::class)
    abstract fun bindWeatherListViewModel(viewModel: WeatherListViewModel) : ViewModel

}