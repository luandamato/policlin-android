package com.policlinsaude.newfeature.features.coparticipation.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class CoParticipationModel(
    @SerializedName("sdtVC_Combos") val items: ArrayList<CoParticipationItemsModel> = arrayListOf(),
    val codAcao: Int = 0,
    val msgInterna: String = "",
    val msgExterna: String = "",
)

data class CoParticipationItemsModel(
    @SerializedName("codigo") val code: String = "",
    @SerializedName("descricao") val description: String = ""
)

data class CoParticipationItems(
    @SerializedName("sdtValoresCopart") val items: ArrayList<CoParticipationItemsDetails> = arrayListOf(),
    val codAcao: Int = 0,
    val msgInterna: String = "",
    val msgExterna: String = "",
)

@Parcelize
data class CoParticipationItemsDetails(
    val tussCod: String = "",
    val tussDes: String = "",
    val copartCod: Int = 0,
    val copartDes: String = "",
    val valor: String = ""
): Parcelable
