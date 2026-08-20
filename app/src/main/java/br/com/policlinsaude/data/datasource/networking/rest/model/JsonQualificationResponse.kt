package br.com.data.datasource.networking.rest.model

import br.com.data.helper.InvalidData
import com.google.gson.annotations.SerializedName

data class JsonQualificationResponse(@SerializedName("imgQualificacao") val image: String?
                                     = InvalidData.UNINITIALIZED.getString(),
                                     @SerializedName("desc") val description: String?
                                     = InvalidData.UNINITIALIZED.getString(),
                                     @SerializedName("sigla") val initial: String?
                                     = InvalidData.UNINITIALIZED.getString())