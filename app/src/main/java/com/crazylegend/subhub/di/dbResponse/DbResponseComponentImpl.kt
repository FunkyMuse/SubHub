package com.crazylegend.subhub.di.dbResponse

import android.view.View
import androidx.recyclerview.widget.ListAdapter
import com.crazylegend.kotlinextensions.collections.isNotNullOrEmpty
import com.crazylegend.kotlinextensions.database.DBResult
import com.crazylegend.kotlinextensions.database.handle
import com.crazylegend.kotlinextensions.ui.ColorProgressBar
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.visible


/**
 * Created by crazy on 11/27/19 to long live and prosper !
 */
class DbResponseComponentImpl : DbResponseComponent {
    override fun handleDBError(throwable: Throwable, colorProgressBar: ColorProgressBar, noDataView: View?) {
        throwable.printStackTrace()
        showNoData(noDataView)
        colorProgressBar.gone()
    }

    override fun handleLoading(colorProgressBar: ColorProgressBar, noDataView: View?) {
        hideNoData(noDataView)
        colorProgressBar.visible()
    }

    override fun <T> handleSuccessList(list: List<T>, noDataView: View?, colorProgressBar: ColorProgressBar, recyclerAdapter: ListAdapter<T, *>?) {
        if (list.isNotNullOrEmpty) {
            hideNoData(noDataView)
            recyclerAdapter?.submitList(list)
        } else {
            showNoData(noDataView)
            recyclerAdapter?.submitList(emptyList())
        }
        colorProgressBar.gone()
    }

    override fun handleNoData(colorProgressBar: ColorProgressBar, noDataView: View?, recyclerAdapter: ListAdapter<*, *>?) {
        showNoData(noDataView)
        recyclerAdapter?.submitList(emptyList())
        colorProgressBar.gone()
    }


    override fun <T> handleDBCall(dbResult: DBResult<List<T>>, colorProgressBar: ColorProgressBar, noDataView:
    View?, listAdapter: ListAdapter<T, *>) {
        dbResult.handle({
            //querying DB
            handleLoading(colorProgressBar, noDataView)
        }, {
            //empty DB
            handleNoData(colorProgressBar, noDataView, listAdapter)
        }, { // db error
            throwable ->
            handleDBError(throwable, colorProgressBar, noDataView)
        }, {
            //Success
            handleSuccessList(this, noDataView, colorProgressBar, listAdapter)
        })
    }

    override fun hideLoadingOnly(colorProgressBar: ColorProgressBar) {
        colorProgressBar.gone()
    }

    override fun handleSuccessUI(noDataView: View?, colorProgressBar: ColorProgressBar) {
        hideNoData(noDataView)
        colorProgressBar.gone()
    }

    private fun showNoData(view: View?) {
        view?.visible()
    }

    private fun hideNoData(view: View?) {
        view?.gone()
    }

}