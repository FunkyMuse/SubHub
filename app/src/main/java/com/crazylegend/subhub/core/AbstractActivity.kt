package com.crazylegend.subhub.core

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.crazylegend.subhub.di.contracts.InjectionContracts
import com.crazylegend.subhub.di.extensions.injector
import com.crazylegend.subhub.di.providers.AdaptersProvider
import com.crazylegend.subhub.di.providers.AppProvider
import com.crazylegend.subhub.di.providers.LifecycleProvider
import javax.inject.Inject


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
abstract class AbstractActivity : AppCompatActivity(), InjectionContracts {

    @Inject
    override lateinit var appProvider: AppProvider

    @Inject
    override lateinit var lifecycleProvider: LifecycleProvider

    @Inject
    override lateinit var adaptersProvider: AdaptersProvider


    abstract val binding: ViewBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        injector(savedInstanceState) { inject(this@AbstractActivity) }
    }


    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

}