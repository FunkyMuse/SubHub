package com.crazylegend.subhub.core

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.crazylegend.kotlinextensions.context.getCompatDrawable
import com.crazylegend.subhub.R
import com.crazylegend.subhub.di.contracts.InjectionContracts
import com.crazylegend.subhub.di.extensions.injector
import com.crazylegend.subhub.di.providers.AdaptersProvider
import com.crazylegend.subhub.di.providers.AppProvider
import com.crazylegend.subhub.di.providers.LifecycleProvider
import com.crazylegend.subhub.di.providers.PermissionsProvider
import javax.inject.Inject


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
abstract class AbstractDialogFragment(setView: Int) : DialogFragment(setView), InjectionContracts {

    @Inject
    override lateinit var appProvider: AppProvider

    @Inject
    override lateinit var lifecycleProvider: LifecycleProvider

    @Inject
    override lateinit var adaptersProvider: AdaptersProvider

    @Inject
    override lateinit var permissionsProvider: PermissionsProvider

    abstract val binding: ViewBinding


    override fun onCreateDialog(savedInstanceState: Bundle?) = with(super.onCreateDialog(savedInstanceState)) {
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        window?.setBackgroundDrawable(requireContext().getCompatDrawable(R.drawable.dialog_bg))
        this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injector(this, savedInstanceState) { inject(this@AbstractDialogFragment) }

    }
}