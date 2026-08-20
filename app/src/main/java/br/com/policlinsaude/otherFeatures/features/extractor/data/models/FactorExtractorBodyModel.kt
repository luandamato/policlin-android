package com.policlinsaude.newfeature.features.extractor.data.models

import com.google.gson.annotations.SerializedName

data class FactorExtractorBodyModel(
    @SerializedName("token") var token: String = "",
    @SerializedName("ano") var year: Int? = null,
    @SerializedName("mes") var month: Int? = null
)