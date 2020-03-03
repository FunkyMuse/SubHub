package com.crazylegend.subhub.utils

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.setPadding
import com.crazylegend.subhub.R


/**
 * Created by crazy on 11/26/19 to long live and prosper !
 */
class SubToast(private val context: Context) {

    private var toast: Toast? = null
    private val drawablePadding = 1
    private val textPadding = 5
    private val textViewSize = 18f

    fun jobToast(message: String, length: Int = Toast.LENGTH_SHORT) {
        toast?.cancel()
        toast = Toast.makeText(context, message, length)
        val toastView = toast?.view
        toastView?.setBackgroundResource(R.drawable.rounded_bg)
        with(toastView?.findViewById<TextView>(android.R.id.message)) {
            this?.apply {
                compoundDrawablePadding = drawablePadding
                setPadding(textPadding)
                textSize = textViewSize
            }
        }
        toast?.show()
    }

    fun jobToastAboveKeyboard(message: String, length: Int = Toast.LENGTH_SHORT, rootView: View?) {
        toast?.cancel()
        toast = Toast.makeText(context, message, length)
        val toastView = toast?.view
        toastView?.setBackgroundResource(R.drawable.rounded_bg)
        with(toastView?.findViewById<TextView>(android.R.id.message)) {
            this?.apply {
                compoundDrawablePadding = drawablePadding
                setPadding(textPadding)
                textSize = textViewSize
            }
        }
        toast?.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, (rootView?.height?.div(2))
                ?: 0)
        toast?.show()
    }

}