package com.crazylegend.subhub.dialogs

import android.os.Bundle
import android.view.View
import com.crazylegend.kotlinextensions.viewBinding.viewBinding
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown

import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.subhub.R
import com.crazylegend.subhub.consts.LEFT_BUTTON_TEXT_ARGUMENT_TAG
import com.crazylegend.subhub.consts.RIGHT_BUTTON_TEXT_ARGUMENT_TAG
import com.crazylegend.subhub.consts.SUBTITLE_ARGUMENT_TAG
import com.crazylegend.subhub.consts.TITLE_ARGUMENT_TAG
import com.crazylegend.subhub.core.AbstractDialogFragment
import com.crazylegend.subhub.databinding.DialogConfirmationBinding
import com.crazylegend.subhub.listeners.onConfirmationCallback
import com.crazylegend.subhub.utils.ButtonClicked

/**
 * Created by crazy on 11/28/19 to long live and prosper !
 */
class DialogConfirmation : AbstractDialogFragment() {
    override val setView: Int
        get() = R.layout.dialog_confirmation

    private val binding by viewBinding(DialogConfirmationBinding::bind)

    var onConfirmationCallback: onConfirmationCallback? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.title.text = (arguments?.getString(TITLE_ARGUMENT_TAG))
        binding.leftButton.text = arguments?.getString(LEFT_BUTTON_TEXT_ARGUMENT_TAG)
        binding.rightButton.text = arguments?.getString(RIGHT_BUTTON_TEXT_ARGUMENT_TAG)
        val subtitle = arguments?.getString(SUBTITLE_ARGUMENT_TAG)
        if (subtitle.isNullOrEmpty()) {
            binding.subtitle.gone()
        } else {
            binding.subtitle.visible()
            binding.subtitle.text = (subtitle)
        }

        binding.leftButton.setOnClickListenerCooldown {
            onConfirmationCallback?.forButtonClicked(ButtonClicked.LEFT)
            dismissAllowingStateLoss()
        }

        binding.rightButton.setOnClickListenerCooldown {
            onConfirmationCallback?.forButtonClicked(ButtonClicked.RIGHT)
            dismissAllowingStateLoss()
        }

        binding.cancelButton.setOnClickListenerCooldown {
            dismissAllowingStateLoss()
        }
    }
}