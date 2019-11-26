package com.crazylegend.subhub.adapters.chooseLanguage

import com.crazylegend.subhub.R
import com.crazylegend.subhub.core.AbstractAdapter


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
//extract the classes to separate files

/**
 * Template created by Hristijan to live long and prosper.
 */

class LanguageAdapter : AbstractAdapter<LanguageItem, LanguageViewHolder>(LanguageDiffUtil(), LanguageViewHolder::class.java) {


    override val getLayout: Int
        get() = R.layout.itemview_language

    override fun bindItems(item: LanguageItem, holder: LanguageViewHolder, position: Int) {
        holder.bind(item)
    }

}

