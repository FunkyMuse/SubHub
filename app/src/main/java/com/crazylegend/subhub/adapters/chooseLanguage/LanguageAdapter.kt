package com.crazylegend.subhub.adapters.chooseLanguage

import com.crazylegend.kotlinextensions.abstracts.AbstractViewBindingAdapter
import com.crazylegend.subhub.databinding.ItemviewLanguageBinding
import com.crazylegend.subhub.dtos.LanguageItem


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
//extract the classes to separate files

/**
 * Template created by Hristijan to live long and prosper.
 */

class LanguageAdapter : AbstractViewBindingAdapter<LanguageItem, LanguageViewHolder, ItemviewLanguageBinding>(LanguageViewHolder::class.java,
        ItemviewLanguageBinding::inflate) {


    override fun bindItems(item: LanguageItem, holder: LanguageViewHolder, position: Int, itemCount: Int) {
        holder.bind(item)
    }

}

