package br.com.policlinsaude.data.datasource.networking.rest.model

import br.com.policlinsaude.data.helper.InvalidData
import com.google.gson.annotations.SerializedName

data class JsonGetBannersResponse(@SerializedName("lista") val banners: List<JsonBannerResponse>? = mutableListOf(),
                                  @SerializedName("msgInterna") val msgInternal: String?
                                  = InvalidData.UNINITIALIZED.getString(),
                                  @SerializedName("msgExterna") val msgExternal: String?
                                  = InvalidData.UNINITIALIZED.getString())