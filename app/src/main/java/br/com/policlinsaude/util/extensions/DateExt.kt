package br.com.policlinsaude.util.extensions

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Extensions e helpers de data reutilizáveis.
 * Migrado/adaptado de `_legacy/.../core/helper/DateHelper.kt` e
 * `_legacy/.../otherFeatures/utils/{DateExt,ContextExt}.kt`.
 */

enum class DateFormats(val format: String) {
    DATE_HOUR_BR("yyyy-MM-dd'T'HH:mm:ss"),
    DATE_BR("yyyy-MM-dd"),
    DD_MM_YYYY("dd/MM/yyyy"),
    MM_YYYY("MM/yyyy"),
    HH_MM_SS("HH:mm:ss"),
}

object DateHelper {
    const val FORMAT_DATE_YYYYMMDD = "yyyy-MM-dd"
    const val FORMAT_DATE_DDMMYYYY = "dd/MM/yyyy"

    fun getDate(format: String, parse: String): Date =
        try {
            SimpleDateFormat(format, Locale.getDefault()).parse(parse) ?: Date()
        } catch (e: Exception) {
            Date()
        }

    fun getStringFromDate(format: String, date: Date): String =
        SimpleDateFormat(format, Locale.getDefault()).format(date)
}

/** Formata um Date conforme o enum de formato. */
fun Date.toFormatted(format: DateFormats): String =
    SimpleDateFormat(format.format, Locale("pt", "BR")).format(this)

fun String.toMMYYYY(): String {
    val parser = SimpleDateFormat(DateFormats.DATE_BR.format, Locale("pt", "BR"))
    val formatter = SimpleDateFormat(DateFormats.MM_YYYY.format, Locale("pt", "BR"))
    return formatter.format(parser.parse(this))
}

fun String.toDDMMYYYY(): String {
    return try {
        val parser = SimpleDateFormat(DateFormats.DATE_BR.format, Locale("pt", "BR"))
        val formatter = SimpleDateFormat(DateFormats.DD_MM_YYYY.format, Locale("pt", "BR"))
        formatter.format(parser.parse(this))
    } catch (e: Exception) {
        this
    }
}

fun String.toHHMMSS(): String {
    val parser = SimpleDateFormat(DateFormats.DATE_HOUR_BR.format, Locale("pt", "BR"))
    val formatter = SimpleDateFormat(DateFormats.HH_MM_SS.format, Locale("pt", "BR"))
    return formatter.format(parser.parse(this))
}

/** Nome do mês (1..12) em pt-BR. */
fun Int.getMonthName(): String = when (this) {
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

/** Nome do mês (1..12) em pt-BR. */
fun Int.getMonthCalendar(): String = getMonthName()

/** Número do mês a partir do nome em pt-BR (0 se não reconhecido). */
fun String.toMonthNumber(): Int = when (this) {
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

/** Lista de anos (1980..atual), do mais recente para o mais antigo. */
fun getYears(): MutableList<String> {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    return (1980..currentYear).toList().reversed().map { it.toString() }.toMutableList()
}

/** Lista de meses (nominais) até o mês atual do ano informado. */
fun getMonths(year: String): MutableList<String> {
    if (year.isEmpty()) return arrayListOf()
    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH)
    val lastMonth = if (currentYear == year.toInt()) currentMonth else Calendar.DECEMBER
    return (0..lastMonth).map { it.getMonthCalendar() }.reversed().toMutableList()
}
