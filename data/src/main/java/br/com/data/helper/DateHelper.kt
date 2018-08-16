package br.com.data.helper

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateHelper {
    const val FORMAT_DATE_DDMMYYYYHHMMSS = "dd/MM/yyyy hh:mm:ss"
    const val FORMAT_DATE_YYYYMMDD = "yyyy-MM-dd"

    fun getDate(format: String, parse: String): Date {
        var date = Date()
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        try {
            date = simpleDateFormat.parse(parse)
        } catch (parseException: ParseException) {
        }

        return date
    }

    fun getStringFromDate(format: String, date: Date): String {
        return SimpleDateFormat(format, Locale.getDefault()).format(date)
    }
}