package com.policlinsaude.newfeature.features.extractor.data.models

import com.google.gson.annotations.SerializedName


data class FactorExtractorYearsModel(
    val sdtValores: ArrayList<FactorExtractorDatesItemModel> = arrayListOf(),
    val codAcao: Int = 0,
    val msgInterna: String = "",
    val msgExterna: String = "",
)

data class FactorExtractorMonthsModel(
    val sdtValores: ArrayList<FactorExtractorDatesItemModel> = arrayListOf(),
    val codAcao: Int = 0,
    val msgInterna: String = "",
    val msgExterna: String = "",
)

data class FactorExtractorDatesItemModel(
    val valor: Int
)

data class FactorExtractorMonthsBody(
    @SerializedName("token") val token: String = "",
    @SerializedName("ano") val year: Int = 0,
)