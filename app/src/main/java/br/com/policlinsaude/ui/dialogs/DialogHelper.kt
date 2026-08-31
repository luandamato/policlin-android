package br.com.policlinsaude.ui.dialogs

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import br.com.policlinsaude.R

/**
 * Diálogos de alerta compartilhados.
 * Migrado/adaptado e unificado de:
 * - `_legacy/.../core/helper/DialogHelper.kt`
 * - `_legacy/.../otherFeatures/utils/DialogHelper.kt`
 */
object DialogHelper {

    fun showDialog(
        context: Context,
        @StringRes title: Int,
        @StringRes message: Int,
        @StringRes messagePositiveButton: Int,
        @StringRes messageNegativeButton: Int? = null,
        listenerPositiveButton: () -> Unit = {},
        listenerNegativeButton: (() -> Unit)? = {},
        onDismiss: (() -> Unit)? = {}
    ): AlertDialog = showDialog(
        context,
        context.getString(title),
        context.getString(message),
        context.getString(messagePositiveButton),
        messageNegativeButton?.let { context.getString(it) },
        listenerPositiveButton,
        listenerNegativeButton,
        onDismiss
    )

    fun showDialog(
        context: Context,
        title: String,
        message: String,
        messagePositiveButton: String? = "",
        messageNegativeButton: String? = "",
        listenerPositiveButton: () -> Unit = {},
        listenerNegativeButton: (() -> Unit)? = {},
        onDismiss: (() -> Unit)? = {}
    ): AlertDialog {
        val builder = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)

        if (!messagePositiveButton.isNullOrEmpty()) {
            builder.setPositiveButton(messagePositiveButton) { _, _ -> listenerPositiveButton.invoke() }
        }
        if (!messageNegativeButton.isNullOrEmpty()) {
            builder.setNegativeButton(messageNegativeButton) { _, _ -> listenerNegativeButton?.invoke() }
        }

        val dialog = builder.setCancelable(false).create()
        dialog.setOnDismissListener { onDismiss?.invoke() }
        dialog.show()
        return dialog
    }

    fun showErrorDialog(context: Context, message: String) {
        showDialog(
            context,
            context.getString(R.string.title_error_oops),
            message,
            context.getString(R.string.text_ok),
            null
        )
    }

    fun showDialogTryAgain(
        context: Context,
        message: String = "",
        listenerPositiveButton: () -> Unit = {}
    ) {
        val dialog = showDialog(
            context = context,
            title = context.getString(R.string.title_error_oops),
            message = if (message.isEmpty()) context.getString(R.string.msg_error_unknown) else message,
            messagePositiveButton = context.getString(R.string.action_try_again),
            messageNegativeButton = context.getString(R.string.action_cancel),
            listenerPositiveButton = listenerPositiveButton
        )
        dialog.setOnDismissListener(null)
    }

    fun showUpdateDialog(
        context: Context,
        message: String = ""
    ) {
        val packageName = context.packageName
        showDialog(
            context,
            context.getString(R.string.title_error_oops),
            if (message.isEmpty()) context.getString(R.string.msg_error_unknown) else message,
            context.getString(R.string.text_ok),
            null,
            listenerPositiveButton = {
                try {
                    context.startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
                    )
                } catch (e: Exception) {
                    context.startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
                    )
                }
            }
        )
    }
}