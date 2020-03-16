package com.crazylegend.subhub.activities

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.observe
import com.crazylegend.kotlinextensions.livedata.compatProvider
import com.crazylegend.kotlinextensions.recyclerview.clickListeners.forItemClickListenerDSL
import com.crazylegend.kotlinextensions.viewBinding.viewBinding
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.subhub.R
import com.crazylegend.subhub.adapters.chooseLanguage.LanguageItem
import com.crazylegend.subhub.adapters.subtitles.SubtitlesAdapter
import com.crazylegend.subhub.consts.INTENT_MOVIE_DOWNLOAD_LOCATION_TAG
import com.crazylegend.subhub.consts.INTENT_MOVIE_LANG_TAG
import com.crazylegend.subhub.consts.INTENT_MOVIE_NAME_TAG
import com.crazylegend.subhub.core.AbstractActivity
import com.crazylegend.subhub.databinding.ActivityLoadSubsBinding
import com.crazylegend.subhub.pickedDirs.PickedDirModel
import com.crazylegend.subhub.utils.isNullStringOrEmpty
import com.crazylegend.subhub.vmfs.LoadSubsVMF
import com.crazylegend.subhub.vms.LoadSubsVM


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
class LoadSubtitlesActivity : AbstractActivity() {

    private var loadSubsVM: LoadSubsVM? = null
    private val subtitlesAdapter by lazy {
        SubtitlesAdapter()
    }

    override val binding by viewBinding(ActivityLoadSubsBinding::inflate)

    override val showBack: Boolean
        get() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.loadBanner(binding.adView)


        component.setupRecycler(binding.recycler, linearLayoutManager, subtitlesAdapter)
        val movieName = intent.getStringExtra(INTENT_MOVIE_NAME_TAG)
        val chosenLanguage = intent.getParcelableExtra<LanguageItem>(INTENT_MOVIE_LANG_TAG)
        val pickedDirModel = intent.getParcelableExtra<PickedDirModel>(INTENT_MOVIE_DOWNLOAD_LOCATION_TAG)

        if (pickedDirModel == null) {
            component.toaster.jobToast(getString(R.string.an_error_has_occurred))
            finish()
        } else {
            val dir = pickedDirModel.dir
            dir ?: return
            movieName ?: return
            chosenLanguage ?: return
            if (chosenLanguage.name.isNullStringOrEmpty()) {
                component.toaster.jobToast(getString(R.string.pick_movie_language), Toast.LENGTH_LONG)
                component.removeLanguagePref()
                finish()
            }

            if (pickedDirModel.dir.toString().isNullStringOrEmpty()) {
                component.toaster.jobToast(getString(R.string.pick_download_location), Toast.LENGTH_LONG)
                component.removeDownloadLocationPref()
                finish()
            }

            loadSubsVM = compatProvider(LoadSubsVMF(application, movieName.toString(), chosenLanguage, dir))

            loadSubsVM?.subtitles?.observe(this) {
                if (it.isNullOrEmpty()) {
                    binding.noSubsLayout.root.visible()
                    binding.noSubsLayout.noDataText.text = (getString(R.string.no_subs_loaded_expl))
                } else {
                    binding.noSubsLayout.root.gone()
                    subtitlesAdapter.submitList(it.toList())
                }
            }
            subtitlesAdapter.forItemClickListener = forItemClickListenerDSL { _, item, _ ->
                loadSubsVM?.downloadSRT(item)
            }

            loadSubsVM?.successEvent?.observe(this) {
                it.getContentIfNotHandled()?.apply {
                    component.loadInterstitialAD(this@LoadSubtitlesActivity)
                    binding.progress.gone()
                    if (this) {
                        component.toaster.jobToast(getString(R.string.succ_dl_sub))
                    } else {
                        component.toaster.jobToast(getString(R.string.failed_to_get_sub))
                    }
                }
            }
            loadSubsVM?.loadingEvent?.observe(this) {
                it.getContentIfNotHandled()?.apply {
                    if (this)
                        binding.progress.visible()
                    else
                        binding.progress.gone()

                }
            }
        }

    }
}