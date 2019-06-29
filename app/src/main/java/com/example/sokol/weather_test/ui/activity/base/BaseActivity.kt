package com.example.sokol.weather_test.ui.activity.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sokol.weather_test.App
import com.example.sokol.weather_test.di.component.AppComponent

abstract class BaseActivity : AppCompatActivity() {

    protected abstract fun setupActivityComponent(appComponent: AppComponent)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActivityComponent(App.instance.appComponent)
    }
}