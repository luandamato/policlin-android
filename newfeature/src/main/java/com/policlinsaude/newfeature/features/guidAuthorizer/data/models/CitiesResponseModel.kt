package com.policlinsaude.newfeature.features.guidAuthorizer.data.models

data class CityRequestModel(
    val token: String = ""
)

data class CitiesResponseModel(
    val sdtAutCidades: MutableList<CityModel> = arrayListOf(),
    var codAcao: Int = -1,
    var msgExterna: String = "",
    var msgInterna: String = ""
)

data class CityModel(
    val codigo: Int = 0,
    val descricao: String = "",
    val outra: String = ""
)
