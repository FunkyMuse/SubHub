package com.crazylegend.subhub.di.fragment

import androidx.fragment.app.Fragment
import com.crazylegend.subhub.di.core.CoreComponent


/**
 * Created by crazy on 11/27/19 to long live and prosper !
 */
class FragmentComponentImpl(override val instance: Fragment, coreComponent: CoreComponent) : FragmentComponent, CoreComponent by coreComponent {
}