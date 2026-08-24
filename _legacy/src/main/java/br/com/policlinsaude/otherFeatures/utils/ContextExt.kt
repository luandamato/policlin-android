package com.policlinsaude.newfeature.utils

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import java.util.Calendar
import java.util.Date

fun Context.openBrowser(url: String) {
    val builder = CustomTabsIntent.Builder()
    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(this, Uri.parse(url))
}

object DatePickerHelper {

    enum class Mode {
        DAY_MONTH_YEAR,
        DAY_MONTH,
        MONTH_YEAR,
        MONTH,
        YEAR
    }

    data class DateResult(
        val day: Int? = null,
        val month: Int? = null,
        val year: Int
    )

    fun show(
        context: Context,
        mode: Mode = Mode.DAY_MONTH_YEAR,
        initialDate: Calendar = Calendar.getInstance(),
        minDate: Calendar? = null,
        maxDate: Calendar? = null,
        onDateSelected: (DateResult) -> Unit
    ) {
        when (mode) {
            Mode.YEAR -> showYearPicker(
                context = context,
                initialDate = initialDate,
                minDate = minDate,
                maxDate = maxDate,
                onDateSelected = onDateSelected
            )

            Mode.MONTH,
            Mode.MONTH_YEAR -> showMonthPicker(
                context = context,
                mode = mode,
                initialDate = initialDate,
                minDate = minDate,
                maxDate = maxDate,
                onDateSelected = onDateSelected
            )

            Mode.DAY_MONTH,
            Mode.DAY_MONTH_YEAR -> showDayPicker(
                context = context,
                mode = mode,
                initialDate = initialDate,
                minDate = minDate,
                maxDate = maxDate,
                onDateSelected = onDateSelected
            )
        }
    }

    private fun showDayPicker(
        context: Context,
        mode: Mode,
        initialDate: Calendar,
        minDate: Calendar?,
        maxDate: Calendar?,
        onDateSelected: (DateResult) -> Unit
    ) {
        val calendar = initialDate.clone() as Calendar

        val dialog = DatePickerDialog(
            context,
            { _, year, month, day ->
                onDateSelected(
                    when (mode) {
                        Mode.DAY_MONTH -> DateResult(
                            day = day,
                            month = month + 1,
                            year = year
                        )

                        else -> DateResult(
                            day = day,
                            month = month + 1,
                            year = year
                        )
                    }
                )
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        minDate?.let {
            dialog.datePicker.minDate = it.timeInMillis
        }

        maxDate?.let {
            dialog.datePicker.maxDate = it.timeInMillis
        }

        dialog.show()
    }

    private fun showMonthPicker(
        context: Context,
        mode: Mode,
        initialDate: Calendar,
        minDate: Calendar?,
        maxDate: Calendar?,
        onDateSelected: (DateResult) -> Unit
    ) {
        val month = initialDate.get(Calendar.MONTH)
        val year = initialDate.get(Calendar.YEAR)

        val selectedMonth = month + 1

        onDateSelected(
            when (mode) {
                Mode.MONTH -> DateResult(
                    month = selectedMonth,
                    year = year
                )

                else -> DateResult(
                    month = selectedMonth,
                    year = year
                )
            }
        )
    }

    private fun showYearPicker(
        context: Context,
        initialDate: Calendar,
        minDate: Calendar?,
        maxDate: Calendar?,
        onDateSelected: (DateResult) -> Unit
    ) {
        val currentYear = initialDate.get(Calendar.YEAR)

        val minYear = minDate?.get(Calendar.YEAR) ?: 1900
        val maxYear = maxDate?.get(Calendar.YEAR) ?: Calendar.getInstance().get(Calendar.YEAR)

        val years = (minYear..maxYear)
            .toList()
            .reversed()
            .map { it.toString() }
            .toTypedArray()

        androidx.appcompat.app.AlertDialog.Builder(context)
            .setTitle("Selecione o ano")
            .setItems(years) { _, which ->
                onDateSelected(
                    DateResult(
                        year = years[which].toInt()
                    )
                )
            }
            .show()
    }

    fun format(result: DateResult): String {
        return when {
            result.day != null && result.month != null ->
                "%02d/%02d/%04d".format(
                    result.day,
                    result.month,
                    result.year
                )

            result.month != null ->
                "%02d/%04d".format(
                    result.month,
                    result.year
                )

            else ->
                result.year.toString()
        }
    }
}

fun Context.calendarDayMonthYear(
    initialDate: Calendar = Calendar.getInstance(),
    minDate: Calendar? = null,
    maxDate: Calendar? = null,
    listener: (day: Int, month: Int, year: Int) -> Unit
) {
    DatePickerHelper.show(
        context = this,
        mode = DatePickerHelper.Mode.DAY_MONTH_YEAR,
        initialDate = initialDate,
        minDate = minDate,
        maxDate = maxDate
    ) {
        listener(
            it.day!!,
            it.month!!,
            it.year
        )
    }
}

fun Context.calendarDayMonth(
    initialDate: Calendar = Calendar.getInstance(),
    minDate: Calendar? = null,
    maxDate: Calendar? = null,
    listener: (day: Int, month: Int, year: Int) -> Unit
) {
    DatePickerHelper.show(
        context = this,
        mode = DatePickerHelper.Mode.DAY_MONTH,
        initialDate = initialDate,
        minDate = minDate,
        maxDate = maxDate
    ) {
        listener(
            it.day!!,
            it.month!!,
            it.year
        )
    }
}

fun Context.calendarMonthYear(
    initialDate: Calendar = Calendar.getInstance(),
    minDate: Calendar? = null,
    maxDate: Calendar? = null,
    listener: (month: Int, year: Int) -> Unit
) {
    DatePickerHelper.show(
        context = this,
        mode = DatePickerHelper.Mode.MONTH_YEAR,
        initialDate = initialDate,
        minDate = minDate,
        maxDate = maxDate
    ) {
        listener(it.month!!, it.year)
    }
}

fun Context.calendarMonth(
    initialDate: Calendar = Calendar.getInstance(),
    listener: (month: Int) -> Unit
) {
    DatePickerHelper.show(
        context = this,
        mode = DatePickerHelper.Mode.MONTH,
        initialDate = initialDate
    ) {
        listener(it.month!!)
    }
}

fun Context.calendarYear(
    initialDate: Calendar = Calendar.getInstance(),
    minDate: Calendar? = null,
    maxDate: Calendar? = null,
    listener: (year: Int) -> Unit
) {
    DatePickerHelper.show(
        context = this,
        mode = DatePickerHelper.Mode.YEAR,
        initialDate = initialDate,
        minDate = minDate,
        maxDate = maxDate
    ) {
        listener(it.year)
    }
}

fun getYears(): MutableList<String> {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    return (1980..currentYear)
        .toList()
        .reversed()
        .map { it.toString() }
        .toMutableList()
}

fun getMonths(year: String): MutableList<String> {
    if (year.isEmpty()) return arrayListOf()

    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH)

    val lastMonth = if (currentYear == year.toInt()) {
        currentMonth
    } else {
        Calendar.DECEMBER
    }

    return (0..lastMonth)
        .map { it.getMonthCalendar() }
        .reversed()
        .toMutableList()
}

fun Int.getMonthCalendar(): String {
    return when (this) {
        Calendar.JANUARY -> "Janeiro"
        Calendar.FEBRUARY -> "Fevereiro"
        Calendar.MARCH -> "Março"
        Calendar.APRIL -> "Abril"
        Calendar.MAY -> "Maio"
        Calendar.JUNE -> "Junho"
        Calendar.JULY -> "Julho"
        Calendar.AUGUST -> "Agosto"
        Calendar.SEPTEMBER -> "Setembro"
        Calendar.OCTOBER -> "Outubro"
        Calendar.NOVEMBER -> "Novembro"
        Calendar.DECEMBER -> "Dezembro"
        else -> this.toString()
    }
}