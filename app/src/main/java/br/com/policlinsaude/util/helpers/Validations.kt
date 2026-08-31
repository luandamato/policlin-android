package br.com.policlinsaude.util.helpers

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.regex.Pattern

/**
 * Validações comuns reutilizáveis.
 * Migrado/adaptado de `_legacy/.../core/helper/Validations.kt`.
 */
object Validations {

    fun isValidCPF(cpf: String): Boolean {
        val cpfValue = cpf.replace(".", "").replace("-", "")
        if (cpfValue.length != 11 || cpfValue.all { it == cpfValue[0] }) return false

        val digits = cpfValue.substring(0, 9)
        val verifyingDigits = cpfValue.substring(9, 11)
        val verifyingDigits1 = generateVerifyingDigits(digits)
        val verifyingDigits2 = generateVerifyingDigits(digits + verifyingDigits1)

        return verifyingDigits == verifyingDigits1 + verifyingDigits2
    }

    private fun generateVerifyingDigits(digits: String): String {
        var weight = digits.length + 1
        var sum = 0
        for (i in 0 until digits.length) {
            sum += Integer.parseInt(digits.substring(i, i + 1)) * weight
            weight--
        }
        val verifyingDigit = 11 - sum % 11
        return if (verifyingDigit > 9) "0" else verifyingDigit.toString()
    }

    fun isValidDate(value: String): Boolean {
        if (!value.matches(Regex("^\\d{2}/\\d{2}/\\d{4}$"))) return false
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        sdf.isLenient = false
        return try {
            sdf.parse(value)
            true
        } catch (e: ParseException) {
            false
        }
    }

    fun isValidEmail(email: String): Boolean {
        val pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        return pattern.matcher(email).matches()
    }

    fun isPhoneValid(phone: String): Boolean {
        val digits = phone.replace(Regex("\\D"), "")
        return digits.length in 10..11
    }
}