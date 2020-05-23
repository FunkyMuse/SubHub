package com.crazylegend.subhub.vms.loadSubs

import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.crazylegend.subhub.dtos.LanguageItem


/**
 * Created by crazy on 5/23/20 to long live and prosper !
 */

@Suppress("UNCHECKED_CAST")
class LoadSubsVMF(private val application: Application, private val movieName: String, private val langCode: LanguageItem, private val dir: Uri) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoadSubsVM(application, movieName, langCode, dir) as T
    }

}