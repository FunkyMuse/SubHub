package com.crazylegend.subhub.core

import android.R
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.crazylegend.subhub.di.core.CoreComponentImpl


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
abstract class AbstractActivity(contentLayoutId: Int) : AppCompatActivity(contentLayoutId) {

    val component by lazy {
        CoreComponentImpl(application)
    }

    override fun onDestroy() {
        super.onDestroy()
        component.disposeResources()
    }

    lateinit var linearLayoutManager: LinearLayoutManager
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        linearLayoutManager = LinearLayoutManager(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                finish()
                true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

}