package com.example.sokol.weather_test

import android.app.Application
import com.example.sokol.weather_test.di.component.AppComponent
import com.example.sokol.weather_test.di.component.DaggerAppComponent
import com.example.sokol.weather_test.di.module.DbModule
import com.facebook.FacebookSdk
import com.facebook.LoggingBehavior
import com.facebook.appevents.AppEventsLogger
import io.realm.Realm
import io.realm.RealmConfiguration

class App : Application() {

    companion object {
        lateinit var instance: App
    }
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        initRealm()
        instance = this
        initDagger()
        FacebookSdk.fullyInitialize()//sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
        FacebookSdk.setIsDebugEnabled(true)     //активация журналов отладки
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS)
    }

    @Suppress("DEPRECATION")
    private fun initDagger() {
        appComponent = DaggerAppComponent
                .builder()
                .dbModule(DbModule(this))
                .build()
    }

    private fun  initRealm(){

        Realm.init(this)
        val config = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(config)
    }
}