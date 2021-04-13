package com.crazylegend.subhub.dialogs

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.crazylegend.coroutines.textChanges
import com.crazylegend.gson.fromJson
import com.crazylegend.kotlinextensions.inputstream.readTextAndClose
import com.crazylegend.kotlinextensions.lifeCycle.repeatingJobOnStarted
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown
import com.crazylegend.recyclerview.clickListeners.forItemClickListener
import com.crazylegend.subhub.R
import com.crazylegend.subhub.consts.DEBOUNCE_TIME
import com.crazylegend.subhub.consts.LANGUAGES_FILE_CONST
import com.crazylegend.subhub.consts.LANGUAGE_REQ_KEY
import com.crazylegend.subhub.consts.ON_LANGUAGE_KEY
import com.crazylegend.subhub.core.AbstractDialogFragment
import com.crazylegend.subhub.databinding.DialogChooseLanguageBinding
import com.crazylegend.subhub.di.providers.AdaptersProvider
import com.crazylegend.subhub.dtos.LanguageItem
import com.crazylegend.viewbinding.viewBinding
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
@AndroidEntryPoint
class ChooseLanguageDialog : AbstractDialogFragment(R.layout.dialog_choose_language) {

    override val binding by viewBinding(DialogChooseLanguageBinding::bind)

    @Inject
    lateinit var adaptersProvider: AdaptersProvider

    @Inject
    lateinit var gson: Gson

    private val languageAdapter by lazy {
        adaptersProvider.languageAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cancel.setOnClickListenerCooldown {
            dismissAllowingStateLoss()
        }
        binding.languages.adapter = languageAdapter

        val adapterList = resources.assets.open(LANGUAGES_FILE_CONST).use {
            val list = gson.fromJson<List<String>>(it.readTextAndClose())
            list.map {
                val splitString = it.split(" ")
                LanguageItem(splitString[0], splitString[1])
            }
        }

        languageAdapter.submitList(adapterList)
        languageAdapter.forItemClickListener = forItemClickListener { _, item, _ ->
            setFragmentResult(LANGUAGE_REQ_KEY, bundleOf(ON_LANGUAGE_KEY to item))
            dismissAllowingStateLoss()
        }
        repeatingJobOnStarted {
            binding.movieNameInput.textChanges(debounce = DEBOUNCE_TIME).map { it.toString() }.collect { query ->
                languageAdapter.submitList(adapterList.filter {
                    it.name.toString().contains(query, true)
                })
            }
        }

    }
}