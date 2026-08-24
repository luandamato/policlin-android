package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData
import com.google.gson.annotations.SerializedName
import java.util.*

data class JsonBannerResponse(@SerializedName("id") val id: String? = InvalidData.UNINITIALIZED.getString(),
                              @SerializedName("imagem") val image: String? = InvalidData.UNINITIALIZED.getString(),
                              @SerializedName("url") val url: String? = InvalidData.UNINITIALIZED.getString())