package com.crazylegend.subhub.di

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.subhub.utils.SubToast
import com.google.gson.Gson


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
class CoreComponentImpl(override val application: Application) : CoreComponent {

    override val defaultPrefs : SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(application)
    }

    override val subToast by lazy { SubToast(application) }

    override val gson by lazy {
        Gson()
    }

    override fun setupRecycler(recyclerView: RecyclerView, layoutmanager: RecyclerView.LayoutManager, listAdapter: ListAdapter<*, *>) {
        recyclerView.setHasFixedSize(false)
        recyclerView.layoutManager = layoutmanager
        recyclerView.adapter = listAdapter
    }
}