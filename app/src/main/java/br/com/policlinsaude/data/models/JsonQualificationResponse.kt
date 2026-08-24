package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData
import com.google.gson.annotations.SerializedName

data class JsonQualificationResponse(@SerializedName("imgQualificacao") val image: String?
                                     = InvalidData.UNINITIALIZED.getString(),
                                     @SerializedName("desc") val description: String?
                                     = InvalidData.UNINITIALIZED.getString(),
                                     @SerializedName("sigla") val initial: String?
                                     = InvalidData.UNINITIALIZED.getString())