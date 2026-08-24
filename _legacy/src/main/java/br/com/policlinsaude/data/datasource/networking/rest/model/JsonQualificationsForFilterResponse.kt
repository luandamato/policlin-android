package br.com.policlinsaude.data.datasource.networking.rest.model

import br.com.policlinsaude.data.helper.InvalidData
import com.google.gson.annotations.SerializedName


data class JsonQualificationsForFilterResponse(@SerializedName("lista") val qualificationsOptions: List<JsonQualificationsForFilter>?
                                   = mutableListOf())

data class JsonQualificationsForFilter(@SerializedName("imgQualificacao") val image: String?
                                     = InvalidData.UNINITIALIZED.getString(),
                                               @SerializedName("desc") val description: String?
                                     = InvalidData.UNINITIALIZED.getString(),
                                               @SerializedName("cod") val cod: String?
                                     = InvalidData.UNINITIALIZED.getString())