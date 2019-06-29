package com.example.sokol.weather_test.ui.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.example.sokol.weather_test.App
import com.example.sokol.weather_test.di.component.AppComponent

abstract class BaseFragment : Fragment() {

    protected abstract fun setupActivityComponent(appComponent: AppComponent)

    @LayoutRes
    protected abstract fun getLayout(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActivityComponent(App.instance.appComponent)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayout(), container, false)
    }
}