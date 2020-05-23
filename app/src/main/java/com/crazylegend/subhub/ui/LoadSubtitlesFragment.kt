package com.crazylegend.subhub.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.crazylegend.kotlinextensions.fragments.shortToast
import com.crazylegend.kotlinextensions.livedata.fragmentProvider
import com.crazylegend.kotlinextensions.recyclerview.clickListeners.forItemClickListenerDSL
import com.crazylegend.kotlinextensions.tryOrIgnore
import com.crazylegend.kotlinextensions.viewBinding.viewBinding
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.subhub.R
import com.crazylegend.subhub.consts.INTERSTITIAL
import com.crazylegend.subhub.core.AbstractFragment
import com.crazylegend.subhub.databinding.FragmentLoadSubsBinding
import com.crazylegend.subhub.vms.loadSubs.LoadSubsVM
import com.crazylegend.subhub.vms.loadSubs.LoadSubsVMF


/**
 * Created by crazy on 5/23/20 to long live and prosper !
 */
class LoadSubtitlesFragment : AbstractFragment(R.layout.fragment_load_subs) {
    override val binding by viewBinding(FragmentLoadSubsBinding::bind)

    private val subtitlesAdapter by lazy {
        adaptersProvider.subtitlesAdapter
    }
    private val args by navArgs<LoadSubtitlesFragmentArgs>()
    private val languageItem get() = args.languageItem
    private val pickedDir get() = args.pickedDir
    private val movieName get() = args.movieName
    private lateinit var loadSubsVM: LoadSubsVM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.adapter = subtitlesAdapter
        lifecycleProvider.loadBanner(binding.adView)
        if (languageItem == null || movieName.isEmpty() || pickedDir == null) {
            tryOrIgnore { findNavController().navigateUp() }
            return
        } else {
            loadSubsVM = fragmentProvider(LoadSubsVMF(requireActivity().application, movieName, languageItem!!, pickedDir!!))

            loadSubsVM.subtitles.observe(viewLifecycleOwner) {
                binding.noSubsLayout.root.isVisible = it.isNullOrEmpty()
                if (it.isNullOrEmpty()) {
                    binding.noSubsLayout.noDataText.text = getString(R.string.no_subs_loaded_expl)
                } else {
                    subtitlesAdapter.submitList(it.toList())
                }
            }
            subtitlesAdapter.forItemClickListener = forItemClickListenerDSL { _, item, _ ->
                loadSubsVM.downloadSRT(item)
            }

            loadSubsVM.successEvent.observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.apply {
                    lifecycleProvider.loadInterstitialAD(INTERSTITIAL)
                    binding.progress.gone()
                    if (this) {
                        shortToast(R.string.succ_dl_sub)
                    } else {
                        shortToast(R.string.failed_to_get_sub)
                    }
                }
            }

            loadSubsVM.loadingEvent.observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.apply {
                    binding.progress.isVisible = this
                }
            }
        }
    }
}