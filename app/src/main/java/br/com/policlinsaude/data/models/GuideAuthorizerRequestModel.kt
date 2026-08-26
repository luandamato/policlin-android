package br.com.policlinsaude.data.models

data class GuideAuthorizerRequestModel(
    val token: String
)

data class GuideAuthorizerResponseModel(
    var sdtAutPesq: MutableList<GuideAuthorizerItemsModel> = mutableListOf(),
    var codAcao: Int = -1,
    var msgExterna: String = "",
    var msgInterna: String = ""
)

data class GuideAuthorizerItemsModel(
    var numeroWEB: String? = "",
    var data: String? = "",
    var statusCod: String? = "",
    var statusDes: String? = "",
    var nome: String? = ""
)

data class GuideAuthorizerRequestCancelModel(
    var token: String = "",
    var numeroWEB: String = "",
)

data class ProcessRequestSendAnswerAttachmentModel(
    val token: String = "",
    val numeroWEB: String = "",
    val arquivo: String? = null,
    val observacao: String? = "",
    val perguntaID: Int? = null,
    val extensao: String = "",
    //val sdtAutResposta: ProcessRequestPhotosItemModel = ProcessRequestPhotosItemModel()
)

data class ProcessRequestSendAnswerModel(
    val token: String = "",
    val numeroWEB: Int = 0,
    val perguntaID: Int = 0,
    val resposta: String = ""
)