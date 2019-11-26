package com.crazylegend.subhub.core

import androidx.appcompat.app.AppCompatActivity
import com.crazylegend.subhub.di.CoreComponentImpl


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
abstract class AbstractActivity(contentLayoutId: Int) : AppCompatActivity(contentLayoutId) {

    val component by lazy {
        CoreComponentImpl(application)
    }


}