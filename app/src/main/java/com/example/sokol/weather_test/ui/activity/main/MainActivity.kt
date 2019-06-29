package com.example.sokol.weather_test.ui.activity.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.example.sokol.weather_test.App
import com.example.sokol.weather_test.R
import com.example.sokol.weather_test.di.component.AppComponent
import com.example.sokol.weather_test.ui.activity.base.BaseActivity
import com.example.sokol.weather_test.ui.activity.view.IListActivity
import java.security.MessageDigest
import com.facebook.appevents.AppEventsLogger
import com.facebook.appevents.AppEventsConstants

class MainActivity : BaseActivity(), IListActivity
{
    val TAG = this.javaClass.simpleName
    var logger = AppEventsLogger.newLogger(App.instance.applicationContext)

    override fun setupActivityComponent(appComponent: AppComponent) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActivityComponent(App.instance.appComponent)
        setContentView(R.layout.activity_main)
        logSentFriendRequestEvent()
//        createKeyHash(this, "com.example.sokol.weather_test" )  для ключа
    }

    @SuppressLint("PackageManagerGetSignatures")
    @Suppress("DEPRECATION")
    fun createKeyHash(activity: Activity, yourPackage: String) {
        val info = activity.packageManager.getPackageInfo(yourPackage, PackageManager.GET_SIGNATURES)
        for (signature in info.signatures) {
            val md = MessageDigest.getInstance("SHA")
            md.update(signature.toByteArray())
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
        }
    }

    fun logSentFriendRequestEvent() {
        logger?.logEvent(AppEventsConstants.EVENT_NAME_ACTIVATED_APP)
    }

}
