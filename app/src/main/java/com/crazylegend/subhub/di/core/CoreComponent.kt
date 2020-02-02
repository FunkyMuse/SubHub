package com.crazylegend.subhub.di.core

import android.app.Application
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.subhub.R
import com.crazylegend.subhub.adapters.chooseLanguage.LanguageItem
import com.crazylegend.subhub.di.dbResponse.DbResponseComponentImpl
import com.crazylegend.subhub.di.pickedDirDB.PickedDirComponentImpl
import com.crazylegend.subhub.pickedDirs.PickedDirModel
import com.crazylegend.subhub.utils.ButtonClicked
import com.crazylegend.subhub.utils.SubToast
import com.crazylegend.subhub.utils.interstitialListener
import com.google.gson.Gson
import com.mopub.mobileads.MoPubInterstitial
import com.mopub.mobileads.MoPubView
import io.reactivex.disposables.CompositeDisposable


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
interface CoreComponent {
    val application: Application
    val defaultPrefs: SharedPreferences
    val toaster: SubToast
    val gson: Gson
    val compositeDisposable: CompositeDisposable
    fun setupRecycler(recyclerView: RecyclerView, layoutManager: RecyclerView.LayoutManager, listAdapter: ListAdapter<*, *>, addDivider: Boolean = false)
    val pickedDirComponent: PickedDirComponentImpl
    val dbResponse: DbResponseComponentImpl
    fun showDialogManualSubtitleSearch(fragmentManager: FragmentManager, videoName: String? = null)
    fun showDialogChooseLanguage(fragmentManager: FragmentManager, onLanguageChosen: (langItem: LanguageItem) -> Unit)
    fun showDialogConfirmation(fragmentManager: FragmentManager, bundle: Bundle, positionClicked: (position: ButtonClicked) -> Unit = {})
    val getLanguagePref: LanguageItem?
    fun addLanguageToPrefs(langItem: LanguageItem)
    val getDownloadLocationPref: PickedDirModel?
    fun addDownloadLocationToPrefs(downloadPrefModel: PickedDirModel)
    fun removeLanguagePref()
    fun confirmationArguments(title: String, subtitle: String? = null, leftButtonText: String = application.getString(R.string.cancel), rightButtonText: String = application.getString(R.string.submit)): Bundle
    fun removeDownloadLocationPref()
    fun disposeResources()
    fun destroyBanner(view: MoPubView)
    fun loadAdBanner(adView: MoPubView, unitID: String, onBannerLoadFailed: () -> Unit = {})
    fun initializeMoPub(adUNit: String, loadAd: () -> Unit = {})
    fun loadInterstitialAD(activity: AppCompatActivity, adUnit: String, listener: MoPubInterstitial.InterstitialAdListener = interstitialListener())

}