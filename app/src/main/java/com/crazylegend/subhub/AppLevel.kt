package com.crazylegend.subhub

import android.app.Application
import com.crazylegend.subhub.di.components.AppComponent
import com.crazylegend.subhub.di.components.DaggerAppComponent
import com.crazylegend.subhub.di.providers.AppProvider
import javax.inject.Inject


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
class AppLevel : Application() {

    lateinit var appComponent: AppComponent

    @Inject
    lateinit var appProvider: AppProvider
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this).also { it.inject(this) }
        appProvider.apply {
            applyCrashlytics()
            initializeADS()
            setErrorHandler()
        }
    }
}