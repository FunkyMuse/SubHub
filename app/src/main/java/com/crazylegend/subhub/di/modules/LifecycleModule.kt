package com.crazylegend.subhub.di.modules

import android.content.Context
import androidx.fragment.app.Fragment
import com.crazylegend.kotlinextensions.internetdetector.InternetDetector
import com.crazylegend.subhub.di.providers.AdaptersProvider
import com.crazylegend.subhub.di.scopes.PerLifecycle
import dagger.Module
import dagger.Provides


/**
 * Created by crazy on 5/4/20 to long live and prosper !
 */

@Module
object LifecycleModule {

    @Provides
    @PerLifecycle
    @JvmStatic
    fun context(fragment: Fragment) = fragment.requireContext()


    @Provides
    @PerLifecycle
    @JvmStatic
    fun fragmentManager(fragment: Fragment) = fragment.childFragmentManager

    @Provides
    @PerLifecycle
    @JvmStatic
    fun lifecycleOwner(fragment: Fragment) = fragment.viewLifecycleOwner

    @Provides
    @JvmStatic
    @PerLifecycle
    fun adapters() = AdaptersProvider()

    @Provides
    @JvmStatic
    @PerLifecycle
    fun internetDetector(context: Context) = InternetDetector(context)


}