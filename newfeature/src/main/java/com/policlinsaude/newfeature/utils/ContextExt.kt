package com.policlinsaude.newfeature.utils

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import androidx.browser.customtabs.CustomTabsIntent
import com.google.android.material.bottomsheet.BottomSheetDialog
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

fun getYears(): MutableList<String> {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val list = mutableListOf<String>()
    for(i in 1980..year) {
        list.add(i.toString())
    }
    return list.asReversed()
}

fun getMonths(year: String): MutableList<String> {
    if(year.isEmpty()) return arrayListOf()
    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    var list = mutableListOf<String>()
    if(currentYear == year.toInt()) {
        for (i in 0..month) {
            list.add(i.getMonthCalendar())
        }
    } else {
        list = arrayListOf("Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro","Dezembro")
    }
    return list.asReversed()
}

fun Int.getMonthCalendar(): String {
    return when(this) {
        0 -> "Janeiro"
        1 -> "Fevereiro"
        2 -> "Março"
        3 -> "Abril"
        4 -> "Maio"
        5 -> "Junho"
        6 -> "Julho"
        7 -> "Agosto"
        8 -> "Setembro"
        9 -> "Outubro"
        10 -> "Novembro"
        11 -> "Dezembro"
        else -> this.toString()
    }
}


