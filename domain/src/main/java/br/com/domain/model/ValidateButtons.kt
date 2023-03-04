package br.com.domain.model

class ValidateButtons(
    val boleto: Boolean,
    val copartFM: String,
    val codAcao: Int,
    val IR: Boolean,
    val msgInterna: String,
    val msgExterna: String
)

data class ValidateButtonBody(
    val token: String = ""
)