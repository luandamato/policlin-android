package com.policlinsaude.newfeature.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.*

enum class DateFormats(val format: String) {
    DATE_HOUR_BR("yyyy-MM-dd"),
    DD_MM_YYYY("dd/MM/yyyy"),
    MM_YYYY("MM/yyyy")
}


fun String.toMMYYYY(): String {
    val parser = SimpleDateFormat(DateFormats.DATE_HOUR_BR.format, Locale("pt", "BR"))
    val formatter = SimpleDateFormat(DateFormats.MM_YYYY.format, Locale("pt", "BR"))

    return formatter.format(parser.parse(this))
}

fun String.toDDMMYYYY(): String {
    val parser = SimpleDateFormat(DateFormats.DATE_HOUR_BR.format, Locale("pt", "BR"))
    val formatter = SimpleDateFormat(DateFormats.DD_MM_YYYY.format, Locale("pt", "BR"))

    return formatter.format(parser.parse(this))
}

