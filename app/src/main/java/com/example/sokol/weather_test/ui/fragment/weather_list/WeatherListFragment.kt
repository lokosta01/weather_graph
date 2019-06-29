package com.example.sokol.weather_test.ui.fragment.weather_list

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.sokol.weather_test.App
import com.example.sokol.weather_test.R
import com.example.sokol.weather_test.di.component.AppComponent
import com.example.sokol.weather_test.realm.model.WeatherData
import com.example.sokol.weather_test.ui.fragment.adapter.WeatherListAdapter
import com.example.sokol.weather_test.ui.fragment.base.BaseFragment
import com.example.sokol.weather_test.ui.fragment.view.IWeatherListFragment
import com.example.sokol.weather_test.ui.fragment.view_model.IWeatherListFragmentViewModel
import com.example.sokol.weather_test.ui.view_model.ViewModelFactory
import com.example.sokol.weather_test.utils.DataState
import com.facebook.appevents.AppEventsLogger
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject


class WeatherListFragment : BaseFragment(), IWeatherListFragment, WeatherListAdapter.CallbackListener {

    companion object {
        const val LOCATION_REQUEST_CODE = 1001
    }

    private var list: RecyclerView? = null
    private var refresh: SwipeRefreshLayout? = null
    internal var latLng: LatLng? = null
    var logger = AppEventsLogger.newLogger(App.instance.applicationContext)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: IWeatherListFragmentViewModel
    private lateinit var mAdapter: WeatherListAdapter

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    override fun setupActivityComponent(appComponent: AppComponent) {
        appComponent.inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(WeatherListViewModel::class.java)
        viewModel.setView(this)
    }

    override fun getLayout(): Int {
        return R.layout.weather_list_fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            interval = 120 * 1000
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?.let {
                    for (location in locationResult.locations) {
                        if (location != null) {
                            latLng = LatLng(location.latitude, location.longitude)
                            viewModel.setLatLng(location.latitude.toString(), location.longitude.toString(),1)
                        }
                    }
                }
            }
        }
    }

    override fun action(data: WeatherData) {
        logGetTempEvent(data.main?.temp!!.minus(273.15).toInt(), data.name!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list = view.findViewById(R.id.list)
        refresh = view.findViewById(R.id.refresh)
        refresh?.setOnRefreshListener(this)

        mAdapter = WeatherListAdapter(requireContext(), this.view!!, this)
        list?.adapter = mAdapter

        list?.apply {
            postponeEnterTransition()
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        }

        viewModel.getListLiveData().observe(this, Observer {
            when (it.dataState) {
                DataState.LOADING -> {
                    refresh?.isRefreshing = true
                }
                DataState.SUCCESS -> {
                    refresh?.isRefreshing = false

                    it.data?.let {
                        mAdapter.setItems(it.toMutableList())
                    }
                }
                DataState.ERROR -> print(it.message)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.onRefresh()
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(requireActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION),
                    LOCATION_REQUEST_CODE)
        } else {
            startLocationUpdates()
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback, null)
        setLocationListener()
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    fun setLocationListener() {
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location ?: return@addOnSuccessListener
                    latLng = LatLng(location.latitude, location.longitude)
                }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_REQUEST_CODE -> if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                checkLocationPermission()
            } else {
//                toast(R.string.permission_denied)
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    fun logGetTempEvent(temp: Int, city: String){
        val params : Bundle = Bundle().apply {
            putInt("temp", temp)
            putString("city", city)
        }
        logger.logEvent("getTemp", params)
    }


    override fun setRefreshing(refreshing: Boolean) {
        if (refresh != null)
            refresh?.isRefreshing = refreshing
    }

    override fun onRefresh() {
        viewModel.onRefresh()
    }

}
