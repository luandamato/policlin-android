package br.com.policlinsaude.data.models

class TokenResponseModel (
    val codAcao: Int = 0,
    val msgInterna: String? = "",
    val msgExterna: String? = "",
    val tokenAtendimento: String? = "",
    val minutosValidade: Int? = 5,
)