package com.crazylegend.subhub.dialogs

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.crazylegend.kotlinextensions.gson.fromJson
import com.crazylegend.kotlinextensions.inputstream.readTextAndClose
import com.crazylegend.kotlinextensions.recyclerview.clickListeners.forItemClickListenerDSL
import com.crazylegend.kotlinextensions.rx.bindings.textChanges
import com.crazylegend.kotlinextensions.viewBinding.viewBinding
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown
import com.crazylegend.subhub.R
import com.crazylegend.subhub.consts.DEBOUNCE_TIME
import com.crazylegend.subhub.consts.LANGUAGES_FILE_CONST
import com.crazylegend.subhub.consts.LANGUAGE_REQ_KEY
import com.crazylegend.subhub.consts.ON_LANGUAGE_KEY
import com.crazylegend.subhub.core.AbstractDialogFragment
import com.crazylegend.subhub.databinding.DialogChooseLanguageBinding
import com.crazylegend.subhub.dtos.LanguageItem

/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
class ChooseLanguageDialog : AbstractDialogFragment(R.layout.dialog_choose_language) {

    override val binding by viewBinding(DialogChooseLanguageBinding::bind)

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
            val list = appProvider.gson.fromJson<List<String>>(it.readTextAndClose())

            list.map {
                val splitString = it.split(" ")
                LanguageItem(splitString[0], splitString[1])
            }
        }

        languageAdapter.submitList(adapterList)
        languageAdapter.forItemClickListener = forItemClickListenerDSL { _, item, _ ->
            setFragmentResult(LANGUAGE_REQ_KEY, bundleOf(ON_LANGUAGE_KEY to item))
            dismissAllowingStateLoss()
        }
        binding.movieNameInput.textChanges(DEBOUNCE_TIME, compositeDisposable = lifecycleProvider.compositeDisposable) { query ->
            languageAdapter.submitList(adapterList.filter {
                it.name.toString().contains(query, true)
            })
        }
    }
}