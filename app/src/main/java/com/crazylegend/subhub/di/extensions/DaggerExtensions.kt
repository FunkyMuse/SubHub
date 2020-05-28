package com.crazylegend.subhub.di.extensions

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.crazylegend.subhub.AppLevel
import com.crazylegend.subhub.di.components.LifecycleComponent


/**
 * Created by crazy on 5/2/20 to long live and prosper !
 */

val FragmentActivity.applicationLevel get() = application as AppLevel
val Fragment.applicationLevel get() = requireActivity().applicationLevel

fun Fragment.injector(fragment: Fragment, bundle: Bundle?, onComponent: LifecycleComponent.() -> Unit) =
        applicationLevel.appComponent.lifecycleFactory().create(bundle, fragment).onComponent()