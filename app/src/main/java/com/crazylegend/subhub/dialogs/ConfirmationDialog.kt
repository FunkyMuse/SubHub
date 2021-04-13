package com.crazylegend.subhub.dialogs

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.crazylegend.kotlinextensions.tryOrIgnore
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.textString
import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.subhub.R
import com.crazylegend.subhub.consts.ON_UPDATE_KEY
import com.crazylegend.subhub.consts.UPDATE_REQ_KEY
import com.crazylegend.subhub.core.AbstractDialogFragment
import com.crazylegend.subhub.databinding.DialogConfirmationBinding
import com.crazylegend.viewbinding.viewBinding

/**
 * Created by crazy on 11/28/19 to long live and prosper !
 */
class ConfirmationDialog : AbstractDialogFragment(R.layout.dialog_confirmation) {

    override val binding by viewBinding(DialogConfirmationBinding::bind)

    private val args by navArgs<ConfirmationDialogArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.title.text = args.title
        binding.leftButton.text = args.cancelText ?: getString(R.string.cancel)
        binding.rightButton.text = args.confirmationText ?: getString(R.string.submit)

        val subtitle = args.subtitle
        if (subtitle.isNullOrEmpty()) {
            binding.subtitle.gone()
        } else {
            binding.subtitle.visible()
            binding.subtitle.text = (subtitle)
        }

        if (binding.leftButton.textString == getString(R.string.cancel)) {
            binding.cancelButton.gone()
        } else {
            binding.cancelButton.visible()
        }
        binding.cancelButton.setOnClickListener { tryOrIgnore { findNavController().navigateUp() } }
        binding.leftButton.setOnClickListener {
            setFragmentResult(UPDATE_REQ_KEY, bundleOf(ON_UPDATE_KEY to false))
            tryOrIgnore { findNavController().navigateUp() }
        }

        binding.rightButton.setOnClickListener {
            setFragmentResult(UPDATE_REQ_KEY, bundleOf(ON_UPDATE_KEY to true))
            tryOrIgnore { findNavController().navigateUp() }
        }
    }
}