package com.crazylegend.subhub.listeners

import com.crazylegend.subhub.adapters.chooseLanguage.LanguageItem


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
interface onLanguageChosen {
    fun forLanguage(language: LanguageItem)
}


fun languageDSL(callback: (chosenLanguage: LanguageItem) -> Unit = {}) = object : onLanguageChosen {
    override fun forLanguage(language: LanguageItem) {
        callback.invoke(language)
    }
}