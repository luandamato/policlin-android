package com.policlinsaude.newfeature.features.guidAuthorizer.data.models

import com.google.gson.annotations.SerializedName
import com.policlinsaude.newfeature.features.coparticipation.data.models.CoParticipationItemsModel

data class GuideAuthorizerRequestResponseModel(
    val sdtAutCabecalho : GuideAuthorizerResponseItemModel = GuideAuthorizerResponseItemModel(),
    val codAcao: Int = -1,
    val msgExterna: String = "",
    val msgInterna: String = ""
)

data class GuideAuthorizerResponseItemModel(
    val numeroWEB: String = "",
    val matricula: String = "",
    val ordem: Int = 0,
    val interlocutor: String = "",
    val nome: String = "",
    val telefone: String = "",
    val email: String = "",
    val semanaGestacional: Int = 0,
    val referenteCOVID: String = "",
    val agendado: String = "",
    val dataAtendimento: String = "",
    val prestador: String = "",
    val cidadeCod: Int = 0,
    val cidadeDes: String = "",
    val cidadeAtendimento: String = "",
    val protocolo: String = "",
    val data: String = "",
    val statusCod: Int = 0,
    val statusDes: String = "",
    val observacao: String = "",
    val linkRelatorio: String = "",
    val anexos: MutableList<String> = arrayListOf(),
)

data class GuideAuthorizerResponseCancelModel(
    val codAcao: Int = -1,
    val msgExterna: String = "",
    val msgInterna: String = ""
)

data class GuideAuthorizerResponsePicturesDetailsModel(
    val sdtAutAnexos: MutableList<GuideAuthorizerReponsePicturesItemsModel> = arrayListOf(),
    val codAcao: Int = -1,
    val msgExterna: String = "",
    val msgInterna: String = ""

)

data class GuideAuthorizerReponsePicturesItemsModel(
    val arquivoID: String = "",
    val observacao: String = "",
    val CRMSolicitante: String = "",
    val medicoSolicitante: String = "",
    val link: String = ""
)

data class GuideAuthorizerQuestionsModel(
    val sdtAutPerguntas: MutableList<GuideAuthorizerQuestionsItemsModel> = arrayListOf(),
    val codAcao: Int = -1,
    val msgExterna: String = "",
    val msgInterna: String = ""
)

data class GuideAuthorizerQuestionsItemsModel(
    val perguntaID: String? = "",
    val pergunta: String? = "",
    val dataPergunta: String? = "",
    val resposta: String? = "",
    val dataResposta: String? = "",
    val anexos: MutableList<QuestionsAttachmentsModel> = arrayListOf()
)

data class QuestionsAttachmentsModel(
    val respostaID: String? = "",
    val nome: String? = "",
    val link: String? = ""
)

data class GuideModel(
    val sdtAutGuias: MutableList<GuideItemsModel> = arrayListOf(),
    val codAcao: Int = -1,
    val msgExterna: String = "",
    val msgInterna: String = ""
)

data class GuideItemsModel(
    val guiaID: String? = "",
    val nome: String? = "",
    val data: String? = "",
    val link: String? = "",
)

data class ComumModel(
    val codAcao: Int = -1,
    val msgInterna: String = "",
    val msgExterna: String = "",
)
