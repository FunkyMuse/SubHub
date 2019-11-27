package com.crazylegend.subhub.di.dbResponse

import android.view.View
import androidx.recyclerview.widget.ListAdapter
import com.crazylegend.kotlinextensions.database.DBResult
import com.crazylegend.kotlinextensions.ui.ColorProgressBar


/**
 * Created by crazy on 11/27/19 to long live and prosper !
 */
interface DbResponseComponent {
    fun handleDBError(throwable: Throwable, colorProgressBar: ColorProgressBar, noDataView: View? = null)
    fun handleLoading(colorProgressBar: ColorProgressBar, noDataView: View? = null)
    fun <T> handleSuccessList(list: List<T> = emptyList(), noDataView: View? = null, colorProgressBar: ColorProgressBar,
                              recyclerAdapter: ListAdapter<T, *>? = null)

    fun handleNoData(colorProgressBar: ColorProgressBar, noDataView: View? = null, recyclerAdapter: ListAdapter<*, *>? = null)
    fun <T> handleDBCall(dbResult: DBResult<List<T>>, colorProgressBar: ColorProgressBar,
                         noDataView: View?, listAdapter: ListAdapter<T, *>)

    fun hideLoadingOnly(colorProgressBar: ColorProgressBar)
    fun handleSuccessUI(noDataView: View?, colorProgressBar: ColorProgressBar)
}