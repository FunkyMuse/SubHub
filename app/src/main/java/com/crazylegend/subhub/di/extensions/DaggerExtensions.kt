package com.crazylegend.subhub.di.extensions

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.crazylegend.subhub.AppLevel
import com.crazylegend.subhub.di.components.LifecycleComponent


/**
 * Created by crazy on 5/2/20 to long live and prosper !
 */

val FragmentActivity.applicationLevel get() = application as AppLevel
val Fragment.applicationLevel get() = requireActivity().applicationLevel

fun AppCompatActivity.injector(bundle: Bundle?, onComponent: LifecycleComponent.() -> Unit) =
        applicationLevel.appComponent.lifecycleFactory().create(this, bundle, this, supportFragmentManager).onComponent()

fun Fragment.injector(bundle: Bundle?, onComponent: LifecycleComponent.() -> Unit) =
        applicationLevel.appComponent.lifecycleFactory().create(requireContext(), bundle, viewLifecycleOwner, childFragmentManager).onComponent()