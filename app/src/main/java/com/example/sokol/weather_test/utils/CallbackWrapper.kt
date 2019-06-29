package com.example.sokol.weather_test.utils

import androidx.lifecycle.MutableLiveData
import io.reactivex.observers.DisposableObserver
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.lang.ref.WeakReference

abstract class CallbackWrapper<T>(liveData: MutableLiveData<Data<T>>) : DisposableObserver<T>() {


    private val weakReference: WeakReference<MutableLiveData<Data<T>>> = WeakReference(liveData)

    protected abstract fun onSuccess(t: T?)

    override fun onNext(t: T) {
        onSuccess(t)
    }

    override fun onError(e: Throwable) {
        val data = weakReference.get()
        when (e) {
            is HttpException -> {

                val responseBody = e.response().errorBody()
                data?.postValue(Data(DataState.ERROR, message = responseBody?.let { getErrorMessage(it) }))

            }
            is IOException ->
                data?.postValue(Data(DataState.ERROR, message = "Ошибка соединения"))

            else -> data?.postValue(Data(DataState.ERROR, message = e.message))
        }
    }

    override fun onComplete() {}

    private fun getErrorMessage(responseBody: ResponseBody): String {
        return try {
            val jsonObject = JSONObject(responseBody.string())
            jsonObject.getString("message")
        } catch (e: Exception) {
            e.message.toString()
        }
    }
}