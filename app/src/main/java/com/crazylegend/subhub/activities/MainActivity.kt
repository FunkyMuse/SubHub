package com.crazylegend.subhub.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.core.view.doOnLayout
import androidx.lifecycle.observe
import com.crazylegend.kotlinextensions.collections.addIfNotExist
import com.crazylegend.kotlinextensions.containsAny
import com.crazylegend.kotlinextensions.context.launch
import com.crazylegend.kotlinextensions.context.shortToast
import com.crazylegend.kotlinextensions.coroutines.defaultCoroutine
import com.crazylegend.kotlinextensions.database.getSuccess
import com.crazylegend.kotlinextensions.database.handle
import com.crazylegend.kotlinextensions.gson.fromJsonTypeToken
import com.crazylegend.kotlinextensions.md5
import com.crazylegend.kotlinextensions.recyclerview.clickListeners.forItemClickListenerDSL
import com.crazylegend.kotlinextensions.storage.openDirectory
import com.crazylegend.kotlinextensions.tryOrPrint
import com.crazylegend.kotlinextensions.views.AppRater
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown
import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.subhub.R
import com.crazylegend.subhub.adapters.localVideos.LocalVideoAdapter
import com.crazylegend.subhub.adapters.localVideos.LocalVideoItem
import com.crazylegend.subhub.consts.*
import com.crazylegend.subhub.core.AbstractActivity
import com.crazylegend.subhub.listeners.onDirChosen
import com.crazylegend.subhub.pickedDirs.PickedDirModel
import com.crazylegend.subhub.utils.getSafFiles
import com.crazylegend.subhub.utils.toPickedDirModel
import com.google.gson.Gson
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.no_folders_selected_layout.view.*
import java.io.File
import java.util.*

/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
class MainActivity : AbstractActivity(R.layout.activity_main) {

    companion object {
        var onDirChosen: onDirChosen? = null
    }

    private var cachedJson: String? = null
    override val showBack: Boolean
        get() = false

    private val localVideoAdapter by lazy {
        LocalVideoAdapter()
    }

    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppRater.appLaunched(this, supportFragmentManager, 3, 0) {
            appTitle = getString(R.string.app_name)
        }

        component.setupRecycler(act_main_videos, linearLayoutManager, localVideoAdapter, true)

        act_main_adView?.doOnLayout {
            component.loadBanner(act_main_adView)
        }

        act_main_manual_search?.setOnClickListenerCooldown {
            component.showDialogManualSubtitleSearch(supportFragmentManager)
        }

        act_main_noFolders?.setOnClickListenerCooldown {
            openDirectory(PICK_DIRECTORY_REQUEST_CODE) {
                shortToast(R.string.file_manager_missing_expl)
            }
        }

        component.pickedDirVM.pickedDirs.observe(this) {
            it.handle({
                //querying DB
                component.dbResponse.handleLoading(act_main_progress, act_main_noFolders)
                act_main_videos?.gone()
            }, {
                //empty DB
                component.dbResponse.handleNoData(act_main_progress, act_main_noFolders)
                act_main_videos?.gone()
            }, { // db error
                throwable ->
                component.dbResponse.handleDBError(throwable, act_main_progress, act_main_noFolders)
                act_main_videos?.gone()
            }, {
                //Success
                component.dbResponse.hideLoadingOnly(act_main_progress)
                handleSuccess(this)
            })
        }



        component.pickedDirVM.videoList.observe(this) {
            if (it.isEmpty()) {
                act_main_videos?.gone()
                act_main_noFolders?.visible()
                act_main_noFolders?.no_data_text?.text = (getString(R.string.no_videos_available))
                component.dbResponse.hideLoadingOnly(act_main_progress)
            } else {
                act_main_videos?.visible()
                act_main_noFolders?.gone()
                cacheVideos(it)
                localVideoAdapter.submitList(it)
                component.dbResponse.hideLoadingOnly(act_main_progress)
            }
        }

        component.pickedDirVM.videoListFiltered.observe(this) {
            localVideoAdapter.submitList(it)
        }

        localVideoAdapter.forItemClickListener = forItemClickListenerDSL { _, item, _ ->
            component.showDialogManualSubtitleSearch(supportFragmentManager, item.videoName)
        }

    }

    private fun cacheVideos(list: List<LocalVideoItem>) {
        if (cacheFile.exists()) {
            val listJson = Gson().toJson(list)
            if (cachedJson != listJson) {
                cacheFile.writeBytes(ByteArray(0))
                cacheFile.appendText(Gson().toJson(list))
            }
        } else {
            cacheFile.writeBytes(ByteArray(0))
            cacheFile.appendText(Gson().toJson(list))
        }
    }

    override fun onResume() {
        super.onResume()
        component.pickedDirVM.pickedDirs.getSuccess?.apply {
            handleSuccess(this)
        }
    }

    private val cacheFile get() = File(cacheDir, CACHE_JSON)

    private fun handleSuccess(list: List<PickedDirModel>) {
        if (list.isEmpty()) {
            with(cacheFile) {
                if (exists()) delete()
            }
            act_main_noFolders?.no_data_text?.text = (getString(R.string.no_folders_expl))
            component.pickedDirVM.postVideos(mutableListOf())
        } else {
            component.dbResponse.handleLoading(act_main_progress, act_main_noFolders)

            if (cacheFile.exists()) {
                cachedJson = cacheFile.readText()
                val cachedAdapterList = Gson().fromJsonTypeToken<List<LocalVideoItem>>(cachedJson
                        ?: "")
                if (!cachedAdapterList.isNullOrEmpty()) {
                    act_main_videos?.visible()
                    act_main_noFolders?.gone()
                    component.dbResponse.hideLoadingOnly(act_main_progress)
                    localVideoAdapter.submitList(cachedAdapterList)
                }
            }

            updateFoldersInTheBackground(list)
        }
    }

    private fun updateFoldersInTheBackground(list: List<PickedDirModel>) {

        defaultCoroutine {
            tryOrPrint {
                val adapterList = mutableListOf<LocalVideoItem>()
                list.forEach {
                    val pickedDir = it.pickedDir(this) ?: return@tryOrPrint
                    getSafFiles(arrayOf(pickedDir)) { documentFile ->
                        if (documentFile.type.toString().toLowerCase(Locale.ROOT).containsAny(*SUPPORTED_FILE_FORMATS)) {
                            adapterList.addIfNotExist(LocalVideoItem(documentFile.uri.toString(), documentFile.name, documentFile.length(), "${documentFile.name.toString()}${documentFile.length()}".md5))
                        }
                    }
                }
                component.pickedDirVM.postVideos(adapterList)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)

        val searchItem = menu?.findItem(R.id.mam_search)
        val folders = menu?.findItem(R.id.mam_folders)
        component.pickedDirVM.pickedDirs.observe(this) {
            it.handle({
                //querying DB

            }, {
                //empty DB
                searchItem?.isVisible = false
                folders?.isVisible = false
            }, { // db error
                throwable ->
                throwable.printStackTrace()
                searchItem?.isVisible = false
                folders?.isVisible = false
            }, {
                //Success
                searchItem?.isVisible = !isNullOrEmpty()
                folders?.isVisible = !isNullOrEmpty()
            })
        }

        searchView = searchItem?.actionView as? SearchView
        searchView?.queryHint = getString(R.string.search_by_name)

        searchView?.queryTextChanges()?.skipInitialValue()?.debounce(DEBOUNCE_TIME, DEBOUNCE_TIME_UNIT)
                ?.map { it.toString() }
                ?.subscribe {
                    component.pickedDirVM.filterVideoList(it.toString())
                }?.addTo(component.compositeDisposable)

        return super.onCreateOptionsMenu(menu)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_DIRECTORY_REQUEST_CODE -> {
                    val dirModel = data?.toPickedDirModel(this)
                    dirModel ?: return
                    component.pickedDirVM.insertDir(dirModel)
                }

                PICK_DOWNLOAD_DIRECTORY_REQUEST_CODE -> {
                    val dirModel = data?.toPickedDirModel(this)
                    dirModel ?: return
                    onDirChosen?.forDirectory(dirModel)
                }
            }
        } else {
            component.toaster.jobToast(getString(R.string.operation_cancelled))
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.mam_settings -> {
                launch<SettingsActivity>()
                true
            }

            R.id.mam_folders -> {
                launch<FolderSourcesActivity>()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

}


