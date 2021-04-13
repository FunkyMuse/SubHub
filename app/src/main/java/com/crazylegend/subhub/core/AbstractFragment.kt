package com.crazylegend.subhub.core

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding


/**
 * Created by crazy on 5/23/20 to long live and prosper !
 */
abstract class AbstractFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {

    abstract val binding: ViewBinding

}