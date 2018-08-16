package br.com.policlinsaude.core.helper

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by lmiyagi on 3/27/18.
 */
object Validations {

    fun isValidCPF(cpf: String): Boolean {
        val cpfValue = cpf.replace(".", "").replace("-", "")

        if (cpfValue.length != 11) {
            return false
        }

        val digits = cpfValue.substring(0, 9)
        val verifyingDigits = cpfValue.substring(9, 11)

        val verifyingDigits1 = generateVerifyingDigits(digits)
        val verifyingDigits2 = generateVerifyingDigits(digits + verifyingDigits1)

        return verifyingDigits == verifyingDigits1 + verifyingDigits2
    }

    private fun generateVerifyingDigits(digits: String): String {
        var weight = digits.length + 1
        var verifyingDigits = 0
        for (i in 0 until digits.length) {
            verifyingDigits += Integer.parseInt(digits.substring(i, i + 1)) * weight
            weight--
        }

        verifyingDigits = 11 - verifyingDigits % 11

        return if (verifyingDigits > 9) {
            "0"
        } else {
            verifyingDigits.toString()
        }
    }

    fun isValidDate(value: String): Boolean {
        if (!value.matches(Regex("^\\d{2}\\/\\d{2}\\/\\d{4}\$"))) return false
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return try {
            sdf.parse(value)
            true
        } catch (e: ParseException) {
            false
        }
    }
}