package com.example.sokol.weather_test.ui.view_model

import androidx.lifecycle.ViewModel

abstract class BaseViewModel<V> : ViewModel(), IViewModel<V> {

    internal var view: V? = null

    private var isCleared: Boolean = false

    override fun setView(view: V) {
        this.view = view
        isCleared = false
    }

    override fun onCleared() {
        isCleared = true
    }
}
