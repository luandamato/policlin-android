package br.com.data.datasource.networking.rest.model

import br.com.data.helper.InvalidData
import com.google.gson.annotations.SerializedName

data class JsonCityOptionsResponse(@SerializedName("lista") val cityOptions: List<JsonCityOptions>?
                                   = mutableListOf())

data class JsonCityOptions(@SerializedName("codigo_Cidade") val code: Int?
                            = InvalidData.UNINITIALIZED.getInt(),
                           @SerializedName("descricao_Cidade") val description: String?
                           = InvalidData.UNINITIALIZED.getString())