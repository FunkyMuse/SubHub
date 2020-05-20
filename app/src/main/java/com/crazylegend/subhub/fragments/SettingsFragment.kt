package com.crazylegend.subhub.fragments

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.crazylegend.kotlinextensions.NEW_LINE
import com.crazylegend.kotlinextensions.context.packageVersionName
import com.crazylegend.kotlinextensions.context.rateUs
import com.crazylegend.kotlinextensions.intent.openWebPage
import com.crazylegend.kotlinextensions.preferences.onClick
import com.crazylegend.subhub.R
import com.crazylegend.subhub.consts.*
import com.crazylegend.subhub.di.core.CoreComponentImpl
import com.crazylegend.subhub.di.fragment.FragmentComponentImpl
import com.crazylegend.subhub.dtos.LanguageItem
import com.crazylegend.subhub.utils.ButtonClicked


/**
 * Created by crazy on 11/28/19 to long live and prosper !
 */
class SettingsFragment : PreferenceFragmentCompat() {

    private val fragmentComponent by lazy {
        FragmentComponentImpl(this, CoreComponentImpl(requireActivity().application))
    }

    private var languagePref: Preference? = null
    private var deleteCache: Preference? = null
    private var rateUs: Preference? = null
    private var movieDLApp: Preference? = null
    private var version: Preference? = null
    private var privacyPolicy: Preference? = null
    private var myOtherApps: Preference? = null
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)
        version = findPreference(VERSION_PREF_KEY)
        myOtherApps = findPreference(MY_OTHER_APPS_PREF_KEY)
        languagePref = findPreference(PREFERRED_LANGUAGE_PREF)
        deleteCache = findPreference(DELETE_CACHE_PREF)
        rateUs = findPreference(RATE_US_PREF_KEY)
        privacyPolicy = findPreference(PRIVACY_POLICY_PREF_KEY)
        movieDLApp = findPreference(CHECKOUT_MOVIE_APP_KEY)

        version?.summary = requireContext().packageVersionName

        myOtherApps.onClick {
            requireContext().openWebPage(DEV_LINK)
        }

        deleteCache.onClick {
            if (requireContext().cacheDir.deleteRecursively()) {
                fragmentComponent.toaster.jobToast(getString(R.string.deletion_success))
            } else {
                fragmentComponent.toaster.jobToast(getString(R.string.deletion_failed))
            }
        }

        languagePref.onClick {
            if (fragmentComponent.getLanguagePref == null) {
                pickLanguage()
            } else {
                fragmentComponent.showDialogConfirmation(childFragmentManager, fragmentComponent.confirmationArguments(
                        title = getString(R.string.remove_selected_language_or_insert_new_one),
                        subtitle = getString(R.string.currently_selected, fragmentComponent.getLanguagePref?.name),
                        leftButtonText = getString(R.string.remove),
                        rightButtonText = getString(R.string.new_one)
                )) {
                    when (it) {
                        ButtonClicked.LEFT -> {
                            resetLangSummary()
                        }
                        ButtonClicked.RIGHT -> {
                            pickLanguage()
                        }
                    }
                }
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
        fragmentComponent.showDialogChooseLanguage(childFragmentManager) {
            fragmentComponent.addLanguageToPrefs(it)
            it.apply {
                updateLanguageSummary(this)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val lang = fragmentComponent.getLanguagePref

        lang?.apply {
            updateLanguageSummary(this)
        }
    }


    private fun resetLangSummary() {
        fragmentComponent.removeLanguagePref()
        languagePref?.summary = ""
        languagePref?.summary = getString(R.string.preferred_language_expl)
    }

    private fun updateLanguageSummary(languageItem: LanguageItem) {
        languageItem.apply {
            resetLangSummary()
            fragmentComponent.addLanguageToPrefs(languageItem)
            val summary = languagePref?.summary?.toString()
            summary ?: return@apply
            val modifiedSummary = summary + NEW_LINE + NEW_LINE + getString(R.string.currently_selected_language, name)
            languagePref?.summary = modifiedSummary
        }
    }

}