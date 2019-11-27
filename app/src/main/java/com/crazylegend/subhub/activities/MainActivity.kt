package com.crazylegend.subhub.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.observe
import com.crazylegend.kotlinextensions.activity.remove
import com.crazylegend.kotlinextensions.containsAny
import com.crazylegend.kotlinextensions.database.handle
import com.crazylegend.kotlinextensions.delegates.activityVM
import com.crazylegend.kotlinextensions.isNotNullOrEmpty
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.kotlinextensions.recyclerview.clickListeners.forItemClickListenerDSL
import com.crazylegend.kotlinextensions.storage.openDirectory
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown
import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.subhub.R
import com.crazylegend.subhub.adapters.localVideos.LocalVideoAdapter
import com.crazylegend.subhub.adapters.localVideos.LocalVideoItem
import com.crazylegend.subhub.consts.*
import com.crazylegend.subhub.core.AbstractActivity
import com.crazylegend.subhub.dialogs.DialogManualSubtitleSearch
import com.crazylegend.subhub.listeners.onDirChosen
import com.crazylegend.subhub.pickedDirs.PickedDirModel
import com.crazylegend.subhub.utils.toPickedDirModel
import com.crazylegend.subhub.vms.PickedDirVM
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
class MainActivity : AbstractActivity(R.layout.activity_main) {

    companion object {
        var onDirChosen: onDirChosen? = null
    }

    private lateinit var dialogManualSubtitleSearch: DialogManualSubtitleSearch

    private val localVideoAdapter by lazy {
        LocalVideoAdapter()
    }

    private val pickedDirVM by activityVM<PickedDirVM>()
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component.setupRecycler(act_main_videos, linearLayoutManager, localVideoAdapter, true)

        act_main_manual_search?.setOnClickListenerCooldown {
            showDialogManualSubtitleSearch()
        }

        act_main_noFolders?.setOnClickListenerCooldown {
            openDirectory(PICK_DIRECTORY_REQUEST_CODE)
        }

        pickedDirVM.pickedDirs.observe(this) {
            it.handle({
                //querying DB
                act_main_videos?.gone()
                act_main_noFolders?.visible()
                act_main_progress?.visible()
            }, {
                //empty DB
                act_main_progress?.gone()
                act_main_videos?.gone()
                act_main_noFolders?.visible()
            }, { // db error
                throwable ->
                act_main_videos?.gone()
                act_main_noFolders?.visible()
                act_main_progress?.gone()
                throwable.printStackTrace()
            }, {
                //Success
                act_main_progress?.gone()
                component.dbResponse.hideLoadingOnly(act_main_progress)
                handleSuccess(this)
            })
        }


        pickedDirVM.videoList.observe(this) {
            if (it.isEmpty()) {
                act_main_videos?.gone()
                act_main_noFolders?.visible()
            } else {
                act_main_noFolders?.gone()
                act_main_videos?.visible()
                localVideoAdapter.submitList(it)
            }
        }

        pickedDirVM.videoListFiltered.observe(this) {
            localVideoAdapter.submitList(it)
        }

        localVideoAdapter.forItemClickListener = forItemClickListenerDSL { _, item, _ ->
            showDialogManualSubtitleSearch(item.videoName)
        }
    }

    private fun handleSuccess(list: List<PickedDirModel>) {
        if (list.isEmpty()) {
            pickedDirVM.postVideos(mutableListOf())
        } else {
            val adapterList = mutableListOf<LocalVideoItem>()
            list.forEach {
                val pickedDir = it.pickedDir(this) ?: return

                val filesList = pickedDir.listFiles().map { documentFile ->
                    debug("FILE NAME ${documentFile.name} ${documentFile.type}")
                    LocalVideoItem(documentFile.uri, documentFile.name, documentFile.type, documentFile.length())
                }.filter {
                    it.videoType.toString().toLowerCase(Locale.ROOT).containsAny(*SUPPORTED_FILE_FORMATS)
                }
                adapterList.addAll(filesList)
            }
            pickedDirVM.postVideos(adapterList)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)

        val searchItem = menu?.findItem(R.id.mam_search)
        val folders = menu?.findItem(R.id.mam_folders)
        pickedDirVM.videoList.observe(this) {
            searchItem?.isVisible = !it.isNullOrEmpty()
            folders?.isVisible = !it.isNullOrEmpty()
        }

        searchView = searchItem?.actionView as? SearchView

        searchView?.queryTextChanges()?.skipInitialValue()?.debounce(DEBOUNCE_TIME, DEBOUNCE_TIME_UNIT)
                ?.map { it.toString() }
                ?.subscribe {
                    pickedDirVM.filterVideoList(it.toString())
                }?.addTo(component.compositeDisposable)

        return super.onCreateOptionsMenu(menu)
    }

    private fun showDialogManualSubtitleSearch(videoName: String? = null) {
        supportFragmentManager.findFragmentByTag(DIALOG_MANUAL_SEARCH_TAG)?.remove()
        dialogManualSubtitleSearch = DialogManualSubtitleSearch()
        if (videoName.isNotNullOrEmpty()) {
            dialogManualSubtitleSearch.addArguments(videoName)
        }
        dialogManualSubtitleSearch.show(supportFragmentManager, DIALOG_MANUAL_SEARCH_TAG)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_DIRECTORY_REQUEST_CODE -> {
                    val dirModel = data?.toPickedDirModel(this)
                    dirModel ?: return
                    pickedDirVM.insertDir(dirModel)
                }

                PICK_DOWNLOAD_DIRECTORY_REQUEST_CODE -> {
                    val dirModel = data?.toPickedDirModel(this)
                    dirModel ?: return
                    onDirChosen?.forDirectory(dirModel)
                }
            }
        } else {
            component.subToast.jobToast(getString(R.string.operation_cancelled))
        }
    }


}


