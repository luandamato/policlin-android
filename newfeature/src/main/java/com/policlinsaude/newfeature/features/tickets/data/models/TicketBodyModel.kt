package com.policlinsaude.newfeature.features.tickets.data.models

import com.google.gson.annotations.SerializedName

data class TicketBodyModel(
    @SerializedName("token") var token: String = "ANvS4u5P2kg7e5HLyaVbQ7QXcXLe/qfH",
    @SerializedName("opcao") var option: Int = 1,
    @SerializedName("ano") var year: Int = 0,
    @SerializedName("mes") var month: Int = 0,
)
