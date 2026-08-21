package br.com.policlinsaude.data.datasource.networking.rest.model

import br.com.policlinsaude.data.helper.InvalidData
import com.google.gson.annotations.SerializedName
import java.util.*

data class JsonBannerResponse(@SerializedName("id") val id: String? = InvalidData.UNINITIALIZED.getString(),
                              @SerializedName("imagem") val image: String? = InvalidData.UNINITIALIZED.getString(),
                              @SerializedName("url") val url: String? = InvalidData.UNINITIALIZED.getString())