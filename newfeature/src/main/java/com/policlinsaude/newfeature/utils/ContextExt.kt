package com.policlinsaude.newfeature.utils

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.whiteelephant.monthpicker.MonthPickerDialog
import java.util.*


fun Context.openBrowser(url: String) {
    val builder = CustomTabsIntent.Builder()
    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(
        this,
        Uri.parse(url)
    )
}


fun Context.calendarMonthYear(listener: (month: Int, year: Int) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)

    val builder = MonthPickerDialog.Builder(
        this,
        { selectedMonth, selectedYear ->
            listener((selectedMonth + 1), selectedYear)
        }, year,0
    )

    builder
        .setActivatedMonth(month)
        .setMinYear(1960)
        .setMaxYear(year)
        .build()
        .show()
}

fun Context.calendarYear(listener: (year: Int) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)

    val builder = MonthPickerDialog.Builder(
        this,
        { _, selectedYear ->
            listener(selectedYear)
        }, year,0
    )

    builder
        .showYearOnly()
        .setYearRange(1980, year)
        .build()
        .show()
}

fun Context.calendarMonth(listener: (month: Int) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)

    val builder = MonthPickerDialog.Builder(
        this,
        { selectedMonth, _ ->
            listener(selectedMonth + 1)
        }, year, month
    )

    builder
        .showMonthOnly()
        .setMonthRange(Calendar.JANUARY, Calendar.DECEMBER)
        .build()
        .show()
}
