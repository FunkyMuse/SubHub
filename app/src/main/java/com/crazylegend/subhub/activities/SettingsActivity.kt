package com.crazylegend.subhub.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.crazylegend.kotlinextensions.activity.replace
import com.crazylegend.subhub.R
import com.crazylegend.subhub.consts.PICK_DOWNLOAD_DIRECTORY_REQUEST_CODE
import com.crazylegend.subhub.core.AbstractActivity
import com.crazylegend.subhub.fragments.SettingsFragment
import com.crazylegend.subhub.listeners.onDirChosen
import com.crazylegend.subhub.utils.toPickedDirModel


/**
 * Created by crazy on 11/28/19 to long live and prosper !
 */
class SettingsActivity : AbstractActivity(R.layout.activity_settings) {

    override val showBack: Boolean
        get() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.replace(SettingsFragment(), R.id.act_settings_fragment_container)
    }

    companion object {
        var onDirChosen: onDirChosen? = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_DOWNLOAD_DIRECTORY_REQUEST_CODE -> {
                    val dirModel = data?.toPickedDirModel(this)
                    dirModel ?: return
                    component.addDownloadLocationToPrefs(dirModel)
                    onDirChosen?.forDirectory(dirModel)
                }
            }
        } else {
            component.toaster.jobToast(getString(R.string.operation_cancelled))
        }
    }
}