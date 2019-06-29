package com.example.sokol.weather_test.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

abstract  class DataViewModel<V, M> : IDataViewModel<V, M>, BaseViewModel<V>() {
    internal  var liveData: MutableLiveData<M>? = null

    override fun getData(): LiveData<M> {
     if (liveData == null) liveData = MutableLiveData()


        return liveData as MutableLiveData<M>
    }

    private fun loadData() {}

    protected override fun onCleared() {
        super.onCleared()
    }
}