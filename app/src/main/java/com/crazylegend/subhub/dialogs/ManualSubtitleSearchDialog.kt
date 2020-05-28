package com.crazylegend.subhub.dialogs

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.crazylegend.kotlinextensions.animations.attentionShake
import com.crazylegend.kotlinextensions.animations.playAnimation
import com.crazylegend.kotlinextensions.context.isOnline
import com.crazylegend.kotlinextensions.fragments.shortToast
import com.crazylegend.kotlinextensions.tryOrIgnore
import com.crazylegend.kotlinextensions.viewBinding.viewBinding
import com.crazylegend.kotlinextensions.views.clearError
import com.crazylegend.kotlinextensions.views.getString
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown
import com.crazylegend.kotlinextensions.views.setTheText
import com.crazylegend.subhub.R
import com.crazylegend.subhub.consts.*
import com.crazylegend.subhub.core.AbstractDialogFragment
import com.crazylegend.subhub.databinding.DialogManualSubSearchBinding
import com.crazylegend.subhub.dtos.LanguageItem
import com.crazylegend.subhub.utils.getSelectedLanguage
import com.crazylegend.subhub.utils.isNullStringOrEmpty


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
class ManualSubtitleSearchDialog : AbstractDialogFragment(R.layout.dialog_manual_sub_search) {

    private var chosenLanguage: LanguageItem? = null
    private var pickedDirModel: Uri? = null

    override val binding by viewBinding(DialogManualSubSearchBinding::bind)

    private val args by navArgs<ManualSubtitleSearchDialogArgs>()
    private val movieNameArg get() = args.movieName

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleProvider.loadBanner(binding.adView)
        chosenLanguage = requireContext().getSelectedLanguage

        chosenLanguage?.apply {
            tryOrIgnore {
                if (!name.isNullStringOrEmpty()) {
                    binding.languageInput.setTheText(name.toString())
                }
            }
        }

        binding.movieNameInput.setTheText(movieNameArg
                ?: "")

        binding.cancel.setOnClickListenerCooldown {
            tryOrIgnore { findNavController().navigateUp() }
        }

        binding.languageInput.setOnClickListenerCooldown {
            pickALanguage()
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
                shortToast(R.string.no_internet_connection)
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
                shortToast(R.string.pick_movie_language)
                pickALanguage()
                return@setOnClickListenerCooldown
            }

            if (pickedDirModel == null) {
                directory.apply {
                    attentionShake().playAnimation(ANIM_TIME)
                    error = getString(R.string.empty_field)
                }
                return@setOnClickListenerCooldown
            }

            tryOrIgnore {
                findNavController().navigate(ManualSubtitleSearchDialogDirections.actionLoadSubtitles(
                        movieName = movieName.getString,
                        pickedDir = pickedDirModel,
                        languageItem = chosenLanguage
                ))
            }
        }
    }

    /*private val pickDir = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) {

    }*/

    private fun pickALanguage() {
        tryOrIgnore { findNavController().navigate(ManualSubtitleSearchDialogDirections.actionChooseLanguage()) }
        setFragmentResultListener(LANGUAGE_REQ_KEY) { _, bundle ->
            chosenLanguage = bundle.getParcelable(ON_LANGUAGE_KEY)
            chosenLanguage?.apply {
                tryOrIgnore {
                    if (!name.isNullStringOrEmpty()) {
                        binding.languageInput.setTheText(name.toString())
                        binding.languageInput.clearError()
                    }
                }
            }
        }
    }

    private fun pickDownloadDirectory() {
        permissionsProvider.openDocumentTree(onCalled = {
            pickedDirModel = it
        }, onPermissionsGranted = {
            val name = DocumentFile.fromTreeUri(requireContext(), it)?.name ?: ""
            binding.downloadLocationInput.setTheText(name)
            binding.downloadLocationInput.clearError()
        }, onDenied = {
            binding.downloadLocationInput.setTheText("")
            binding.downloadLocationInput.clearError()
        }).launch(null)
    }


    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        savedInstanceState?.getParcelable<LanguageItem>(SAVING_STATE_MODEL)?.apply {
            chosenLanguage = this
        }
        savedInstanceState?.getParcelable<Uri>(SAVING_PICKED_ITEM_MODEL)?.apply {
            pickedDirModel = this
        }
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(SAVING_STATE_MODEL, chosenLanguage)
        outState.putParcelable(SAVING_PICKED_ITEM_MODEL, pickedDirModel)
        super.onSaveInstanceState(outState)
    }
}
