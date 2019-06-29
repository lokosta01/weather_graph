package com.example.sokol.weather_test.ui.fragment.weather_list

import androidx.lifecycle.MutableLiveData
import com.example.sokol.weather_test.realm.model.WeatherData
import com.example.sokol.weather_test.repository.IWeatherRepository
import com.example.sokol.weather_test.ui.fragment.view.IWeatherListFragment
import com.example.sokol.weather_test.ui.fragment.view_model.IWeatherListFragmentViewModel
import com.example.sokol.weather_test.ui.view_model.DataViewModel
import com.example.sokol.weather_test.utils.CallbackWrapper
import com.example.sokol.weather_test.utils.Data
import com.example.sokol.weather_test.utils.setSuccess
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject
import io.reactivex.Observer as Observer1

class WeatherListViewModel @Inject constructor(private val repository: IWeatherRepository) :
        DataViewModel<IWeatherListFragment, Data<List<WeatherData>>>(),
        IWeatherListFragmentViewModel {

    private var lat: String = ""
    private var lon: String = ""
    private var state: Int = 1

    override fun setLatLng(lat: String, lon: String, state: Int) {
        this.lat = lat
        this.lon = lon
        loadData(state)
    }

    override fun setState(state: Int) {
        this.state = state
    }

    val liveDataList: MutableLiveData<Data<List<WeatherData>>> = MutableLiveData()
    override fun getListLiveData(): MutableLiveData<Data<List<WeatherData>>> {
        return liveDataList
    }

    override fun loadData(state: Int) {
        if (lat.isEmpty() || lon.isEmpty())
            return

        liveDataList.let {
                repository.getWeather(lat, lon, state)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : CallbackWrapper<List<WeatherData>>(it){
                            override fun onSuccess(t: List<WeatherData>?) {
                                liveDataList?.setSuccess(t)
                            }
                        })
        }
    }

    override fun onCleared() {
        super.onCleared()
        liveData = null
    }

    override fun onRefresh() {
       loadData(state)
    }
}
