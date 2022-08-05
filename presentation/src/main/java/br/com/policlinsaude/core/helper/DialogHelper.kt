package br.com.policlinsaude.core.helper

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import br.com.policlinsaude.R

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
                   messagePositiveButton: String? = "", messageNegativeButton: String? = "",
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
            setOnDismissListener({ onDismiss?.invoke() })
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
        return alertDialog
    }

    fun showDialogTryAgain(context: Context, listenerPositiveButton: () -> Unit = { },
                           message: String = InvalidData.UNINITIALIZED.getString()) {
        with(context) {
            var dismissListener: () -> Unit = {

            }

            val dialog = showDialog(context = context, title = getString(R.string.title_error_oops),
                    message = if (message.isEmpty()) getString(R.string.msg_error_unknown) else message,
                    messagePositiveButton = getString(R.string.action_try_again),
                    messageNegativeButton = getString(R.string.action_cancel),
                    listenerPositiveButton = {
                        listenerPositiveButton.invoke()
                        dismissListener.invoke()

                    },
                    listenerNegativeButton = {
                        dismissListener.invoke()
                    })

            dismissListener = {
                dialog.dismiss()
            }
        }
    }

    fun showErrorDialog(context: Context, message: String) {
        showDialog(context,
                context.getString(R.string.title_error_oops),
                message,
                context.getString(R.string.text_ok),
                null)
    }

}