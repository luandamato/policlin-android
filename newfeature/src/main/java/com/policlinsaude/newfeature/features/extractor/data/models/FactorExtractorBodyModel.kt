package com.policlinsaude.newfeature.features.extractor.data.models

import com.google.gson.annotations.SerializedName

data class FactorExtractorBodyModel(
    @SerializedName("token") var token: String = "teste2",
    @SerializedName("ano") var year: Int = 0,
    @SerializedName("mes") var month: Int = 0
)