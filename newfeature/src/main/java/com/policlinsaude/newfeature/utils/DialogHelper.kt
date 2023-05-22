package com.policlinsaude.newfeature.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.policlinsaude.newfeature.R

object DialogHelper {

    fun showDialog(context: Context, @StringRes title: Int, @StringRes message: Int,
                   @StringRes messagePositiveButton: Int, @StringRes messageNegativeButton: Int? = null,
                   listenerPositiveButton: () -> Unit = { },
                   listenerNegativeButton: (() -> Unit)? = { },
                   onDismiss: (() -> Unit)? = { }): AlertDialog {
        return showDialog(context,
            context.getString(title),
            context.getString(message),
            context.getString(messagePositiveButton),
            if (messageNegativeButton == null) null else context.getString(messageNegativeButton),
            listenerPositiveButton,
            listenerNegativeButton,
            onDismiss)
    }

    fun showDialog(context: Context, title: String, message: String,
                   messagePositiveButton: String? = "",
                   messageNegativeButton: String? = "",
                   listenerPositiveButton: () -> Unit = { },
                   listenerNegativeButton: (() -> Unit)? = { },
                   onDismiss: (() -> Unit)? = { }): AlertDialog {

        val alertDialogBuilder = AlertDialog.Builder(context)
        with(alertDialogBuilder) {
            setTitle(title)
            setMessage(message)
            if (messagePositiveButton?.isNotEmpty() != null) {
                setPositiveButton(messagePositiveButton) { _, _ ->
                    listenerPositiveButton.invoke()
                }
            }
            if (!messageNegativeButton.isNullOrEmpty()) {
                setNegativeButton(messageNegativeButton) { _, _ ->
                    listenerNegativeButton?.invoke()
                }
            }

            setCancelable(false)
            setOnDismissListener { onDismiss?.invoke() }
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
        return alertDialog
    }


    fun showErrorDialog(context: Context, message: String) {
        showDialog(context,
            context.getString(R.string.title_error_oops),
            message,
            context.getString(R.string.text_ok),
            null)
    }

}