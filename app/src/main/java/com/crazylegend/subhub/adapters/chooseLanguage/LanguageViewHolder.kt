package com.crazylegend.subhub.adapters.chooseLanguage

import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.recyclerview.context
import com.crazylegend.kotlinextensions.views.setPrecomputedText
import com.crazylegend.subhub.R
import com.crazylegend.subhub.databinding.ItemviewLanguageBinding
import com.crazylegend.subhub.dtos.LanguageItem

class LanguageViewHolder(private val binding: ItemviewLanguageBinding) : RecyclerView.ViewHolder(binding.root) {


    fun bind(item: LanguageItem) {
        binding.languageName.setPrecomputedText(context.getString(R.string.language_placeholder, item.name))
        binding.languageCode.setPrecomputedText(itemView.context.getString(R.string.language_code_placeholder, item.code))
    }

}