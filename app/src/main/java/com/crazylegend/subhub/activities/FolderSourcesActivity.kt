package com.crazylegend.subhub.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.crazylegend.kotlinextensions.context.isIntentResolvable
import com.crazylegend.kotlinextensions.context.snackBar
import com.crazylegend.kotlinextensions.recyclerview.clickListeners.forItemClickListenerDSL
import com.crazylegend.kotlinextensions.storage.openDirectory
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown
import com.crazylegend.kotlinextensions.views.setPrecomputedText
import com.crazylegend.subhub.R
import com.crazylegend.subhub.adapters.folderSources.PickedDirsAdapter
import com.crazylegend.subhub.consts.PICK_DIRECTORY_REQUEST_CODE
import com.crazylegend.subhub.core.AbstractActivity
import com.crazylegend.subhub.utils.ButtonClicked
import com.crazylegend.subhub.utils.isSnackbarFolderSourcesShown
import com.crazylegend.subhub.utils.setSnackShown
import com.crazylegend.subhub.utils.toPickedDirModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_folder_sources.*
import kotlinx.android.synthetic.main.no_folders_selected_layout.view.*


/**
 * Created by crazy on 11/28/19 to long live and prosper !
 */
class FolderSourcesActivity : AbstractActivity(R.layout.activity_folder_sources) {

    override val showBack: Boolean
        get() = true

    private val adapter by lazy {
        PickedDirsAdapter(lifecycleScope)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        act_fs_no_sources_layout.no_data_text?.setPrecomputedText(getString(R.string.folder_sources_no_data_expl))

        if (!isSnackbarFolderSourcesShown) {
            snackBar(getString(R.string.folder_source_hold_expl), getString(R.string.okay), Snackbar.LENGTH_INDEFINITE) {
                setSnackShown()
            }
        }

        component.setupRecycler(act_fs_folder_sources, linearLayoutManager, adapter, true)
        act_fs_add.setOnClickListenerCooldown {
            openDirectory(PICK_DIRECTORY_REQUEST_CODE)
        }

        component.pickedDirVM.pickedDirs.observe(this) {
            component.dbResponse.handleDBCall(it, act_fs_progress, act_fs_no_sources_layout, adapter)
        }

        adapter.forItemClickListener = forItemClickListenerDSL { _, item, _ ->
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setDataAndType(item.dir, "*/*")
            if (isIntentResolvable(intent))
                startActivity(intent)
            else
                component.toaster.jobToast(getString(R.string.no_apps_to_handle_action))
        }

        adapter.onLongClickListener = forItemClickListenerDSL { _, item, _ ->
            //confirmation dialog
            component.showDialogConfirmation(supportFragmentManager, component.confirmationArguments(
                    title = getString(R.string.confirm_deletion),
                    rightButtonText = getString(R.string.yes)
            )) {
                when (it) {
                    ButtonClicked.RIGHT -> {
                        component.pickedDirVM.deleteDir(item)
                    }
                }
            }
        }
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
            }
        } else {
            component.toaster.jobToast(getString(R.string.operation_cancelled))
        }
    }

}