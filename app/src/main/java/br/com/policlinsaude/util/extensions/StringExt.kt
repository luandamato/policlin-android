package br.com.policlinsaude.util.extensions

import java.text.NumberFormat
import java.util.Locale

/**
 * Extensions de String/moeda reutilizáveis.
 * Migrado/adaptado de `_legacy/.../otherFeatures/utils/UtilsExt.kt`.
 */

fun String.toCurrencyBRL(): String {
    val format = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    return try {
        format.format(this.toDouble())
    } catch (e: NumberFormatException) {
        format.format(0.0)
    }
}
fun String.onlyDigits(): String = replace(Regex("\\D"), "")

fun String.capitalizeFirst(): String = replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(Locale("pt", "BR")) else it.toString()
}

fun String.isOnlyDigits(): Boolean = onlyDigits().length == length && isNotEmpty()

/** Remove acentos de uma string. */
fun String.removeAccents(): String =
    this.replace(Regex("[áàâãä]"), "a")
        .replace(Regex("[éèêë]"), "e")
        .replace(Regex("[íìîï]"), "i")
        .replace(Regex("[óòôõö]"), "o")
        .replace(Regex("[úùûü]"), "u")
        .replace("ç", "c")
        .replace(Regex("[ÁÀÂÃÄ]"), "A")
        .replace(Regex("[ÉÈÊË]"), "E")
        .replace(Regex("[ÍÌÎÏ]"), "I")
        .replace(Regex("[ÓÒÔÕÖ]"), "O")
        .replace(Regex("[ÚÙÛÜ]"), "U")
        .replace("Ç", "C")
