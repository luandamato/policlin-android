package com.policlinsaude.newfeature.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.*

enum class DateFormats(val format: String) {
    DATE_HOUR_BR("yyyy-MM-dd'T'HH:mm:SS"),
    DATE_BR("yyyy-MM-dd"),
    DD_MM_YYYY("dd/MM/yyyy"),
    MM_YYYY("MM/yyyy"),
    HH_MM_SS("HH:mm:ss"),
}


fun String.toMMYYYY(): String {
    val parser = SimpleDateFormat(DateFormats.DATE_BR.format, Locale("pt", "BR"))
    val formatter = SimpleDateFormat(DateFormats.MM_YYYY.format, Locale("pt", "BR"))

    return formatter.format(parser.parse(this))
}

fun String.toDDMMYYYY(): String {
    return try {
        val parser = SimpleDateFormat(DateFormats.DATE_BR.format, Locale("pt", "BR"))
        val formatter = SimpleDateFormat(DateFormats.DD_MM_YYYY.format, Locale("pt", "BR"))

        return formatter.format(parser.parse(this))
    } catch (e: java.lang.Exception) {
        this
    }
}

fun String.toHHMMSS(): String {
    val parser = SimpleDateFormat(DateFormats.DATE_HOUR_BR.format, Locale("pt", "BR"))
    val formatter = SimpleDateFormat(DateFormats.HH_MM_SS.format, Locale("pt", "BR"))
    return formatter.format(parser.parse(this))
}

fun Int.getMonth(): String {
    return when(this) {
        1 -> "Janeiro"
        2 -> "Fevereiro"
        3 -> "Março"
        4 -> "Abril"
        5 -> "Maio"
        6 -> "Junho"
        7 -> "Julho"
        8 -> "Agosto"
        9 -> "Setembro"
        10 -> "Outubro"
        11 -> "Novembro"
        12 -> "Dezembro"
        else -> this.toString()
    }
}

fun String.toMonths(): Int {
    return when(this) {
        "Janeiro" -> 1
        "Fevereiro" -> 2
        "Março" -> 3
        "Abril" -> 4
        "Maio" -> 5
        "Junho" -> 6
        "Julho" -> 7
        "Agosto" -> 8
        "Setembro" -> 9
        "Outubro" -> 10
        "Novembro" -> 11
        "Dezembro" -> 12
        else -> 0
    }
}



