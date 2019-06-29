package com.example.sokol.weather_test.ui.fragment.weather_list

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.transition.TransitionInflater
import com.example.sokol.weather_test.R
import com.example.sokol.weather_test.di.component.AppComponent
import com.example.sokol.weather_test.ui.canvas.draw.data.Marker
import com.example.sokol.weather_test.ui.fragment.base.BaseFragment
import com.example.sokol.weather_test.ui.fragment.view_model.IWeatherListFragmentViewModel
import com.example.sokol.weather_test.ui.view_model.ViewModelFactory
import com.example.sokol.weather_test.utils.DataState
import kotlinx.android.synthetic.main.weather_days_fragment.*
import javax.inject.Inject

class WeatherDaysFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var vmWeather: IWeatherListFragmentViewModel
    private var cityAnim: AppCompatTextView? = null
    private var position: Int? = null
    private var itemId: String? = null

    override fun setupActivityComponent(appComponent: AppComponent) {
        appComponent.inject(this)

        vmWeather = ViewModelProviders.of(this, viewModelFactory)
                .get(WeatherListViewModel::class.java)

    }

    override fun getLayout(): Int = R.layout.weather_days_fragment

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        setArguments()

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setArguments() {
        arguments?.let {
            WeatherDaysFragmentArgs.fromBundle(it).let { it1 ->
                vmWeather.setLatLng(it1.lat.toString(), it1.lon.toString(), 2)
                vmWeather.setState(2)
            }
        }
        position = WeatherDaysFragmentArgs.fromBundle(arguments!!).position
        itemId = WeatherDaysFragmentArgs.fromBundle(arguments!!).itemId
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cityAnim = view.findViewById(R.id.city)
        ViewCompat.setTransitionName(cityAnim!!, "secondTransitionName_$position")

        vmWeather.loadData(2)

        vmWeather.getListLiveData().observe(this, Observer {
            when (it.dataState) {

                DataState.LOADING -> {
                    progres.show()
                }

                DataState.SUCCESS -> {
                    progres.hide()
                    it.data.let {
                        graph.setView(it?.map { it1 -> it1.mapToSend() }?.toList() as List<Marker>?)
                    }
                }

                DataState.ERROR -> print(it.message)
            }
        })
        startPostponedEnterTransition()
    }
}