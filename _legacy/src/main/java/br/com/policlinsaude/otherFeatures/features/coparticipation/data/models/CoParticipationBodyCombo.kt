package com.policlinsaude.newfeature.features.coparticipation.data.models

import com.google.gson.annotations.SerializedName

data class CoParticipationBodyCombo(
    @SerializedName("token") val token: String = "",
    @SerializedName("combo") val combo: Int
)

data class CoParticipationBodyValue(
    @SerializedName("token") val token: String,
    @SerializedName("tussCod") val codeComboOne: Int?,
    @SerializedName("copartCod") val codeComboTwo: Int?,
    @SerializedName("copartDes") val codeComboThree: String?,
    @SerializedName("tussDes") val description: String?,
)
