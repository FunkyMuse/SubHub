package com.crazylegend.subhub.dialogs

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.crazylegend.kotlinextensions.animations.attentionShake
import com.crazylegend.kotlinextensions.animations.playAnimation
import com.crazylegend.kotlinextensions.context.isOnline
import com.crazylegend.kotlinextensions.fragments.launchActivity
import com.crazylegend.kotlinextensions.storage.openDirectory
import com.crazylegend.kotlinextensions.tryOrIgnore
import com.crazylegend.kotlinextensions.viewBinding.viewBinding
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
import com.crazylegend.subhub.databinding.DialogManualSubSearchBinding
import com.crazylegend.subhub.listeners.onDirChosenDSL
import com.crazylegend.subhub.pickedDirs.PickedDirModel
import com.crazylegend.subhub.utils.isNullStringOrEmpty


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
class DialogManualSubtitleSearch : AbstractDialogFragment() {
    override val setView: Int
        get() = R.layout.dialog_manual_sub_search

    private var chosenLanguage: LanguageItem? = null
    private var pickedDirModel: PickedDirModel? = null

    private val binding by viewBinding(DialogManualSubSearchBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.loadBanner(binding.adView)
        chosenLanguage = component.getLanguagePref
        pickedDirModel = component.getDownloadLocationPref

        chosenLanguage?.apply {
            tryOrIgnore {
                if (!name.isNullStringOrEmpty()) {
                    binding.languageInput.setTheText(this.name.toString())
                }
            }
        }

        pickedDirModel?.apply {
            tryOrIgnore {
                if (!name.isNullStringOrEmpty()) {
                    binding.downloadLocationInput.setTheText(this.name)
                    binding.downloadLocationInput.clearError()
                }
            }
        }


        binding.movieNameInput.setTheText(arguments?.getString(INTENT_MOVIE_NAME_TAG)
                ?: "")

        binding.cancel.setOnClickListenerCooldown {
            dismissAllowingStateLoss()
        }

        binding.languageInput.setOnClickListenerCooldown {
            component.showDialogChooseLanguage(childFragmentManager) {
                chosenLanguage = it
                it.name?.apply {
                    tryOrIgnore {
                        if (!this.isNullStringOrEmpty()) {
                            binding.languageInput.setTheText(this)
                        }
                    }
                }
            }
        }

        binding.downloadLocationInput.setOnClickListenerCooldown {
            pickDownloadDirectory()
        }

        binding.submit.setOnClickListenerCooldown {
            val movieName = binding.movieNameInput
            movieName.clearError()

            val directory = binding.downloadLocationInput
            directory.clearError()

            val language = binding.languageInput
            language.clearError()

            if (!requireContext().isOnline) {
                component.toaster.jobToast(getString(R.string.no_internet_connection))
                return@setOnClickListenerCooldown
            }

            if (movieName.getString.isEmpty()) {
                movieName.apply {
                    attentionShake().playAnimation(ANIM_TIME)
                    error = getString(R.string.empty_field)
                }
                return@setOnClickListenerCooldown
            }

            if (chosenLanguage == null) {
                language.apply {
                    attentionShake().playAnimation(ANIM_TIME)
                    error = getString(R.string.empty_field)
                }
                component.toaster.jobToast(getString(R.string.pick_movie_language))
                component.showDialogChooseLanguage(childFragmentManager) {
                    language.clearError()
                    chosenLanguage = it
                    it.name?.apply {
                        binding.languageInput.setTheText(this)
                    }
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

            launchActivity<LoadSubtitlesActivity> {
                putExtra(INTENT_MOVIE_NAME_TAG, movieName.getString)
                putExtra(INTENT_MOVIE_LANG_TAG, chosenLanguage)
                putExtra(INTENT_MOVIE_DOWNLOAD_LOCATION_TAG, pickedDirModel)
            }
            dismissAllowingStateLoss()
        }

        MainActivity.onDirChosen = onDirChosenDSL {
            pickedDirModel = it
            tryOrIgnore {
                if (!it.name.isNullStringOrEmpty()) {
                    binding.downloadLocationInput.setTheText(it.name)
                    binding.downloadLocationInput.clearError()
                }
            }
        }
    }

    private fun pickDownloadDirectory() {
        requireActivity().openDirectory(PICK_DOWNLOAD_DIRECTORY_REQUEST_CODE)
    }


    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        savedInstanceState?.getParcelable<LanguageItem>(SAVING_STATE_MODEL)?.apply {
            chosenLanguage = this
        }
        savedInstanceState?.getParcelable<PickedDirModel>(SAVING_PICKED_ITEM_MODEL)?.apply {
            pickedDirModel = this
        }
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
