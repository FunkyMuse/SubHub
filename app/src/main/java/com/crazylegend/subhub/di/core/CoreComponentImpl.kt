package com.crazylegend.subhub.di.core

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.context.getCompatColor
import com.crazylegend.kotlinextensions.recyclerview.divider
import com.crazylegend.subhub.R
import com.crazylegend.subhub.di.dbResponse.DbResponseComponentImpl
import com.crazylegend.subhub.di.pickedDirDB.PickedDirComponentImpl
import com.crazylegend.subhub.utils.SubToast
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
class CoreComponentImpl(override val application: Application) : CoreComponent {

    override val defaultPrefs: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(application)
    }

    override val subToast by lazy { SubToast(application) }

    override val gson by lazy {
        Gson()
    }

    override val compositeDisposable by lazy {
        CompositeDisposable()
    }

    override val pickedDirComponent by lazy {
        PickedDirComponentImpl(application)
    }

    override fun setupRecycler(recyclerView: RecyclerView, layoutmanager: RecyclerView.LayoutManager, listAdapter: ListAdapter<*, *>, addDivider: Boolean) {
        recyclerView.setHasFixedSize(false)
        recyclerView.layoutManager = layoutmanager
        recyclerView.adapter = listAdapter
        if (addDivider) {
            recyclerView.divider(recyclerView.context.getCompatColor(R.color.helperColor))
        }
    }

    override val dbResponse by lazy { DbResponseComponentImpl() }

    fun disposeResources() {
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }
}