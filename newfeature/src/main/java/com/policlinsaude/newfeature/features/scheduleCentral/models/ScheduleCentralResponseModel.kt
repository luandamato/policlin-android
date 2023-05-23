package com.policlinsaude.newfeature.features.scheduleCentral.models

data class ScheduleCentralResponseModel(
    val codAcao: Int = 0,
    val msgInterna: String? = "",
    val msgExterna: String? = "",
    val mensagem: String? = "",
    val telefone: String? = "",
    val email: String? = "",
    val whatsapp: String? = "",
)

