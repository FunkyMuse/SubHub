package com.crazylegend.subhub.core

import android.app.Application
import android.content.ContentResolver
import android.database.ContentObserver
import androidx.lifecycle.AndroidViewModel
import com.crazylegend.kotlinextensions.livedata.context
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow


/**
 * Created by crazy on 11/27/19 to long live and prosper !
 */
abstract class AbstractAVM(application: Application) : AndroidViewModel(application) {

    protected val contentResolver: ContentResolver get() = context.contentResolver
    protected var contentObserver: ContentObserver? = null

    protected val loadingIndicatorData = Channel<Boolean>(Channel.BUFFERED)
    val loadingIndicator = loadingIndicatorData.receiveAsFlow()

    /**
     * Using this instead of event since it serves the same purpose thus it's needed here
     */
    protected var canLoad = true


    override fun onCleared() {
        super.onCleared()
        contentObserver?.apply {
            contentResolver.unregisterContentObserver(this)
        }
    }

    protected val compositeDisposable = CompositeDisposable()


}
