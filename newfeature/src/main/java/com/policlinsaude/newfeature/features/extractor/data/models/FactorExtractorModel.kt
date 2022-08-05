package com.policlinsaude.newfeature.features.extractor.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class FactorExtractorModel(
    val extratoCopart: ArrayList<FactorExtractorDetailModel> = arrayListOf(),
    val codAcao: Int = 0,
    val valorTotal: String? = "",
    val msgInterna: String = "",
    val msgExterna: String = "",
)

data class FactorExtractorDetailModel(
    val prestador: String = "",
    val data: String? = null,
    val itens: ArrayList<FactorExtractorDetailItemsModel> = arrayListOf(),
)

@Parcelize
data class FactorExtractorDetailItemsModel(
    val tabela: Int = 0,
    val codigo: String = "",
    val descricao: String = "",
    val quantidade: Int = 0,
    val valor: String = "",
    val copartCod: Int = 0,
    val copartDes: String
): Parcelable
