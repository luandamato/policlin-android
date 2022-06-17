package com.policlinsaude.newfeature.features.tickets.data.models

data class TicketModel(
    val sdtBoleto: ArrayList<TicketDetail> = arrayListOf(),
    val codAcao: Int = 0,
    val msgInterna: String? = null,
    val msgExterna: String? = null,
)

data class TicketDetail(
    val nossoNumero: String? = null,
    val vencimento: String? = null,
    val informacoes: String? = null,
    val linhaDigitavel: String? = null,
    val link: String? = null,
    val valor: String? = null
)
