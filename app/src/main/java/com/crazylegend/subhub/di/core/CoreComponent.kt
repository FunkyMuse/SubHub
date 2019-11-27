package com.crazylegend.subhub.di.core

import android.app.Application
import android.content.SharedPreferences
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.subhub.di.dbResponse.DbResponseComponentImpl
import com.crazylegend.subhub.di.pickedDirDB.PickedDirComponentImpl
import com.crazylegend.subhub.utils.SubToast
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
interface CoreComponent {
    val application: Application
    val defaultPrefs: SharedPreferences
    val subToast: SubToast
    val gson: Gson
    val compositeDisposable: CompositeDisposable
    fun setupRecycler(recyclerView: RecyclerView, layoutmanager: RecyclerView.LayoutManager, listAdapter: ListAdapter<*, *>, addDivider: Boolean = false)
    val pickedDirComponent: PickedDirComponentImpl
    val dbResponse: DbResponseComponentImpl
}