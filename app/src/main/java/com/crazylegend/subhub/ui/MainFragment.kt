package com.crazylegend.subhub.ui

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.coroutines.textChanges
import com.crazylegend.kotlinextensions.collections.isNotNullOrEmpty
import com.crazylegend.kotlinextensions.context.isPermissionGranted
import com.crazylegend.kotlinextensions.lifeCycle.repeatingJobOnStarted
import com.crazylegend.kotlinextensions.tryOrIgnore
import com.crazylegend.kotlinextensions.views.*
import com.crazylegend.recyclerview.clickListeners.forItemClickListener
import com.crazylegend.recyclerview.isEmpty
import com.crazylegend.recyclerview.registerDataObserver
import com.crazylegend.subhub.R
import com.crazylegend.subhub.consts.DEBOUNCE_TIME
import com.crazylegend.subhub.consts.ON_UPDATE_KEY
import com.crazylegend.subhub.consts.UPDATE_REQ_KEY
import com.crazylegend.subhub.core.AbstractFragment
import com.crazylegend.subhub.databinding.FragmentMainBinding
import com.crazylegend.subhub.di.providers.AdaptersProvider
import com.crazylegend.subhub.di.providers.PermissionsProvider
import com.crazylegend.subhub.utils.isSnackbarFolderSourcesShown
import com.crazylegend.subhub.utils.setSnackShown
import com.crazylegend.subhub.vms.VideosVM
import com.crazylegend.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import javax.inject.Inject


/**
 * Created by crazy on 5/23/20 to long live and prosper !
 */
@AndroidEntryPoint
class MainFragment : AbstractFragment(R.layout.fragment_main) {
    private var dataObserver: RecyclerView.AdapterDataObserver? = null

    override val binding by viewBinding(FragmentMainBinding::bind) {
        dataObserver?.let { adapter.unregisterAdapterDataObserver(it) }
    }

    @Inject
    lateinit var adaptersProvider: AdaptersProvider

    @Inject
    lateinit var permissionsProvider: PermissionsProvider

    private val videosVM by viewModels<VideosVM>()
    private val adapter by lazy {
        adaptersProvider.videosAdapter
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_fragment_menu, menu)
        val searchItem = menu.findItem(R.id.mam_search)
        videosVM.videos.observe(viewLifecycleOwner) {
            searchItem.isVisible = it.isNotNullOrEmpty
        }
        searchItem.asSearchView()?.apply {
            queryHint = getString(R.string.search_by_name)
            getEditTextSearchView?.apply {
                repeatingJobOnStarted {
                    textChanges(true, DEBOUNCE_TIME).map { it.toString() }.collect {
                        videosVM.filterVideos(it)
                    }
                }
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding.videos.adapter = adapter
        onDataChanged(emptyAction = {
            showNoData()
            showNoVideos()
        }, notEmptyAction = ::hideNoData)

        if (requireContext().isPermissionGranted(READ_EXTERNAL_STORAGE)) {
            videosVM.loadVideos()
        } else {
            if (!requireContext().isSnackbarFolderSourcesShown) {
                tryOrIgnore {
                    findNavController().navigate(MainFragmentDirections.actionConfirmation(getString(R.string.permission_expl), getString(R.string.cancel), getString(R.string.grant)))
                }
            } else {
                setOnNoFoldersClick()
                if (adapter.isEmpty) {
                    showNoData()
                } else {
                    hideNoData()
                }
            }
        }

        setFragmentResultListener(UPDATE_REQ_KEY) { _, bundle ->
            requireContext().setSnackShown()
            if (bundle.getBoolean(ON_UPDATE_KEY, false)) {
                getStoragePermissions()
            } else {
                setupUIIfPermissionIsNotAllowed()
            }
        }

        adapter.forItemClickListener = forItemClickListener { _, item, _ ->
            tryOrIgnore {
                findNavController().navigate(MainFragmentDirections.actionManualSearch(item.displayName))
            }
        }

        binding.manualSearch.setOnClickListenerCooldown {
            tryOrIgnore { findNavController().navigate(MainFragmentDirections.actionManualSearch()) }
        }

        videosVM.videos.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                showNoData()
                showNoVideos()
                adapter.submitList(emptyList())
            } else {
                adapter.submitList(it)
                hideNoData()
            }
        }

        repeatingJobOnStarted {
            videosVM.loadingIndicator.collect {
                binding.progress.isVisible = it
            }
        }

        videosVM.filteredVideos.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        permissionsProvider.permissionListener(onDenied = {
            setupUIIfPermissionIsNotAllowed()
        }, onGranted = {
            videosVM.loadVideos()
        })
    }

    private fun setupUIIfPermissionIsNotAllowed() {
        binding.videos.gone()
        binding.noFolders.root.visible()
        setOnNoFoldersClick()
    }

    private fun showNoVideos() {
        binding.noFolders.noDataText.text = getString(R.string.no_videos_available_locally)
    }

    private fun setOnNoFoldersClick() {
        binding.noFolders.root.setOnClickListenerCooldown {
            getStoragePermissions()
        }
    }

    private inline fun onDataChanged(crossinline emptyAction: () -> Unit, crossinline notEmptyAction: () -> Unit) {
        dataObserver = adapter.registerDataObserver {
            if (adapter.isEmpty) {
                emptyAction()
            } else {
                notEmptyAction()
            }
        }
    }

    private fun getStoragePermissions() = permissionsProvider.askForAPermission(READ_EXTERNAL_STORAGE)


    private fun showNoData() {
        binding.videos.gone()
        binding.noFolders.root.visible()
        binding.noFolders.noDataText.text = getString(R.string.permission_expl2)
    }

    private fun hideNoData() {
        binding.videos.visible()
        binding.noFolders.root.gone()
    }
}