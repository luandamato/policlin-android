package br.com.policlinsaude.core.helper

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateHelper {
    const val FORMAT_DATE_YYYYMMDD = "yyyy-MM-dd"
    const val FORMAT_DATE_DDMMYYYY = "dd/MM/yyyy"

    fun getDate(format: String, parse: String): Date {
        var date = Date()
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        try {
            date = simpleDateFormat.parse(parse)
        }catch (parseException: ParseException) { }

        return date
    }

    fun getStringFromDate(format: String, date: Date): String {
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        return simpleDateFormat.format(date)
    }
}