package com.crazylegend.subhub

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.crazylegend.subhub.di.core.CoreComponentImpl
import io.fabric.sdk.android.Fabric
import io.reactivex.plugins.RxJavaPlugins


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
class AppLevel : Application() {

    private val component by lazy {
        CoreComponentImpl(this)
    }

    override fun onCreate() {
        super.onCreate()

        component.initializeGoogleSDK()

        RxJavaPlugins.setErrorHandler { }

        Crashlytics.Builder()
                .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build()
                .also { crashlyticsKit ->
                    Fabric.with(this, crashlyticsKit)
                }

    }
}