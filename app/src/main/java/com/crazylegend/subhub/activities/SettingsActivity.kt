package com.crazylegend.subhub.activities

import android.os.Bundle
import com.crazylegend.kotlinextensions.activity.replace
import com.crazylegend.kotlinextensions.viewBinding.viewBinding
import com.crazylegend.subhub.R
import com.crazylegend.subhub.core.AbstractActivity
import com.crazylegend.subhub.databinding.ActivitySettingsBinding
import com.crazylegend.subhub.fragments.SettingsFragment


/**
 * Created by crazy on 11/28/19 to long live and prosper !
 */
class SettingsActivity : AbstractActivity() {

    override val showBack: Boolean
        get() = true

    override val binding by viewBinding(ActivitySettingsBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.replace(SettingsFragment(), R.id.act_settings_fragment_container)
    }

}