package com.crazylegend.subhub.listeners

import com.crazylegend.subhub.utils.ButtonClicked


/**
 * Created by crazy on 11/28/19 to long live and prosper !
 */
interface onConfirmationCallback {
    fun forButtonClicked(buttonClicked: ButtonClicked)
}

fun onConfirmationCallbackDSL(callBack: (position: ButtonClicked) -> Unit = {}) = object : onConfirmationCallback {
    override fun forButtonClicked(buttonClicked: ButtonClicked) {
        callBack.invoke(buttonClicked)
    }
}