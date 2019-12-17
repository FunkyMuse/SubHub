package com.crazylegend.subhub.di.core

import android.app.Application
import android.content.SharedPreferences
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.activity.remove
import com.crazylegend.kotlinextensions.context.getCompatColor
import com.crazylegend.kotlinextensions.isNotNullOrEmpty
import com.crazylegend.kotlinextensions.recyclerview.divider
import com.crazylegend.kotlinextensions.sharedprefs.getObject
import com.crazylegend.kotlinextensions.sharedprefs.putObject
import com.crazylegend.kotlinextensions.sharedprefs.remove
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
import com.mopub.mobileads.MoPubView
import io.reactivex.disposables.CompositeDisposable


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
class CoreComponentImpl(override val application: Application) : CoreComponent {

    override val defaultPrefs: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(application)
    }

    override fun loadBanner(view: MoPubView, adUNit: String) {
        view.adUnitId = adUNit
        view.loadAd()
    }

    override fun destroyBanner(view: MoPubView) {
        view.destroy()
    }


    override fun initializeMoPub(adUNit: String, loadAd: () -> Unit) {
        val sdkConfiguration = SdkConfiguration.Builder(adUNit)
                .withLogLevel(MoPubLog.LogLevel.DEBUG)
                .withLegitimateInterestAllowed(false)
                .build()
        MoPub.initializeSdk(application, sdkConfiguration) {
            loadAd()
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
        compositeDisposable.clear()
        compositeDisposable.dispose()
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