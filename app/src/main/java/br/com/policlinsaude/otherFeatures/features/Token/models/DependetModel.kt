package com.policlinsaude.newfeature.features.Token.models

data class BeneficiariosRequestModel(
    val token: String = ""
)
data class BeneficiariosResponseModel(
    val listaBeneficiario: MutableList<BeneficiarioModel> = arrayListOf(),
    var codAcao: Int = -1,
    var msgExterna: String = "",
    var msgInterna: String = ""
)
data class BeneficiarioModel(
    val matricula: String = "",
    val ordem: String = "",
    val Nome_Beneficiario: String = ""
)