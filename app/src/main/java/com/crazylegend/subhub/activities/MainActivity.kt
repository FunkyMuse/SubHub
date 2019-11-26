package com.crazylegend.subhub.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.documentfile.provider.DocumentFile
import com.crazylegend.kotlinextensions.activity.remove
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.kotlinextensions.storage.openDirectory
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown
import com.crazylegend.subhub.R
import com.crazylegend.subhub.consts.DIALOG_MANUAL_SEARCH_TAG
import com.crazylegend.subhub.core.AbstractActivity
import com.crazylegend.subhub.dialogs.DialogManualSubtitleSearch
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
class MainActivity : AbstractActivity(R.layout.activity_main) {

    private lateinit var dialogManualSubtitleSearch: DialogManualSubtitleSearch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        act_main_manual_search?.setOnClickListenerCooldown {
            showDialogManualSubtitleSearch()
        }

        act_main_noFolders?.setOnClickListenerCooldown {
            openDirectory(1)
        }
    }

    private fun showDialogManualSubtitleSearch() {
        supportFragmentManager.findFragmentByTag(DIALOG_MANUAL_SEARCH_TAG)?.remove()
        dialogManualSubtitleSearch = DialogManualSubtitleSearch()
        dialogManualSubtitleSearch.show(supportFragmentManager, DIALOG_MANUAL_SEARCH_TAG)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                1 -> {
                    val uri = data?.data ?: return

                    debug("URI ${uri.toString().substringAfterLast("/")}")

                    val treefile = DocumentFile.fromTreeUri(this, uri)
                    treefile?.listFiles()?.forEach {
                        debug("FILE ${it.name} ${it.type}")
                    }
                }
            }
        }
    }
}

