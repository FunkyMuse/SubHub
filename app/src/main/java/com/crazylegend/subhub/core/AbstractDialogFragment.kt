package com.crazylegend.subhub.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.crazylegend.kotlinextensions.context.getCompatDrawable
import com.crazylegend.subhub.R
import com.crazylegend.subhub.di.CoreComponentImpl


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
abstract class AbstractDialogFragment : DialogFragment() {

    val component by lazy {
        CoreComponentImpl(requireActivity().application)
    }



    abstract val setView: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(setView, container, false)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)
        dialog?.window?.setBackgroundDrawable(requireContext().getCompatDrawable(R.drawable.dialog_bg))
        return view
    }


}