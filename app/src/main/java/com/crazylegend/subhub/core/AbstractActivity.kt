package com.crazylegend.subhub.core

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.crazylegend.kotlinextensions.context.showBackButton
import com.crazylegend.subhub.BuildConfig
import com.crazylegend.subhub.consts.UNITY_GAME_ID
import com.crazylegend.subhub.di.activity.ActivityComponentImpl
import com.crazylegend.subhub.di.core.CoreComponentImpl
import com.mopub.common.MoPub
import com.unity3d.ads.UnityAds


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
abstract class AbstractActivity(contentLayoutId: Int) : AppCompatActivity(contentLayoutId) {

    abstract val showBack: Boolean

    val component by lazy {
        ActivityComponentImpl(this, CoreComponentImpl(application))
    }

    override fun onDestroy() {
        super.onDestroy()
        component.disposeResources()
    }

    override fun onPause() {
        super.onPause()
        MoPub.onPause(this)
    }

    override fun onResume() {
        super.onResume()
        MoPub.onResume(this)
    }


    override fun onStop() {
        super.onStop()
        MoPub.onStop(this)
    }

    lateinit var linearLayoutManager: LinearLayoutManager
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UnityAds.initialize(this, UNITY_GAME_ID, BuildConfig.DEBUG)
        MoPub.onCreate(this)

        if (showBack) {
            showBackButton()
        }
        linearLayoutManager = LinearLayoutManager(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

}