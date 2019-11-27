package com.crazylegend.subhub.activities

import android.os.Bundle
import androidx.lifecycle.observe
import com.crazylegend.kotlinextensions.livedata.compatProvider
import com.crazylegend.subhub.R
import com.crazylegend.subhub.adapters.chooseLanguage.LanguageItem
import com.crazylegend.subhub.adapters.subtitles.SubtitlesAdapter
import com.crazylegend.subhub.consts.INTENT_MOVIE_DOWNLOAD_LOCATION_TAG
import com.crazylegend.subhub.consts.INTENT_MOVIE_LANG_TAG
import com.crazylegend.subhub.consts.INTENT_MOVIE_NAME_TAG
import com.crazylegend.subhub.core.AbstractActivity
import com.crazylegend.subhub.pickedDirs.PickedDirModel
import com.crazylegend.subhub.vmfs.LoadSubsVMF
import com.crazylegend.subhub.vms.LoadSubsVM


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
class LoadSubtitlesActivity : AbstractActivity(R.layout.activity_load_subs) {

    private var pickedDirModel: PickedDirModel? = null
    private var loadSubsVM: LoadSubsVM? = null
    private var chosenLanguage: LanguageItem? = null
    private val subtitlesAdapter by lazy {
        SubtitlesAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val movieName = intent.getStringExtra(INTENT_MOVIE_NAME_TAG)
        chosenLanguage = intent.getParcelableExtra(INTENT_MOVIE_LANG_TAG)
        pickedDirModel = intent.getParcelableExtra(INTENT_MOVIE_DOWNLOAD_LOCATION_TAG)

        if (pickedDirModel == null) {
            component.subToast.jobToast(getString(R.string.an_error_has_occurred))
            finish()
        } else {
            val dir = pickedDirModel?.dir
            dir ?: return
            movieName ?: return
            chosenLanguage ?: return
            loadSubsVM = compatProvider(LoadSubsVMF(application, movieName.toString(), chosenLanguage, dir))

            loadSubsVM?.subtitles?.observe(this) {
                subtitlesAdapter.submitList(it.toList())
            }
        }

    }
}