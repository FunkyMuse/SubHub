package com.crazylegend.subhub.vmfs

import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.crazylegend.subhub.adapters.chooseLanguage.LanguageItem
import com.crazylegend.subhub.vms.LoadSubsVM


/**
 * Created by crazy on 11/27/19 to long live and prosper !
 */

/**
 * Template created by Hristijan to live long and prosper.
 */

class LoadSubsVMF(private val application: Application, private val movieName: String, private val langCode: LanguageItem, private val dir: Uri) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoadSubsVM(application, movieName, langCode, dir) as T
    }

}