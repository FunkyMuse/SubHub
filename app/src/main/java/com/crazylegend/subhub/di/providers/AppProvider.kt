package com.crazylegend.subhub.di.providers

import android.app.Application
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.recyclerview.getLastVisibleItemPosition
import com.crazylegend.kotlinextensions.recyclerview.smoothScrollTo
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.subhub.BuildConfig
import com.crazylegend.subhub.consts.SCROLL_TO_TOP_VISIBILITY_THRESHOLD
import com.google.android.gms.ads.MobileAds
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by crazy on 5/2/20 to long live and prosper !
 */

@Singleton
class AppProvider @Inject constructor(
        private val application: Application
) {

    val gson by lazy {
        Gson()
    }

    fun applyCrashlytics() {
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
    }

    fun initializeADS() {
        MobileAds.initialize(application)
    }

    var isLoading = false
    inline fun recyclerScrollBackToTop(backToTop: FloatingActionButton?, recycler: RecyclerView?, adapter: RecyclerView.Adapter<*>,
                                       crossinline loadMore: () -> Unit = {}) {
        recycler?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                adapter.apply {
                    val lastPos = recyclerView.getLastVisibleItemPosition()

                    lastPos?.apply {
                        if (this >= adapter.itemCount - 1 && !isLoading) {
                            loadMore()
                        }
                        if (this > SCROLL_TO_TOP_VISIBILITY_THRESHOLD) {
                            backToTop?.visible()
                        } else {
                            backToTop?.gone()
                        }
                    }
                }
            }
        })
        backToTop?.setOnClickListener {
            recycler?.smoothScrollTo(0)
        }
    }

    fun setErrorHandler() {
        RxJavaPlugins.setErrorHandler { }
    }


}