package com.crazylegend.subhub

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.documentfile.provider.DocumentFile
import com.crazylegend.kotlinextensions.intent.openFile
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.kotlinextensions.storage.openDirectory
import com.crazylegend.kotlinextensions.storage.openDocument
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openDirectory(1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                1 -> {
                    val uri = data?.data
                    debug("URI ${uri.toString()}")

                    uri?.let {

                        val treefile = DocumentFile.fromTreeUri(this, it)

                        treefile?.listFiles()?.forEach {
                            debug("FILE ${it.name} ${it.type} ${it.length()}")
                        }
                    }
                }
            }
        }
    }
}
