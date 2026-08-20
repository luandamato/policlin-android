package com.policlinsaude.newfeature.features.notifications.data.models

import com.google.gson.annotations.SerializedName

data class NotificationsResponseModel (
    @SerializedName("lista") var list: MutableList<NotificationsModel> = arrayListOf(),
    val codAcao: Int = 0,
    val msgInterna: String = "",
    val msgExterna: String = "",
)


data class NotificationsModel (
    @SerializedName("id") var id: String = "",
    @SerializedName("titulo") var title: String = "",
    @SerializedName("descricao") var description: String = "",
    @SerializedName("dataHoraEnvio") var sendDate: String = "",
    @SerializedName("link") var link: String = "",
    @SerializedName("telefone") var phoneNumber: String = ""
)