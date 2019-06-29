package com.example.sokol.weather_test.network

import com.example.sokol.weather_test.BuildConfig
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.realm.RealmObject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ApiModel {

    companion object {
        const val TIMEOUT_SEC = 30L
        const val BASE_URL = "http://api.openweathermap.org/"
        const val API_KEY = "9f5eb837bd4d6da0878535f13c201f7e"
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .excludeFieldsWithoutExposeAnnotation()
                .setExclusionStrategies(object : ExclusionStrategy {
                    override fun shouldSkipClass(f: Class<*>?): Boolean {
                        return false
                    }

                    override fun shouldSkipField(f: FieldAttributes?): Boolean {
                        return f?.declaredClass == RealmObject::class.java
                    }
                })
                .create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(logging)
        }

        builder.connectTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
                .addInterceptor { chain ->

                    val newUrl = chain.request().url()
                            .newBuilder()
                            .addQueryParameter("appid", API_KEY)
                            .build()

                    val newRequest = chain.request()
                            .newBuilder()
                            .url(newUrl)
                            .build()

                    chain.proceed(newRequest)
                }
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRestAdapter(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Singleton
    fun provideApiService(restAdapter: Retrofit): WeatherApiService {
        return restAdapter.create(WeatherApiService::class.java)
    }
}