package com.crazylegend.subhub.dialogs

import android.os.Bundle
import android.view.View
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown
import com.crazylegend.kotlinextensions.views.setPrecomputedText
import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.subhub.R
import com.crazylegend.subhub.consts.LEFT_BUTTON_TEXT_ARGUMENT_TAG
import com.crazylegend.subhub.consts.RIGHT_BUTTON_TEXT_ARGUMENT_TAG
import com.crazylegend.subhub.consts.SUBTITLE_ARGUMENT_TAG
import com.crazylegend.subhub.consts.TITLE_ARGUMENT_TAG
import com.crazylegend.subhub.core.AbstractDialogFragment
import com.crazylegend.subhub.listeners.onConfirmationCallback
import com.crazylegend.subhub.utils.ButtonClicked
import kotlinx.android.synthetic.main.dialog_confirmation.view.*


/**
 * Created by crazy on 11/28/19 to long live and prosper !
 */
class DialogConfirmation : AbstractDialogFragment() {
    override val setView: Int
        get() = R.layout.dialog_confirmation

    var onConfirmationCallback: onConfirmationCallback? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.dialog_confirmation_title_text?.setPrecomputedText(arguments?.getString(TITLE_ARGUMENT_TAG))
        view.dialog_confirmation_left_button?.text = arguments?.getString(LEFT_BUTTON_TEXT_ARGUMENT_TAG)
        view.dialog_confirmation_right_button?.text = arguments?.getString(RIGHT_BUTTON_TEXT_ARGUMENT_TAG)
        val subtitle = arguments?.getString(SUBTITLE_ARGUMENT_TAG)
        if (subtitle.isNullOrEmpty()) {
            view.dialog_confirmation_subtitle_text?.gone()
        } else {
            view.dialog_confirmation_subtitle_text?.visible()
            view.dialog_confirmation_subtitle_text?.setPrecomputedText(subtitle)
        }

        view.dialog_confirmation_left_button?.setOnClickListenerCooldown {
            onConfirmationCallback?.forButtonClicked(ButtonClicked.LEFT)
            dismissAllowingStateLoss()
        }

        view.dialog_confirmation_right_button?.setOnClickListenerCooldown {
            onConfirmationCallback?.forButtonClicked(ButtonClicked.RIGHT)
            dismissAllowingStateLoss()
        }

        view.dialog_confirmation_cancel_button?.setOnClickListenerCooldown {
            dismissAllowingStateLoss()
        }
    }
}