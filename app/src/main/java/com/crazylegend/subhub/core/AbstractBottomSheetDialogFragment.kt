package com.crazylegend.subhub.core

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.crazylegend.subhub.R
import com.crazylegend.subhub.di.core.CoreComponentImpl
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
abstract class AbstractBottomSheetDialogFragment : BottomSheetDialogFragment(){

    val component by lazy {
        CoreComponentImpl(requireActivity().application)
    }

    abstract val theView: Int

    lateinit var linearLayoutManager: LinearLayoutManager
        private set

    override fun getTheme(): Int = R.style.BSDTtheme
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(theView, container, false)
        linearLayoutManager = LinearLayoutManager(requireContext())
        dialog?.window?.setDimAmount(0.7f)
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        component.disposeResources()
    }

}