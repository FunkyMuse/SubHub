package com.crazylegend.subhub

import android.app.Application
import io.reactivex.plugins.RxJavaPlugins


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
class AppLevel : Application() {

    override fun onCreate() {
        super.onCreate()

        RxJavaPlugins.setErrorHandler { }

        /*Crashlytics.Builder()
                .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build()
                .also { crashlyticsKit ->
                    Fabric.with(this, crashlyticsKit)
                }*/
    }
}