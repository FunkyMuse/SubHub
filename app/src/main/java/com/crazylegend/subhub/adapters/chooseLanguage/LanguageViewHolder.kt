package com.crazylegend.subhub.adapters.chooseLanguage

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.views.setPrecomputedText

import com.crazylegend.subhub.R
import kotlinx.android.synthetic.main.itemview_language.view.*

class LanguageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: LanguageItem) {
        itemView.il_language_name?.setPrecomputedText(itemView.context.getString(R.string.language_placeholder, item.name))
        itemView.il_language_code?.setPrecomputedText(itemView.context.getString(R.string.language_code_placeholder, item.code))
    }

}