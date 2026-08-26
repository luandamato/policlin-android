package br.com.policlinsaude.data.models

data class IncomeTaxResponseModel(
    val listaIR: ArrayList<IncomeTaxItemModel> = arrayListOf(),
    val codAcao: Int = 0,
    val msgInterna: String? = "",
    val msgExterna: String? = "",
)

data class IncomeTaxItemModel(
    val ano: Int = 0,
    val descricao: String? = "",
    val link: String? = ""
)