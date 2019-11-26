package com.crazylegend.subhub.adapters.chooseLanguage

import androidx.recyclerview.widget.DiffUtil

class LanguageDiffUtil : DiffUtil.ItemCallback<LanguageItem>() {
    override fun areItemsTheSame(oldItem: LanguageItem, newItem: LanguageItem) = oldItem == newItem

    override fun areContentsTheSame(oldItem: LanguageItem, newItem: LanguageItem) = oldItem == newItem
}