package com.example.sokol.weather_test.utils

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator

sealed class DataState {
    object LOADING : DataState()
    object SUCCESS : DataState()
    object ERROR : DataState()
}

data class Data<out T> constructor(
        val dataState: DataState,
        val data: T? = null,
        val message: String? = null
)

fun <T> MutableLiveData<Data<T>>.setSuccess(data: T? = null) =
        postValue(Data(DataState.SUCCESS, data))

fun <T> MutableLiveData<Data<T>>.setLoading() =
        postValue(Data(DataState.LOADING, value?.data))

fun <T> MutableLiveData<Data<T>>.setError(message: String? = null) =
        postValue(Data(DataState.ERROR, value?.data, message))

inline fun <reified T : ViewModel> FragmentActivity.getViewModel(viewModelFactory: ViewModelProvider.Factory): T {
    return ViewModelProviders.of(this, viewModelFactory)[T::class.java]
}


fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) {
    liveData.observe(this, Observer(body))
}

fun MutableLiveData<Int?>.update(newValue: Int) {
    if (value != newValue)
        value = newValue
}

fun View.navigate(@IdRes direction: Int) =
        Navigation.findNavController(this).navigate(direction)

fun View.navigate(direction: NavDirections, extras: FragmentNavigator.Extras) =
        Navigation.findNavController(this).navigate(direction, extras)


