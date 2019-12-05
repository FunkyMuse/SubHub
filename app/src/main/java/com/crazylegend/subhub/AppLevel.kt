package com.crazylegend.subhub

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.crazylegend.subhub.consts.CA_ID
import com.google.android.gms.ads.MobileAds
import io.fabric.sdk.android.Fabric
import io.reactivex.plugins.RxJavaPlugins


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
class AppLevel : Application() {

    override fun onCreate() {
        super.onCreate()

        RxJavaPlugins.setErrorHandler { }

        Crashlytics.Builder()
                .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build()
                .also { crashlyticsKit ->
                    Fabric.with(this, crashlyticsKit)
                }

        MobileAds.initialize(this, CA_ID)
    }
}