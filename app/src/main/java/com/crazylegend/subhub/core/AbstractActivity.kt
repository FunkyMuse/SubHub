package com.crazylegend.subhub.core

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.crazylegend.kotlinextensions.context.showBackButton
import com.crazylegend.subhub.di.activity.ActivityComponentImpl
import com.crazylegend.subhub.di.core.CoreComponentImpl


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

    lateinit var linearLayoutManager: LinearLayoutManager
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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