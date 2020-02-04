package com.crazylegend.subhub

import android.app.Application
import com.adcolony.sdk.AdColony
import com.adcolony.sdk.AdColonyAppOptions
import com.applovin.sdk.AppLovinPrivacySettings
import com.applovin.sdk.AppLovinSdk
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.crazylegend.subhub.consts.AD_COLONY_ID
import com.crazylegend.subhub.consts.AD_COLONY_IDS
import com.crazylegend.subhub.consts.VUNGLE_ID
import com.mopub.common.MoPub
import com.unity3d.ads.metadata.MetaData
import com.vungle.warren.InitCallback
import com.vungle.warren.Vungle
import io.fabric.sdk.android.Fabric
import io.reactivex.plugins.RxJavaPlugins


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
class AppLevel : Application() {

    override fun onCreate() {
        super.onCreate()

        Vungle.init(VUNGLE_ID, this, object : InitCallback {
            override fun onSuccess() {}
            override fun onAutoCacheAdAvailable(placementId: String?) {}
            override fun onError(throwable: Throwable?) {
                throwable?.printStackTrace()
            }
        })
        if (MoPub.canCollectPersonalInformation()) {
            Vungle.updateConsentStatus(Vungle.Consent.OPTED_IN, "1.0.0")
        } else {
            Vungle.updateConsentStatus(Vungle.Consent.OPTED_OUT, "1.0.0")
        }

        val consent = if (MoPub.canCollectPersonalInformation()) {
            "1"
        } else {
            "0"
        }
        val metaData = MetaData(this)
        metaData.set("gdpr.cosent", MoPub.canCollectPersonalInformation())
        metaData.commit()
        val options = with(AdColonyAppOptions()) {
            gdprRequired = true
            gdprConsentString = consent
            this
        }

        AdColony.configure(this, options, AD_COLONY_ID, *AD_COLONY_IDS)
        AppLovinSdk.initializeSdk(this) {
            AppLovinPrivacySettings.setIsAgeRestrictedUser(false, this)
            AppLovinPrivacySettings.setHasUserConsent(MoPub.canCollectPersonalInformation(), this)
        }


        RxJavaPlugins.setErrorHandler { }

        Crashlytics.Builder()
                .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build()
                .also { crashlyticsKit ->
                    Fabric.with(this, crashlyticsKit)
                }

    }
}