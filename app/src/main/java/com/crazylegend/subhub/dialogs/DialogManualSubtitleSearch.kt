package com.crazylegend.subhub.dialogs

import android.os.Bundle
import android.view.View
import com.crazylegend.kotlinextensions.activity.remove
import com.crazylegend.kotlinextensions.animations.attentionShake
import com.crazylegend.kotlinextensions.animations.playAnimation
import com.crazylegend.kotlinextensions.fragments.launchActivity
import com.crazylegend.kotlinextensions.views.clearError
import com.crazylegend.kotlinextensions.views.getString
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown
import com.crazylegend.subhub.R
import com.crazylegend.subhub.activities.LoadSubtitlesActivity
import com.crazylegend.subhub.consts.ANIM_TIME
import com.crazylegend.subhub.consts.DIALOG_CHOOSE_LANGUAGE_TAG
import com.crazylegend.subhub.consts.INTENT_MOVIE_NAME_TAG
import com.crazylegend.subhub.core.AbstractDialogFragment
import kotlinx.android.synthetic.main.dialog_manual_sub_search.view.*


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
class DialogManualSubtitleSearch : AbstractDialogFragment() {
    override val setView: Int
        get() = R.layout.dialog_manual_sub_search

    private lateinit var dialogChooseLanguage: DialogChooseLanguage

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.dialog_mss_cancel?.setOnClickListenerCooldown {
            dismissAllowingStateLoss()
        }

        view.dialog_mss_language_input?.setOnClickListenerCooldown {
            childFragmentManager.findFragmentByTag(DIALOG_CHOOSE_LANGUAGE_TAG)?.remove()
            dialogChooseLanguage = DialogChooseLanguage()
            dialogChooseLanguage.show(childFragmentManager, DIALOG_CHOOSE_LANGUAGE_TAG)
        }

        view.dialog_mss_submit?.setOnClickListenerCooldown {
            val movieName = view.dialog_mss_movie_name_input
            movieName ?: return@setOnClickListenerCooldown
            movieName.clearError()

            if (movieName.getString.isEmpty()) {
                movieName.let {
                    it.attentionShake().playAnimation(ANIM_TIME)
                    it.error = getString(R.string.empty_field)
                }
                return@setOnClickListenerCooldown
            }

            launchActivity<LoadSubtitlesActivity> {
                putExtra(INTENT_MOVIE_NAME_TAG, movieName.getString)
            }
            dismissAllowingStateLoss()
        }
    }
}

