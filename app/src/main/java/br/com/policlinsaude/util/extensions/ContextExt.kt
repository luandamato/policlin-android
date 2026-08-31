package br.com.policlinsaude.util.extensions

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import java.util.Calendar

/**
 * Extensions e helpers de Context (navegação + date picker).
 * Migrado/adaptado de `_legacy/.../otherFeatures/utils/ContextExt.kt`.
 */

/** Abre URL no navegador (Chrome Custom Tabs quando disponível). */
fun Context.openBrowser(url: String) {
    val customTabsIntent = CustomTabsIntent.Builder().build()
    customTabsIntent.launchUrl(this, Uri.parse(url))
}

/** Abre URL no navegador via Intent simples. */
fun Context.openUrl(url: String) {
    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url))
    intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

object DatePickerHelper {

    enum class Mode { DAY_MONTH_YEAR, DAY_MONTH, MONTH_YEAR, MONTH, YEAR }

    data class DateResult(val day: Int? = null, val month: Int? = null, val year: Int)

    fun show(
        context: Context,
        mode: Mode = Mode.DAY_MONTH_YEAR,
        initialDate: Calendar = Calendar.getInstance(),
        minDate: Calendar? = null,
        maxDate: Calendar? = null,
        onDateSelected: (DateResult) -> Unit
    ) {
        val calendar = initialDate.clone() as Calendar
        val dialog = DatePickerDialog(
            context,
            { _, year, month, day ->
                onDateSelected(DateResult(day = day, month = month + 1, year = year))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        minDate?.let { dialog.datePicker.minDate = it.timeInMillis }
        maxDate?.let { dialog.datePicker.maxDate = it.timeInMillis }

        dialog.show()
    }
}

fun Context.pickDate(
    initialDate: Calendar = Calendar.getInstance(),
    minDate: Calendar? = null,
    maxDate: Calendar? = null,
    onDateSelected: (day: Int, month: Int, year: Int) -> Unit
) {
    DatePickerHelper.show(this, initialDate = initialDate, minDate = minDate, maxDate = maxDate) {
        onDateSelected(it.day ?: 0, it.month ?: 0, it.year)
    }
}
