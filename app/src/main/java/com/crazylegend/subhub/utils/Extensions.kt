package com.crazylegend.subhub.utils

import android.content.Context
import android.content.Intent
import androidx.documentfile.provider.DocumentFile
import com.crazylegend.kotlinextensions.containsAny
import com.crazylegend.subhub.consts.SUPPORTED_FILE_FORMATS
import com.crazylegend.subhub.pickedDirs.PickedDirModel
import com.mopub.mobileads.MoPubErrorCode
import com.mopub.mobileads.MoPubInterstitial
import java.util.*


/**
 * Created by crazy on 11/27/19 to long live and prosper !
 */

fun Intent?.toPickedDirModel(context: Context): PickedDirModel? {
    val uri = this?.data ?: return null
    val pickedDir = DocumentFile.fromTreeUri(context, uri)
    pickedDir ?: return null
    return PickedDirModel(pickedDir.name.toString(), pickedDir.uri.toString())
}


fun getSafFiles(files: Array<DocumentFile>?, onFileCallback: (documentFile: DocumentFile) -> Unit = {}) {
    files?.apply {
        for (file in files) {
            if (file.isDirectory) {
                getSafFiles(file.listFiles(), onFileCallback)
            } else {
                onFileCallback.invoke(file)
            }
        }
    }
}

fun countSafVideoFiles(files: Array<DocumentFile?>, shouldIncrement: () -> Unit = {}) {
    files.apply {
        for (file in files) {
            file ?: return
            if (file.isDirectory) {
                countSafVideoFiles(file.listFiles(), shouldIncrement)
            } else {
                if (file.type.toString().toLowerCase(Locale.ROOT).containsAny(*SUPPORTED_FILE_FORMATS)) {
                    shouldIncrement.invoke()
                }
            }
        }
    }
}

fun interstitialListener() = object : MoPubInterstitial.InterstitialAdListener {
    override fun onInterstitialLoaded(interstitial: MoPubInterstitial?) {
        interstitial?.show()
        //Log.i("interstitialListener", "onInterstitialLoaded")
    }

    override fun onInterstitialShown(interstitial: MoPubInterstitial?) {
        // Log.i("interstitialListener", "onInterstitialShown")
    }

    override fun onInterstitialFailed(interstitial: MoPubInterstitial?, errorCode: MoPubErrorCode?) {
        // Log.i("interstitialListener", "onInterstitialFailed")
    }

    override fun onInterstitialDismissed(interstitial: MoPubInterstitial?) {
        // Log.i("interstitialListener", "onInterstitialDismissed")
    }

    override fun onInterstitialClicked(interstitial: MoPubInterstitial?) {
        // Log.i("interstitialListener", "onInterstitialClicked")
    }
}