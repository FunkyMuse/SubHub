package com.crazylegend.subhub.dialogs

import android.os.Bundle
import android.view.View
import com.crazylegend.subhub.R
import com.crazylegend.subhub.core.AbstractBottomSheetDialogFragment


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
class DialogChooseLanguage : AbstractBottomSheetDialogFragment() {
    override val theView: Int
        get() = R.layout.dialog_select_language

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}