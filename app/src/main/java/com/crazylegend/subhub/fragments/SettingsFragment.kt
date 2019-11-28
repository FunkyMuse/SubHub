package com.crazylegend.subhub.fragments

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.crazylegend.kotlinextensions.NEW_LINE
import com.crazylegend.kotlinextensions.storage.openDirectory
import com.crazylegend.subhub.R
import com.crazylegend.subhub.activities.SettingsActivity
import com.crazylegend.subhub.consts.DELETE_CACHE_PREF
import com.crazylegend.subhub.consts.PICK_DOWNLOAD_DIRECTORY_REQUEST_CODE
import com.crazylegend.subhub.consts.PREFERRED_DOWNLOAD_LOCATION_PREF
import com.crazylegend.subhub.consts.PREFERRED_LANGUAGE_PREF
import com.crazylegend.subhub.di.core.CoreComponentImpl
import com.crazylegend.subhub.di.fragment.FragmentComponentImpl
import com.crazylegend.subhub.listeners.onDirChosenDSL
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
    private var downloadLocationPref: Preference? = null
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)
        languagePref = findPreference(PREFERRED_LANGUAGE_PREF)
        downloadLocationPref = findPreference(PREFERRED_DOWNLOAD_LOCATION_PREF)
        deleteCache = findPreference(DELETE_CACHE_PREF)

        val lang = fragmentComponent.getLanguagePref
        val dlLocation = fragmentComponent.getDownloadLocationPref

        lang?.apply {
            val summary = languagePref?.summary?.toString()
            summary ?: return@apply
            val modifiedSummary = summary + NEW_LINE + NEW_LINE + getString(R.string.currently_selected_language, name)
            languagePref?.summary = modifiedSummary
        }

        dlLocation?.apply {
            val summary = downloadLocationPref?.summary?.toString()
            summary ?: return@apply
            val modifiedSummary = summary + NEW_LINE + NEW_LINE + getString(R.string.currently_selected_dl_location, name)
            downloadLocationPref?.summary = modifiedSummary
        }

        deleteCache?.setOnPreferenceClickListener {
            val deletionStatus = requireContext().cacheDir.deleteRecursively()
            if (deletionStatus) {
                fragmentComponent.toaster.jobToast(getString(R.string.deletion_success))
            } else {
                fragmentComponent.toaster.jobToast(getString(R.string.deletion_failed))
            }
            true
        }

        languagePref?.setOnPreferenceClickListener {

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
                            fragmentComponent.removeLanguagePref()
                            languagePref?.summary = getString(R.string.preferred_language_expl)
                        }
                        ButtonClicked.RIGHT -> {
                            pickLanguage()
                        }
                    }
                }
            }

            true
        }

        downloadLocationPref?.setOnPreferenceClickListener {
            if (fragmentComponent.getDownloadLocationPref == null) {
                pickNewDownloadLocation()
            } else {
                fragmentComponent.showDialogConfirmation(childFragmentManager, fragmentComponent.confirmationArguments(
                        title = getString(R.string.remove_dl_location_or_insert_new_one),
                        subtitle = getString(R.string.currently_selected, fragmentComponent.getDownloadLocationPref?.name),
                        leftButtonText = getString(R.string.remove),
                        rightButtonText = getString(R.string.new_one)
                )) {
                    when (it) {
                        ButtonClicked.LEFT -> {
                            fragmentComponent.removeDownloadLocationPref()
                            downloadLocationPref?.summary = getString(R.string.selected_dl_folder_expl)
                        }
                        ButtonClicked.RIGHT -> {
                            pickNewDownloadLocation()
                        }
                    }
                }
            }
            true
        }


        SettingsActivity.onDirChosen = onDirChosenDSL {
            it.apply {
                val summary = downloadLocationPref?.summary?.toString()
                summary ?: return@apply
                val modifiedSummary = summary + NEW_LINE + NEW_LINE + getString(R.string.currently_selected_dl_location, name)
                downloadLocationPref?.summary = modifiedSummary
            }
        }
    }

    private fun pickLanguage() {
        fragmentComponent.showDialogChooseLanguage(childFragmentManager) {
            fragmentComponent.addLanguageToPrefs(it)
            it.apply {
                val summary = languagePref?.summary?.toString()
                summary ?: return@apply
                val modifiedSummary = summary + NEW_LINE + NEW_LINE + getString(R.string.currently_selected_language, name)
                languagePref?.summary = modifiedSummary
            }
        }
    }


    private fun pickNewDownloadLocation() {
        requireActivity().openDirectory(PICK_DOWNLOAD_DIRECTORY_REQUEST_CODE)
    }
}