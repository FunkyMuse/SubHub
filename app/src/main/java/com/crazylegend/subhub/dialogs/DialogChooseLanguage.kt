package com.crazylegend.subhub.dialogs

import android.os.Bundle
import android.view.View
import com.crazylegend.kotlinextensions.gson.fromJson
import com.crazylegend.kotlinextensions.inputstream.readTextAndClose
import com.crazylegend.kotlinextensions.recyclerview.clickListeners.forItemClickListenerDSL
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown
import com.crazylegend.subhub.R
import com.crazylegend.subhub.adapters.chooseLanguage.LanguageAdapter
import com.crazylegend.subhub.adapters.chooseLanguage.LanguageItem
import com.crazylegend.subhub.consts.DEBOUNCE_TIME
import com.crazylegend.subhub.consts.DEBOUNCE_TIME_UNIT
import com.crazylegend.subhub.consts.LANGUAGES_FILE_CONST
import com.crazylegend.subhub.core.AbstractDialogFragment
import com.crazylegend.subhub.listeners.onLanguageChosen
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.dialog_choose_language.view.*


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
class DialogChooseLanguage : AbstractDialogFragment() {
    override val setView: Int
        get() = R.layout.dialog_choose_language

    private val languageAdapter by lazy {
        LanguageAdapter()
    }

    var onLanguageChosen: onLanguageChosen? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.dialog_ss_cancel?.setOnClickListenerCooldown {
            dismissAllowingStateLoss()
        }
        component.setupRecycler(view.dialog_ss_languages, linearLayoutManager, languageAdapter, true)
        val adapterList = resources.assets.open(LANGUAGES_FILE_CONST).use {
            val list = component.gson.fromJson<List<String>>(it.readTextAndClose())

            list.map {
                val splitString = it.split(" ")
                LanguageItem(splitString[0], splitString[1])
            }
        }

        languageAdapter.submitList(adapterList)
        languageAdapter.forItemClickListener = forItemClickListenerDSL { _, item, _ ->
            onLanguageChosen?.forLanguage(item)
            dismissAllowingStateLoss()
        }
        view.dialog_mss_movie_name_input?.textChanges()?.skipInitialValue()?.debounce(DEBOUNCE_TIME, DEBOUNCE_TIME_UNIT)?.map {
            it.toString()
        }?.subscribe { query ->
            languageAdapter.submitList(adapterList.filter {
                it.name.toString().contains(query, true)
            })
        }?.addTo(component.compositeDisposable)

    }
}