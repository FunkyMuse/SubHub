package com.crazylegend.subhub.core

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.crazylegend.subhub.MainActivity
import com.crazylegend.subhub.di.contracts.InjectionContracts
import com.crazylegend.subhub.di.extensions.injector
import com.crazylegend.subhub.di.providers.AdaptersProvider
import com.crazylegend.subhub.di.providers.AppProvider
import com.crazylegend.subhub.di.providers.LifecycleProvider
import javax.inject.Inject


/**
 * Created by crazy on 5/23/20 to long live and prosper !
 */
abstract class AbstractFragment(contentLayoutId: Int) : Fragment(contentLayoutId), InjectionContracts {

    @Inject
    override lateinit var appProvider: AppProvider

    @Inject
    override lateinit var lifecycleProvider: LifecycleProvider

    @Inject
    override lateinit var adaptersProvider: AdaptersProvider

    fun changeTitle(title: String = "") {
        (requireActivity() as MainActivity).supportActionBar?.title = title
    }

    abstract val binding: ViewBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injector(savedInstanceState) { inject(this@AbstractFragment) }
    }

}