package ru.hse.pe

import android.app.Application
import ru.hse.pe.di.AppComponent
import ru.hse.pe.di.DaggerAppComponent

/**
 * Basic application to support DI with application context.
 * Created by Kocharyan Tigran.
 */
class App() : Application() {
    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().context(applicationContext).build()
    }

    fun getAppComponent() = appComponent
}
