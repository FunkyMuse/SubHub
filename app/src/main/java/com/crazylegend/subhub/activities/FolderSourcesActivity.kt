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
import com.crazylegend.kotlinextensions.viewBinding.viewBinding
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown
import com.crazylegend.subhub.R
import com.crazylegend.subhub.adapters.folderSources.PickedDirsAdapter
import com.crazylegend.subhub.consts.PICK_DIRECTORY_REQUEST_CODE
import com.crazylegend.subhub.core.AbstractActivity
import com.crazylegend.subhub.databinding.ActivityFolderSourcesBinding
import com.crazylegend.subhub.utils.ButtonClicked
import com.crazylegend.subhub.utils.isSnackbarFolderSourcesShown
import com.crazylegend.subhub.utils.setSnackShown
import com.crazylegend.subhub.utils.toPickedDirModel
import com.google.android.material.snackbar.Snackbar


/**
 * Created by crazy on 11/28/19 to long live and prosper !
 */
class FolderSourcesActivity : AbstractActivity() {

    override val showBack: Boolean
        get() = true

    override val binding by viewBinding(ActivityFolderSourcesBinding::inflate)

    private val adapter by lazy {
        PickedDirsAdapter(lifecycleScope)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component.loadBanner(binding.adView)

        if (!isSnackbarFolderSourcesShown) {
            snackBar(getString(R.string.folder_source_hold_expl), getString(R.string.okay), Snackbar.LENGTH_INDEFINITE) {
                setSnackShown()
            }
        }

        component.setupRecycler(binding.folderSources, linearLayoutManager, adapter, true)
        binding.add.setOnClickListenerCooldown {
            openDirectory(PICK_DIRECTORY_REQUEST_CODE)
        }

        component.pickedDirVM.pickedDirs.observe(this) {
            component.dbResponse.handleDBCall(it, binding.progress, binding.noSourcesLayout.root,
                    adapter)
        }

        adapter.forItemClickListener = forItemClickListenerDSL { _, item, _ ->
            val intent = Intent(Intent.ACTION_VIEW)
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