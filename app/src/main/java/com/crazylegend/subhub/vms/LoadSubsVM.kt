package com.crazylegend.subhub.vms

import android.app.Application
import android.net.Uri
import android.os.Build
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crazylegend.kotlinextensions.gson.fromJson
import com.crazylegend.kotlinextensions.inputstream.readTextAndClose
import com.crazylegend.kotlinextensions.livedata.SingleEvent
import com.crazylegend.kotlinextensions.livedata.context
import com.crazylegend.kotlinextensions.rx.ioThreadScheduler
import com.crazylegend.kotlinextensions.rx.mainThreadScheduler
import com.crazylegend.kotlinextensions.rx.newThreadScheduler
import com.crazylegend.kotlinextensions.rx.singleFrom
import com.crazylegend.kotlinextensions.tryOrElse
import com.crazylegend.subhub.R
import com.crazylegend.subhub.adapters.chooseLanguage.LanguageItem
import com.crazylegend.subhub.consts.CHARSETS_FILE_CONST
import com.crazylegend.subhub.consts.DEFAULT_LANGUAGE
import com.crazylegend.subhub.consts.SRT_TYPE
import com.crazylegend.subhub.core.AbstractAVM
import com.masterwok.opensubs.OpenSubtitlesUrlBuilder
import com.masterwok.opensubs.models.OpenSubtitleItem
import com.masterwok.opensubs.services.OpenSubtitlesService
import io.reactivex.rxkotlin.addTo
import org.mozilla.universalchardet.UniversalDetector
import java.io.File
import java.nio.charset.Charset


/**
 * Created by crazy on 11/27/19 to long live and prosper !
 */

/**
 * Template created by Hristijan to live long and prosper.
 */

class LoadSubsVM(
        application: Application,
        private val movieName: String,
        private val langCode: LanguageItem,
        private val dir: Uri
) : AbstractAVM(application) {

    private val jsonAssetString =
            application.resources.assets.open(CHARSETS_FILE_CONST).readTextAndClose()
    private val charsetList = component.gson.fromJson<List<String>>(jsonAssetString).sortedBy { it }

    private val service = OpenSubtitlesService()

    private val subtitleData: MutableLiveData<Array<OpenSubtitleItem>> = MutableLiveData()
    val subtitles: LiveData<Array<OpenSubtitleItem>> = subtitleData

    private val successEventData: MutableLiveData<SingleEvent<Boolean>> = MutableLiveData()
    val successEvent: LiveData<SingleEvent<Boolean>> get() = successEventData


    private val loadingEventData: MutableLiveData<SingleEvent<Boolean>> = MutableLiveData()
    val loadingEvent: LiveData<SingleEvent<Boolean>> get() = loadingEventData

    init {
        loadSubtitles()
    }

    private fun loadSubtitles() {
        loadingEventData.value = SingleEvent(true)

        val url = OpenSubtitlesUrlBuilder()
                .query(movieName)
                .subLanguageId(langCode.code ?: DEFAULT_LANGUAGE)
                .build()

        service.searchSingle(OpenSubtitlesService.TemporaryUserAgent, url)
                .subscribeOn(ioThreadScheduler)
                .observeOn(mainThreadScheduler)
                .subscribe({
                    subtitleData.value = it
                    loadingEventData.value = SingleEvent(false)
                }, {
                    it.printStackTrace()
                    loadingEventData.value = SingleEvent(false)
                }).addTo(component.compositeDisposable)

    }


    fun downloadSRT(item: OpenSubtitleItem) {
        val srtLocation = File(context.cacheDir, "${item.LanguageName}-${item.SubFileName}")
        val subtitleUri = Uri.fromFile(srtLocation)
        service.downloadSubtitleCompletable(context, item, subtitleUri)
                .subscribeOn(ioThreadScheduler)
                .observeOn(mainThreadScheduler)
                .subscribe({
                    modifyFile(srtLocation)
                }, {
                    it.printStackTrace()
                    successEventData.value = SingleEvent(false)
                }).addTo(component.compositeDisposable)

    }

    private fun modifyFile(srtLocation: File) {
        loadingEventData.value = SingleEvent(true)
        component.toaster.jobToast(context.getString(R.string.obtaining_sub_file))
        if (srtLocation.name.endsWith(SRT_TYPE)) {

            val charset = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                tryOrElse(Charsets.UTF_8) {
                    val detector = UniversalDetector.detectCharset(srtLocation)
                    Charset.forName(detector) ?: checkIfValueIsInAssetsListOrElse(langCode.name
                            ?: "English")
                }
            } else {
                checkIfValueIsInAssetsListOrElse(langCode.name ?: "English")
            }

            singleFrom {
                modifyFile(charset, srtLocation)
            }.subscribeOn(newThreadScheduler)
                    .observeOn(mainThreadScheduler)
                    .subscribe({
                        successEventData.value = SingleEvent(true)
                    }, {
                        it.printStackTrace()
                        successEventData.value = SingleEvent(false)
                    }).addTo(component.compositeDisposable)
        }
    }

    private fun modifyFile(charset: Charset, file: File) {

        val text = file.readLines(charset)
        file.outputStream().use {
            it.write(ByteArray(0))
        }
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

    private fun checkIfValueIsInAssetsListOrElse(languageFullName: String): Charset {
        val languageExist = charsetList.find {
            it.split(" ")[0].toLowerCase() == languageFullName.toLowerCase()
        }

        return if (languageExist != null) {
            val charset = languageExist.split(" ")[1]
            Charset.forName(charset)
        } else {
            Charsets.UTF_8
        }
    }


}