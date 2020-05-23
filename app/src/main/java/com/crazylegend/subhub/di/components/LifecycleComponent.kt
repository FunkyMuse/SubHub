package com.crazylegend.subhub.di.components

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.crazylegend.subhub.core.AbstractActivity
import com.crazylegend.subhub.core.AbstractDialogFragment
import com.crazylegend.subhub.core.AbstractFragment
import com.crazylegend.subhub.di.modules.LifecycleModule
import com.crazylegend.subhub.di.scopes.PerLifecycle
import com.crazylegend.subhub.ui.SettingsFragment
import dagger.BindsInstance
import dagger.Subcomponent


/**
 * Created by crazy on 5/4/20 to long live and prosper !
 */

@PerLifecycle
@Subcomponent(modules = [LifecycleModule::class])
interface LifecycleComponent {

    fun inject(fragment: AbstractFragment)
    fun inject(fragment: AbstractDialogFragment)
    fun inject(fragment: SettingsFragment)
    fun inject(abstractActivity: AbstractActivity)


    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance context: Context,
                   @BindsInstance bundle: Bundle?,
                   @BindsInstance lifecycleOwner: LifecycleOwner,
                   @BindsInstance fragmentManager: FragmentManager): LifecycleComponent
    }
}