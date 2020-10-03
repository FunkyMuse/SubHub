package com.crazylegend.subhub.di.providers

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.context.longToast
import com.crazylegend.kotlinextensions.intent.openWebPage
import com.crazylegend.kotlinextensions.internetdetector.InternetDetector
import com.crazylegend.kotlinextensions.recyclerview.isEmpty
import com.crazylegend.kotlinextensions.recyclerview.registerDataObserver
import com.crazylegend.kotlinextensions.rx.clearAndDispose
import com.crazylegend.subhub.R
import com.crazylegend.subhub.di.scopes.PerLifecycle
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject


/**
 * Created by crazy on 5/2/20 to long live and prosper !
 */
@PerLifecycle
class LifecycleProvider @Inject constructor(
        private val context: Context,
        private val fragmentManager: FragmentManager,
        private val lifecycleOwner: LifecycleOwner,
        private val bundle: Bundle? = null,
        private val internetDetector: InternetDetector
) : LifecycleObserver {

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    private var dataObserver: RecyclerView.AdapterDataObserver? = null
    private var temporaryAdapter: RecyclerView.Adapter<*>? = null
    internal val compositeDisposable = CompositeDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun disposeResources() {
        compositeDisposable.clearAndDispose()
        internetDetector.removeObservers(lifecycleOwner)
        dataObserver?.let { temporaryAdapter?.unregisterAdapterDataObserver(it) }
    }



    fun openWebPage(url: String) {
        context.openWebPage(url) {
            context.longToast(R.string.no_browsers)
        }
    }

    fun onDataChanged(adapter: RecyclerView.Adapter<*>, emptyAction: () -> Unit, notEmptyAction: () -> Unit) {
        temporaryAdapter = adapter
        dataObserver = temporaryAdapter?.registerDataObserver {
            if (temporaryAdapter?.isEmpty == true) {
                emptyAction()
            } else {
                notEmptyAction()
            }
        }
    }


}