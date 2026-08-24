package br.com.policlinsaude.data.datasource.networking.rest.model

import br.com.policlinsaude.data.helper.InvalidData
import com.google.gson.annotations.SerializedName

data class JsonPlanOptionsResponse(@SerializedName("lista") val planOptions: List<JsonPlanOptions>?
                                   = mutableListOf())

data class JsonPlanOptions(@SerializedName("Codigo_Guia") val codeGuide: Int?
                            = InvalidData.UNINITIALIZED.getInt(),
                           @SerializedName("Codigo_Plano") val codePlan: Int?
                           = InvalidData.UNINITIALIZED.getInt(),
                           @SerializedName("Descricao_Plano") val description: String?
                           = InvalidData.UNINITIALIZED.getString())