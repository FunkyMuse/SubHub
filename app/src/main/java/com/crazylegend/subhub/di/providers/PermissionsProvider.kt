package com.crazylegend.subhub.di.providers

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import javax.inject.Inject


/**
 * Created by crazy on 4/24/20 to long live and prosper !
 */

class PermissionsProvider @Inject constructor(private val fragment: Fragment, private val savedStateHandle: Bundle?) {


    fun askForAPermission(onDenied: () -> Unit = {}, onPermissionsGranted: () -> Unit = {}) =
            fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {
                    onPermissionsGranted()
                } else {
                    onDenied()
                }
            }


    fun openDocumentTree(onCalled: (Uri?) -> Unit = {}, onDenied: () -> Unit = {}, onPermissionsGranted: (Uri) -> Unit = {}) = fragment.registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
        onCalled(it)
        if (it != null) {
            onPermissionsGranted(it)
        } else {
            onDenied()
        }
    }
}