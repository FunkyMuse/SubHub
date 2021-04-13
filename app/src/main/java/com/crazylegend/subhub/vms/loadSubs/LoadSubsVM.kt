package com.crazylegend.subhub.vms.loadSubs

import android.annotation.SuppressLint
import android.app.Application
import android.net.Uri
import android.os.Build
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.crazylegend.coroutines.ioDispatcher
import com.crazylegend.gson.fromJson
import com.crazylegend.kotlinextensions.inputstream.readTextAndClose
import com.crazylegend.kotlinextensions.livedata.context
import com.crazylegend.kotlinextensions.safeOffer
import com.crazylegend.kotlinextensions.tryOrElse
import com.crazylegend.retrofit.retrofitResult.RetrofitResult
import com.crazylegend.subhub.consts.CHARSETS_FILE_CONST
import com.crazylegend.subhub.consts.DEFAULT_LANGUAGE
import com.crazylegend.subhub.consts.DEFAULT_LANGUAGE_FULL_NAME
import com.crazylegend.subhub.consts.SRT_TYPE
import com.crazylegend.subhub.core.AbstractAVM
import com.crazylegend.subhub.dtos.LanguageItem
import com.funkymuse.opensubs.OpenSubtitleItem
import com.funkymuse.opensubs.OpenSubtitlesUrl
import com.funkymuse.opensubshiltsealed.OpenSubtitleSealedHiltRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.mozilla.universalchardet.UniversalDetector
import java.io.File
import java.nio.charset.Charset
import javax.inject.Inject


/**
 * Created by crazy on 11/27/19 to long live and prosper !
 */

/**
 * Template created by Hristijan to live long and prosper.
 */

@HiltViewModel
class LoadSubsVM @Inject constructor(
        private val savedStateHandle: SavedStateHandle,
        application: Application,
        gson: Gson,
        private val openSubtitleRepository: OpenSubtitleSealedHiltRepository,
) : AbstractAVM(application) {

    private val movieName get() = savedStateHandle.get<String>("movieName")!!
    private val dir get() = savedStateHandle.get<Uri?>("pickedDir")!!
    private val languageItem get() = savedStateHandle.get<LanguageItem>("languageItem")!!
    private val langCode get() = languageItem.code ?: DEFAULT_LANGUAGE
    private val langName get() = languageItem.name ?: DEFAULT_LANGUAGE_FULL_NAME


    private val jsonAssetString =
            application.resources.assets.open(CHARSETS_FILE_CONST).readTextAndClose()
    private val charsetList = gson.fromJson<List<String>>(jsonAssetString).sortedBy { it }

    private val subtitlesData: MutableStateFlow<RetrofitResult<List<OpenSubtitleItem>?>> = MutableStateFlow(RetrofitResult.EmptyData)
    val subtitles: StateFlow<RetrofitResult<List<OpenSubtitleItem>?>> = subtitlesData

    private val downloadingData = Channel<RetrofitResult<Any>>(Channel.BUFFERED)
    val downloading = downloadingData.receiveAsFlow()

    init {
        getInitialSubtitles()
    }

    private fun getInitialSubtitles() {
        subtitlesData.value = RetrofitResult.Loading
        viewModelScope.launch(ioDispatcher) {
            subtitlesData.value = openSubtitleRepository.search(OpenSubtitlesUrl(query = movieName, subLanguageId = langCode))
        }
    }

    fun downloadSRT(item: OpenSubtitleItem) {
        val srtLocation = File(context.cacheDir, "${item.languageName}-${item.subFileName}")

        val subtitleUri = Uri.fromFile(srtLocation)
        viewModelScope.launch(ioDispatcher) {
            downloadingData.safeOffer(RetrofitResult.Loading)
            try {
                openSubtitleRepository.downloadSubtitle(context, item, subtitleUri)
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
                downloadingData.safeOffer(RetrofitResult.Error(throwable))
            }
            try {
                modifyFile(srtLocation)
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
                downloadingData.safeOffer(RetrofitResult.Error(throwable))
            }
        }
    }


    private fun modifyFile(srtLocation: File) {

        if (srtLocation.name.endsWith(SRT_TYPE)) {

            val charset = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                checkIfValueIsInAssetsList(langName) ?: tryOrElse(Charsets.UTF_8) {
                    val detector = UniversalDetector.detectCharset(srtLocation)
                    Charset.forName(detector) ?: checkIfValueIsInAssetsListOrElse(langName)
                }
            } else {
                checkIfValueIsInAssetsListOrElse(langName)
            }

            try {
                modifySrt(charset, srtLocation)
                downloadingData.safeOffer(RetrofitResult.Success(Any()))

            } catch (throwable: Throwable) {
                throwable.printStackTrace()
                downloadingData.safeOffer(RetrofitResult.Error(throwable))
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun checkIfValueIsInAssetsListOrElse(languageFullName: String): Charset {
        val languageExist = charsetList.find {
            it.split(" ")[0].equals(languageFullName, ignoreCase = true)
        }

        return if (languageExist != null) {
            val charset = languageExist.split(" ")[1]
            Charset.forName(charset)
        } else {
            Charsets.UTF_8
        }
    }

    @SuppressLint("DefaultLocale")
    private fun checkIfValueIsInAssetsList(languageFullName: String): Charset? {
        val languageExist = charsetList.find {
            it.split(" ")[0].equals(languageFullName, ignoreCase = true)
        }

        return if (languageExist != null) {
            val charset = languageExist.split(" ")[1]
            Charset.forName(charset)
        } else {
            null
        }
    }

    private fun modifySrt(charset: Charset, file: File) {

        val text = file.readLines(charset)
        file.writeBytes(ByteArray(0))
        text.map {
            String(it.toByteArray(), Charsets.UTF_8)
        }.forEach {
            file.appendText(it, Charsets.UTF_8)
            file.appendText("\n")
        }
        text.forEach {
            val testText = String(it.toByteArray(), Charsets.UTF_8)
            file.appendText(testText, Charsets.UTF_8)
        }

        val createFile = DocumentFile.fromTreeUri(context, dir)?.createFile(SRT_TYPE, file.name)
        val uriTowRite = createFile?.uri ?: return
        context.contentResolver.openOutputStream(uriTowRite).use {
            it?.write(file.readBytes())
        }
        file.delete()
    }
}