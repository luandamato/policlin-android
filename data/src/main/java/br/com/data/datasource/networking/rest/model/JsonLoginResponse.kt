package br.com.data.datasource.networking.rest.model

import br.com.data.helper.InvalidData
import com.google.gson.annotations.SerializedName

data class JsonLoginResponse(val token: String? = InvalidData.UNINITIALIZED.getString(),
                             @SerializedName("msgInterna") val msgInternal: String?
                             = InvalidData.UNINITIALIZED.getString(),
                             @SerializedName("msgExterna") val msgExternal: String?
                             = InvalidData.UNINITIALIZED.getString(),
                             @SerializedName("inf") val user: JsonUserResponse? = JsonUserResponse())
