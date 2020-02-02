package com.crazylegend.subhub.di.core

import android.app.Application
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.activity.remove
import com.crazylegend.kotlinextensions.context.getCompatColor
import com.crazylegend.kotlinextensions.isNotNullOrEmpty
import com.crazylegend.kotlinextensions.recyclerview.divider
import com.crazylegend.kotlinextensions.rx.clearAndDispose
import com.crazylegend.kotlinextensions.sharedprefs.getObject
import com.crazylegend.kotlinextensions.sharedprefs.putObject
import com.crazylegend.kotlinextensions.sharedprefs.remove
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.subhub.R
import com.crazylegend.subhub.adapters.chooseLanguage.LanguageItem
import com.crazylegend.subhub.consts.*
import com.crazylegend.subhub.di.dbResponse.DbResponseComponentImpl
import com.crazylegend.subhub.di.pickedDirDB.PickedDirComponentImpl
import com.crazylegend.subhub.dialogs.DialogChooseLanguage
import com.crazylegend.subhub.dialogs.DialogConfirmation
import com.crazylegend.subhub.dialogs.DialogManualSubtitleSearch
import com.crazylegend.subhub.listeners.languageDSL
import com.crazylegend.subhub.listeners.onConfirmationCallbackDSL
import com.crazylegend.subhub.pickedDirs.PickedDirModel
import com.crazylegend.subhub.utils.ButtonClicked
import com.crazylegend.subhub.utils.SubToast
import com.google.gson.Gson
import com.mopub.common.MoPub
import com.mopub.common.SdkConfiguration
import com.mopub.common.logging.MoPubLog
import com.mopub.common.privacy.ConsentDialogListener
import com.mopub.mobileads.MoPubErrorCode
import com.mopub.mobileads.MoPubInterstitial
import com.mopub.mobileads.MoPubView
import io.reactivex.disposables.CompositeDisposable


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
class CoreComponentImpl(override val application: Application) : CoreComponent {

    override val defaultPrefs: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(application)
    }

    override fun loadAdBanner(adView: MoPubView, unitID: String, onBannerLoadFailed: () -> Unit) {
        adView.adUnitId = unitID
        adView.loadAd()
        adView.bannerAdListener = object : MoPubView.BannerAdListener {
            override fun onBannerExpanded(banner: MoPubView?) {
            }

            override fun onBannerLoaded(banner: MoPubView?) {
            }

            override fun onBannerCollapsed(banner: MoPubView?) {
            }

            override fun onBannerFailed(banner: MoPubView?, errorCode: MoPubErrorCode?) {
                adView.gone()
                onBannerLoadFailed()
            }

            override fun onBannerClicked(banner: MoPubView?) {
            }
        }
    }

    override fun destroyBanner(view: MoPubView) {
        view.destroy()
        moPubInterstitial?.destroy()
        view.bannerAdListener = null
        moPubInterstitial?.interstitialAdListener = null
    }

    private var moPubInterstitial: MoPubInterstitial? = null

    override fun loadInterstitialAD(activity: AppCompatActivity, adUnit: String, listener: MoPubInterstitial.InterstitialAdListener) {
        with(MoPubInterstitial(activity, adUnit)) {
            moPubInterstitial = this
            interstitialAdListener = listener
            load()
        }
    }

    override fun initializeMoPub(adUNit: String, loadAd: () -> Unit) {
        val sdkConfiguration = SdkConfiguration.Builder(adUNit)
                .withLogLevel(MoPubLog.LogLevel.NONE)
                .withLegitimateInterestAllowed(MoPub.canCollectPersonalInformation())
                .build()
        MoPub.initializeSdk(application, sdkConfiguration) {
            loadAd()
            val manager = MoPub.getPersonalInformationManager()
            val gdprApplies = MoPub.getPersonalInformationManager()?.gdprApplies() ?: false
            if (gdprApplies && !MoPub.canCollectPersonalInformation()) {
                val shouldShow = manager?.shouldShowConsentDialog() ?: false
                if (shouldShow) {
                    manager?.loadConsentDialog(object : ConsentDialogListener {
                        override fun onConsentDialogLoaded() {
                            manager.showConsentDialog()
                        }

                        override fun onConsentDialogLoadFailed(moPubErrorCode: MoPubErrorCode) {
                        }
                    })
                }
            }
        }
    }

    override fun addDownloadLocationToPrefs(downloadPrefModel: PickedDirModel) = defaultPrefs.putObject(DL_LOCATION_PREF_KEY, downloadPrefModel)
    override val getDownloadLocationPref get() = defaultPrefs.getObject<PickedDirModel>(DL_LOCATION_PREF_KEY)

    override fun addLanguageToPrefs(langItem: LanguageItem) = defaultPrefs.putObject(LANGUAGE_PREF_KEY, langItem)
    override val getLanguagePref get() = defaultPrefs.getObject<LanguageItem>(LANGUAGE_PREF_KEY)

    override fun removeDownloadLocationPref() = defaultPrefs.remove(DL_LOCATION_PREF_KEY)

    override fun removeLanguagePref() = defaultPrefs.remove(LANGUAGE_PREF_KEY)

    override val toaster by lazy { SubToast(application) }

    override val gson by lazy {
        Gson()
    }

    override val compositeDisposable by lazy {
        CompositeDisposable()
    }

    override val pickedDirComponent by lazy {
        PickedDirComponentImpl(application)
    }

    override fun setupRecycler(recyclerView: RecyclerView, layoutManager: RecyclerView.LayoutManager, listAdapter: ListAdapter<*, *>, addDivider: Boolean) {
        recyclerView.setHasFixedSize(false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = listAdapter
        if (addDivider) {
            recyclerView.divider(recyclerView.context.getCompatColor(R.color.helperColor))
        }
    }

    override val dbResponse by lazy { DbResponseComponentImpl() }

    override fun disposeResources() {
        compositeDisposable.clearAndDispose()
    }

    override fun showDialogManualSubtitleSearch(fragmentManager: FragmentManager, videoName: String?) {
        removeFragmentByTag(fragmentManager, DIALOG_MANUAL_SEARCH_TAG)
        val dialogManualSubtitleSearch = DialogManualSubtitleSearch()
        if (videoName.isNotNullOrEmpty()) {
            dialogManualSubtitleSearch.addArguments(videoName)
        }
        dialogManualSubtitleSearch.show(fragmentManager, DIALOG_MANUAL_SEARCH_TAG)
    }

    override fun showDialogChooseLanguage(fragmentManager: FragmentManager, onLanguageChosen: (langItem: LanguageItem) -> Unit) {
        removeFragmentByTag(fragmentManager, DIALOG_CHOOSE_LANGUAGE_TAG)
        val dialogChooseLanguage = DialogChooseLanguage()
        dialogChooseLanguage.show(fragmentManager, DIALOG_CHOOSE_LANGUAGE_TAG)
        dialogChooseLanguage.onLanguageChosen = languageDSL {
            onLanguageChosen.invoke(it)
        }
    }

    private fun removeFragmentByTag(fragmentManager: FragmentManager, tag: String) {
        fragmentManager.findFragmentByTag(tag)?.remove()
    }

    override fun confirmationArguments(title: String, subtitle: String?, leftButtonText: String, rightButtonText: String): Bundle = bundleOf(
            Pair(TITLE_ARGUMENT_TAG, title),
            Pair(SUBTITLE_ARGUMENT_TAG, subtitle),
            Pair(LEFT_BUTTON_TEXT_ARGUMENT_TAG, leftButtonText),
            Pair(RIGHT_BUTTON_TEXT_ARGUMENT_TAG, rightButtonText)
    )

    override fun showDialogConfirmation(fragmentManager: FragmentManager, bundle: Bundle, positionClicked: (position: ButtonClicked) -> Unit) {
        removeFragmentByTag(fragmentManager, DIALOG_CONFIRMATION_TAG)
        val confirmation = DialogConfirmation()
        confirmation.arguments = bundle
        confirmation.show(fragmentManager, DIALOG_CONFIRMATION_TAG)
        confirmation.onConfirmationCallback = onConfirmationCallbackDSL {
            positionClicked.invoke(it)
        }
    }
}