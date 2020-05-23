package com.crazylegend.subhub.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.crazylegend.kotlinextensions.NEW_LINE
import com.crazylegend.kotlinextensions.context.packageVersionName
import com.crazylegend.kotlinextensions.context.rateUs
import com.crazylegend.kotlinextensions.intent.openWebPage
import com.crazylegend.kotlinextensions.preferences.onClick
import com.crazylegend.kotlinextensions.runDelayedOnUiThread
import com.crazylegend.kotlinextensions.tryOrIgnore
import com.crazylegend.subhub.R
import com.crazylegend.subhub.consts.*
import com.crazylegend.subhub.di.contracts.InjectionContracts
import com.crazylegend.subhub.di.extensions.injector
import com.crazylegend.subhub.di.providers.AdaptersProvider
import com.crazylegend.subhub.di.providers.AppProvider
import com.crazylegend.subhub.di.providers.LifecycleProvider
import com.crazylegend.subhub.dtos.LanguageItem
import com.crazylegend.subhub.utils.addSelectedLanguage
import com.crazylegend.subhub.utils.getSelectedLanguage
import com.crazylegend.subhub.utils.removeSelectedLanguage
import javax.inject.Inject


/**
 * Created by crazy on 11/28/19 to long live and prosper !
 */
class SettingsFragment : PreferenceFragmentCompat(), InjectionContracts {

    @Inject
    override lateinit var appProvider: AppProvider

    @Inject
    override lateinit var lifecycleProvider: LifecycleProvider

    @Inject
    override lateinit var adaptersProvider: AdaptersProvider

    private var languagePref: Preference? = null
    private var rateUs: Preference? = null
    private var movieDLApp: Preference? = null
    private var version: Preference? = null
    private var privacyPolicy: Preference? = null
    private var myOtherApps: Preference? = null

    private val lang get() = requireContext().getSelectedLanguage

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)
        version = findPreference(VERSION_PREF_KEY)
        myOtherApps = findPreference(MY_OTHER_APPS_PREF_KEY)
        languagePref = findPreference(PREFERRED_LANGUAGE_PREF)
        rateUs = findPreference(RATE_US_PREF_KEY)
        privacyPolicy = findPreference(PRIVACY_POLICY_PREF_KEY)
        movieDLApp = findPreference(CHECKOUT_MOVIE_APP_KEY)
        version?.summary = requireContext().packageVersionName

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        injector(savedInstanceState) { inject(this@SettingsFragment) }


        myOtherApps.onClick {
            requireContext().openWebPage(DEV_LINK)
        }

        languagePref.onClick {
            if (lang == null) {
                pickLanguage()
            } else {

                tryOrIgnore {
                    findNavController().navigate(SettingsFragmentDirections.actionConfirmation(
                            title = getString(R.string.remove_selected_language_or_insert_new_one),
                            subtitle = getString(R.string.currently_selected, lang?.name),
                            cancelText = getString(R.string.remove),
                            confirmationText = getString(R.string.new_one)
                    ))
                }
            }
        }

        setFragmentResultListener(UPDATE_REQ_KEY) { _, bundle ->
            if (bundle.getBoolean(ON_UPDATE_KEY, false)) {
                runDelayedOnUiThread(DEBOUNCE_TIME) {
                    pickLanguage()
                }
            } else {
                resetLangSummary()
            }
        }

        setFragmentResultListener(LANGUAGE_REQ_KEY) { _, bundle ->
            val chosenLanguage = bundle.getParcelable<LanguageItem>(ON_LANGUAGE_KEY)
            chosenLanguage?.apply {
                requireContext().addSelectedLanguage(this)
                updateLanguageSummary(this)
            }
        }

        rateUs.onClick {
            requireContext().rateUs()
        }

        movieDLApp.onClick {
            requireContext().openWebPage(MOVIE_APP_LINK)
        }

        privacyPolicy.onClick {
            requireContext().openWebPage(PRIVACY_POLICY_LINK)
        }
    }

    private fun pickLanguage() {
        tryOrIgnore {
            findNavController().navigate(SettingsFragmentDirections.actionPickDefaultLanguage())
        }
    }

    override fun onResume() {
        super.onResume()

        lang?.apply {
            updateLanguageSummary(this)
        }
    }


    private fun resetLangSummary() {
        requireContext().removeSelectedLanguage()
        languagePref?.summary = ""
        languagePref?.summary = getString(R.string.preferred_language_expl)
    }

    private fun updateLanguageSummary(languageItem: LanguageItem) {
        languageItem.apply {
            resetLangSummary()
            requireContext().addSelectedLanguage(languageItem)
            val summary = languagePref?.summary?.toString()
            summary ?: return@apply
            val modifiedSummary = summary + NEW_LINE + NEW_LINE + getString(R.string.currently_selected_language, name)
            languagePref?.summary = modifiedSummary
        }
    }

}