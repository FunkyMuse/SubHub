package com.crazylegend.subhub.di

import android.app.Application
import android.content.SharedPreferences
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.subhub.utils.SubToast
import com.google.gson.Gson


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
interface CoreComponent {
    val application: Application
    val defaultPrefs: SharedPreferences
    val subToast: SubToast
    val gson: Gson
    fun setupRecycler(recyclerView: RecyclerView, layoutmanager: RecyclerView.LayoutManager, listAdapter: ListAdapter<*, *>)
}