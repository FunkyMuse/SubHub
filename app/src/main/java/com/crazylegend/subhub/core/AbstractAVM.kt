package com.crazylegend.subhub.core

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.crazylegend.subhub.di.core.CoreComponentImpl


/**
 * Created by crazy on 11/27/19 to long live and prosper !
 */
abstract class AbstractAVM(application: Application) : AndroidViewModel(application) {

    protected val component by lazy {
        CoreComponentImpl(application)
    }

    override fun onCleared() {
        super.onCleared()
        component.disposeResources()
    }

}
