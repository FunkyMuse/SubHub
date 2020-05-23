package com.crazylegend.subhub.core

import android.app.Application
import android.database.ContentObserver
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crazylegend.kotlinextensions.livedata.context
import com.crazylegend.kotlinextensions.rx.clearAndDispose
import com.crazylegend.subhub.di.components.DaggerAppComponent
import com.crazylegend.subhub.di.providers.AppProvider
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject


/**
 * Created by crazy on 11/27/19 to long live and prosper !
 */
abstract class AbstractAVM(application: Application) : AndroidViewModel(application) {

    protected val contentResolver get() = context.contentResolver
    protected var contentObserver: ContentObserver? = null

    protected val loadingIndicatorData = MutableLiveData<Boolean>()
    val loadingIndicator: LiveData<Boolean> = loadingIndicatorData

    /**
     * Using this instead of event since it serves the same purpose thus it's needed here
     */
    protected var canLoad = true


    override fun onCleared() {
        super.onCleared()
        contentObserver?.apply {
            contentResolver.unregisterContentObserver(this)
        }
        compositeDisposable.clearAndDispose()

    }

    protected val compositeDisposable = CompositeDisposable()

    @Inject
    protected lateinit var appProvider: AppProvider

    init {
        DaggerAppComponent.factory().create(application).also { it.inject(this) }
    }

}
