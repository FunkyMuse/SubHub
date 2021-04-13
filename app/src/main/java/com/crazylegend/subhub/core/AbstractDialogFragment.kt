package com.crazylegend.subhub.core

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.crazylegend.kotlinextensions.context.getCompatDrawable
import com.crazylegend.subhub.R


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
abstract class AbstractDialogFragment(setView: Int) : DialogFragment(setView) {


    abstract val binding: ViewBinding


    override fun onCreateDialog(savedInstanceState: Bundle?) = with(super.onCreateDialog(savedInstanceState)) {
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        window?.setBackgroundDrawable(requireContext().getCompatDrawable(R.drawable.dialog_bg))
        this
    }
}