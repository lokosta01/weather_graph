package com.example.sokol.weather_test.realm

import android.os.HandlerThread
import com.example.sokol.weather_test.realm.model.WeatherData
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import io.reactivex.Single.*
import io.reactivex.android.schedulers.AndroidSchedulers

import java.util.concurrent.Executors

class RealmDao {

    init {
        initRealm()
    }

    lateinit var realm: Realm
    private lateinit var writeScheduler: Scheduler

    private lateinit var looperScheduler: Scheduler

    fun initRealm() {
        // read scheduler
        val handlerThread = HandlerThread("LOOPER_SCHEDULER")
        handlerThread.start()
        synchronized(handlerThread) {
            looperScheduler = AndroidSchedulers.from(handlerThread.looper)
        }
        // write scheduler
        writeScheduler = Schedulers.from(Executors.newSingleThreadExecutor())
    }

    fun getWeather(): Observable<List<WeatherData>> {
        return Observable.create(ObservableOnSubscribe<List<WeatherData>> { emitter ->
            realm = Realm.getDefaultInstance()
            val dbTasks = realm.where(WeatherData::class.java)?.findAllAsync()
            val realmChangeListener = RealmChangeListener<RealmResults<WeatherData>> { results ->
                if (results.isLoaded && !emitter.isDisposed) {
                    val tasks = results.mapNotNull { it }
                    if (!emitter.isDisposed) {
                        emitter.onNext(tasks)
                    }
                }
            }

            emitter.setDisposable(Disposables.fromAction {
                if (dbTasks!!.isValid) {
                    dbTasks.removeChangeListener(realmChangeListener)

                }
                realm.close()
            })

            dbTasks?.addChangeListener(realmChangeListener)
        }).subscribeOn(looperScheduler)
                .unsubscribeOn(looperScheduler)
    }


    fun saveWeather(weather: List<WeatherData>) {
        (create<WeatherData> {
            Realm.getDefaultInstance().use {
                it.executeTransaction { realm -> realm.insertOrUpdate(weather.map { it }) }
            }
        }).subscribeOn(writeScheduler).subscribe()
    }

}
