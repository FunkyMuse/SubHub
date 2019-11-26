package com.crazylegend.subhub.dialogs

import android.os.Bundle
import android.view.View
import com.crazylegend.kotlinextensions.gson.fromJson
import com.crazylegend.kotlinextensions.readTextAndClose
import com.crazylegend.kotlinextensions.recyclerview.clickListeners.forItemClickListenerDSL
import com.crazylegend.subhub.R
import com.crazylegend.subhub.adapters.chooseLanguage.LanguageAdapter
import com.crazylegend.subhub.adapters.chooseLanguage.LanguageItem
import com.crazylegend.subhub.consts.LANGUAGES_FILE_CONST
import com.crazylegend.subhub.core.AbstractBottomSheetDialogFragment
import com.crazylegend.subhub.listeners.onLanguageChosen
import kotlinx.android.synthetic.main.dialog_select_language.view.*


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
class DialogChooseLanguage : AbstractBottomSheetDialogFragment() {
    override val theView: Int
        get() = R.layout.dialog_select_language

    private val languageAdapter by lazy {
        LanguageAdapter()
    }

    var onLanguageChosen: onLanguageChosen? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.setupRecycler(view.dialog_ss_languages, linearLayoutManager, languageAdapter)
        resources.assets.open(LANGUAGES_FILE_CONST).use {
            val list = component.gson.fromJson<List<String>>(it.readTextAndClose())

            val adapterList = list.map {
                val splitString = it.split(" ")
                LanguageItem(splitString[0], splitString[1])
            }

            languageAdapter.submitList(adapterList)
            languageAdapter.forItemClickListener = forItemClickListenerDSL { _, item, _ ->
                onLanguageChosen?.forLanguage(item)
            }
        }
    }
}