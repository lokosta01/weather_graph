package com.example.sokol.weather_test.ui.view_model

import androidx.lifecycle.LiveData

interface IDataViewModel<V, T> : IViewModel<V> {

    fun getData(): LiveData<T>
}