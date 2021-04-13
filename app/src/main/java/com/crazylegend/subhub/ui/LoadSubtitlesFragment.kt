package com.crazylegend.subhub.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.crazylegend.kotlinextensions.lifeCycle.repeatingJobOnStarted
import com.crazylegend.kotlinextensions.toaster.Toaster
import com.crazylegend.kotlinextensions.tryOrIgnore
import com.crazylegend.recyclerview.clickListeners.forItemClickListener
import com.crazylegend.retrofit.retrofitResult.RetrofitResult
import com.crazylegend.retrofit.retrofitResult.handle
import com.crazylegend.subhub.R
import com.crazylegend.subhub.core.AbstractFragment
import com.crazylegend.subhub.databinding.FragmentLoadSubsBinding
import com.crazylegend.subhub.di.providers.AdaptersProvider
import com.crazylegend.subhub.vms.loadSubs.LoadSubsVM
import com.crazylegend.viewbinding.viewBinding
import com.funkymuse.opensubs.OpenSubtitleItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject


/**
 * Created by crazy on 5/23/20 to long live and prosper !
 */
@AndroidEntryPoint
class LoadSubtitlesFragment : AbstractFragment(R.layout.fragment_load_subs) {

    override val binding by viewBinding(FragmentLoadSubsBinding::bind)

    @Inject
    lateinit var toastProvider: Toaster

    @Inject
    lateinit var adaptersProvider: AdaptersProvider

    private val subtitlesAdapter by lazy {
        adaptersProvider.subtitlesAdapter
    }
    private val args by navArgs<LoadSubtitlesFragmentArgs>()
    private val languageItem get() = args.languageItem
    private val pickedDir get() = args.pickedDir
    private val movieName get() = args.movieName
    private val loadSubsVM by viewModels<LoadSubsVM>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.adapter = subtitlesAdapter
        if (languageItem == null || movieName.isEmpty() || pickedDir == null) {
            tryOrIgnore { findNavController().navigateUp() }
            return
        } else {
            binding.noSubsLayout.noDataText.text = getString(R.string.no_subs_loaded_expl)


            repeatingJobOnStarted {
                loadSubsVM.subtitles.collect {
                    handleApiCall(it)
                }
            }
            subtitlesAdapter.forItemClickListener = forItemClickListener { _, item, _ ->
                loadSubsVM.downloadSRT(item)
            }

            repeatingJobOnStarted {
                loadSubsVM.downloading.collect {
                    handleDownloadStatus(it)
                }
            }
        }
    }

    private fun handleDownloadStatus(it: RetrofitResult<Any>) {
        binding.progress.isVisible = it is RetrofitResult.Loading
        it.handle(
                loading = {
                    toastProvider.shortToast(R.string.obtaining_subtitle_file)
                },
                callError = { throwable ->
                    throwable.printStackTrace()
                    toastProvider.shortToast(R.string.failed_to_get_sub)
                },
                success = {
                    toastProvider.shortToast(R.string.succ_dl_sub)
                }
        )
    }

    private fun handleApiCall(it: RetrofitResult<List<OpenSubtitleItem>?>) {
        val noSubsLogic = it is RetrofitResult.EmptyData || it is RetrofitResult.Error || it is RetrofitResult.ApiError
        binding.noSubsLayout.root.isVisible = noSubsLogic
        binding.progress.isVisible = it is RetrofitResult.Loading
        if (it is RetrofitResult.Success) {
            subtitlesAdapter.submitList(it.value)
        }
    }
}