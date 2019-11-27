package com.crazylegend.subhub.dialogs

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.crazylegend.kotlinextensions.activity.remove
import com.crazylegend.kotlinextensions.animations.attentionShake
import com.crazylegend.kotlinextensions.animations.playAnimation
import com.crazylegend.kotlinextensions.fragments.launchActivity
import com.crazylegend.kotlinextensions.storage.openDirectory
import com.crazylegend.kotlinextensions.views.clearError
import com.crazylegend.kotlinextensions.views.getString
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown
import com.crazylegend.kotlinextensions.views.setTheText
import com.crazylegend.subhub.R
import com.crazylegend.subhub.activities.LoadSubtitlesActivity
import com.crazylegend.subhub.activities.MainActivity
import com.crazylegend.subhub.adapters.chooseLanguage.LanguageItem
import com.crazylegend.subhub.consts.*
import com.crazylegend.subhub.core.AbstractDialogFragment
import com.crazylegend.subhub.listeners.languageDSL
import com.crazylegend.subhub.listeners.onDirChosenDSL
import com.crazylegend.subhub.pickedDirs.PickedDirModel
import kotlinx.android.synthetic.main.dialog_manual_sub_search.view.*


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
class DialogManualSubtitleSearch : AbstractDialogFragment() {
    override val setView: Int
        get() = R.layout.dialog_manual_sub_search

    private lateinit var dialogChooseLanguage: DialogChooseLanguage
    private var chosenLanguage: LanguageItem? = null
    private var pickedDirModel: PickedDirModel? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.dialog_mss_movie_name_input?.setTheText(arguments?.getString(INTENT_MOVIE_NAME_TAG)
                ?: "")

        view.dialog_mss_cancel?.setOnClickListenerCooldown {
            dismissAllowingStateLoss()
        }

        view.dialog_mss_language_input?.setOnClickListenerCooldown {
            showDialogChooseLanguage()
        }

        view.dialog_mss_download_location_input?.setOnClickListenerCooldown {
            pickDownloadDirectory()
        }

        view.dialog_mss_submit?.setOnClickListenerCooldown {
            val movieName = view.dialog_mss_movie_name_input
            movieName ?: return@setOnClickListenerCooldown
            movieName.clearError()

            val directory = view.dialog_mss_download_location_input
            directory ?: return@setOnClickListenerCooldown
            directory.clearError()

            val language = view.dialog_mss_language_input
            language ?: return@setOnClickListenerCooldown
            language.clearError()

            if (movieName.getString.isEmpty()) {
                movieName.apply {
                    attentionShake().playAnimation(ANIM_TIME)
                    error = getString(R.string.empty_field)
                }
                return@setOnClickListenerCooldown
            }

            if (pickedDirModel == null) {
                directory.apply {
                    attentionShake().playAnimation(ANIM_TIME)
                    error = getString(R.string.empty_field)
                }
                return@setOnClickListenerCooldown
            }

            if (chosenLanguage == null) {
                showDialogChooseLanguage()
                language.apply {
                    attentionShake().playAnimation(ANIM_TIME)
                    error = getString(R.string.empty_field)
                }
                component.subToast.jobToast(getString(R.string.pick_movie_language))
                return@setOnClickListenerCooldown
            }

            launchActivity<LoadSubtitlesActivity> {
                putExtra(INTENT_MOVIE_NAME_TAG, movieName.getString)
                putExtra(INTENT_MOVIE_LANG_TAG, chosenLanguage)
                putExtra(INTENT_MOVIE_DOWNLOAD_LOCATION_TAG, pickedDirModel)
            }
            dismissAllowingStateLoss()
        }

        MainActivity.onDirChosen = onDirChosenDSL {
            pickedDirModel = it
            view.dialog_mss_download_location_input?.setTheText(it.name)
        }
    }

    private fun pickDownloadDirectory() {
        requireActivity().openDirectory(PICK_DOWNLOAD_DIRECTORY_REQUEST_CODE)
    }

    private fun showDialogChooseLanguage() {
        childFragmentManager.findFragmentByTag(DIALOG_CHOOSE_LANGUAGE_TAG)?.remove()
        dialogChooseLanguage = DialogChooseLanguage()
        dialogChooseLanguage.show(childFragmentManager, DIALOG_CHOOSE_LANGUAGE_TAG)
        dialogChooseLanguage.onLanguageChosen = languageDSL {
            chosenLanguage = it
            view?.dialog_mss_language_input?.setTheText(it.name)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        chosenLanguage = savedInstanceState?.getParcelable(SAVING_STATE_MODEL)
        pickedDirModel = savedInstanceState?.getParcelable(SAVING_PICKED_ITEM_MODEL)
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(SAVING_STATE_MODEL, chosenLanguage)
        outState.putParcelable(SAVING_PICKED_ITEM_MODEL, pickedDirModel)
        super.onSaveInstanceState(outState)
    }

    fun addArguments(videoName: String?) {
        arguments = bundleOf(Pair(INTENT_MOVIE_NAME_TAG, videoName))
    }
}

