package com.example.sokol.weather_test.ui.fragment.view_model

import androidx.lifecycle.MutableLiveData
import com.example.sokol.weather_test.realm.model.WeatherData
import com.example.sokol.weather_test.ui.fragment.view.IWeatherListFragment
import com.example.sokol.weather_test.ui.view_model.IDataViewModel
import com.example.sokol.weather_test.ui.view_model.IListViewModel
import com.example.sokol.weather_test.utils.Data

interface IWeatherListFragmentViewModel : IDataViewModel<IWeatherListFragment, Data<List<WeatherData>>>,
        IListViewModel {
    fun loadData(state: Int)
    fun setLatLng(lat: String, lon: String, state: Int)
    fun setState(state: Int)
    fun getListLiveData() : MutableLiveData<Data<List<WeatherData>>>
}