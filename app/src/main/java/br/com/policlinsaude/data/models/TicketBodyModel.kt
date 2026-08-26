package br.com.policlinsaude.data.models

import com.google.gson.annotations.SerializedName

data class TicketBodyModel(
    @SerializedName("token") var token: String = "",
    @SerializedName("opcao") var option: Int = 1,
    @SerializedName("ano") var year: Int = 0,
    @SerializedName("mes") var month: Int = 0,
)