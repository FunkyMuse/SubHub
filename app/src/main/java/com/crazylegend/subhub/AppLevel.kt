package com.crazylegend.subhub

import android.app.Application
import com.crazylegend.subhub.di.core.CoreComponentImpl
import com.google.firebase.crashlytics.FirebaseCrashlytics
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
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
    }
}