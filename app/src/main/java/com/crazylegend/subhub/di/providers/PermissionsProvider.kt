package com.crazylegend.subhub.di.providers

import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.crazylegend.kotlinextensions.fragments.fragmentBooleanResult
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject


/**
 * Created by crazy on 4/24/20 to long live and prosper !
 */

@FragmentScoped
class PermissionsProvider @Inject constructor(private val fragment: Fragment) {

    companion object {
        const val PERMISSION_REQUEST_KEY = "resultRequestKey_perm"
        const val BOOLEAN_RESULT_KEY = "boolean_res_perm"


        const val TREE_REQUEST_KEY = "resultRequestKey_tree"
        const val URI_RESULT_KEY = "uri_res_perm"
    }

    private val permissionRequester = fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        fragment.setFragmentResult(PERMISSION_REQUEST_KEY, bundleOf(BOOLEAN_RESULT_KEY to it))
    }

    fun askForAPermission(permission: String) {
        permissionRequester.launch(permission)
    }

    fun permissionListener(onDenied: () -> Unit, onGranted: () -> Unit) {
        fragment.fragmentBooleanResult(PERMISSION_REQUEST_KEY, BOOLEAN_RESULT_KEY, onDenied, onGranted)
    }

    private val documentTreeRequester = fragment.registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
        fragment.setFragmentResult(TREE_REQUEST_KEY, bundleOf(URI_RESULT_KEY to it))
    }

    fun openDocumentTree() {
        documentTreeRequester.launch(null)
    }

    fun documentTreeListener(onTreeURI: Uri?.() -> Unit) {
        fragment.setFragmentResultListener(TREE_REQUEST_KEY) { _, bundle ->
            val treeUri: Uri? = bundle.getParcelable(URI_RESULT_KEY)
            treeUri.onTreeURI()
        }
    }

}