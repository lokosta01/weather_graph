package com.example.sokol.weather_test.ui.fragment.view

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.sokol.weather_test.ui.view.IBaseView

interface IWeatherListFragment : IBaseView, SwipeRefreshLayout.OnRefreshListener {

    //для методов , которые будем использовать во ViewModel
    fun setRefreshing(refreshing: Boolean)
}