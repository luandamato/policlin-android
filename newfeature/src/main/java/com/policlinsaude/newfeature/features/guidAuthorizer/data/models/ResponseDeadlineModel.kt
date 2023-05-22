package com.policlinsaude.newfeature.features.guidAuthorizer.data.models

class ResponseDeadlineModel(
    val sdtAutPrazoResposta: ResponseDeadlineItemsModel? = ResponseDeadlineItemsModel(),
    var codAcao: Int = -1,
    var msgExterna: String = "",
    var msgInterna: String = ""

)

data class ResponseDeadlineItemsModel(
    val descricao: String? = "",
    val prazos: MutableList<DeadlinesItemsModel>? = arrayListOf()
)

data class DeadlinesItemsModel(
    val servico: String? = "",
    val prazo: String? = ""
)