package com.policlinsaude.newfeature.features.guidAuthorizer.data.models

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File
import java.util.Base64

data class ProcessRequestModel(
    var token: String = "",
    var sdtAutCabecalho: ProcessRequestItemModel = ProcessRequestItemModel(),
)

data class ProcessRequestItemModel(
    var interlocutor: String = "",
    var telefone: String = "",
    var email: String = "",
    var semanaGestacional: Int = 0,
    var referenteCOVID: String = "",
    var agendado: String = "",
    var dataAtendimento: String = "",
    var prestador: String = "",
    var cidadeCod: Int = 0,
    var cidadeDes: String = "",
    var cidadeAtendimento: String = "",
)

@Parcelize
data class ProcessRequestPhotosModel(
    val token: String = "",
    val numeroWEB: String = "",
    val arquivo: String? = null,
    val observacao: String? = "",
    val perguntaID: Int? = null,
    val extensao: String = "",
    //val sdtAutAnexo: ProcessRequestPhotosItemModel = ProcessRequestPhotosItemModel()
) : Parcelable

@Parcelize
data class ProcessRequestPhotosItemModel(
    val arquivo: String? = null,
    val observacao: String? = "",
    val perguntaID: Int? = null,
    val extensao: String = "",
): Parcelable

data class ProcessResponsePhotosModel(
    val codAcao: Int = -1,
    val msgExterna: String = "",
    val msgInterna: String = ""
)

@Parcelize
data class PictureSave(
    val bitmap: Bitmap? = null,
    val format: String = "",
    val base64: String? = "",
    val file: File? = null
): Parcelable