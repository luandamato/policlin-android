package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData
import com.google.gson.annotations.SerializedName

data class JsonLoginResponse (
    @SerializedName("codAcao") val actionCode: Int? = InvalidData.UNINITIALIZED.getInt(),
    val token: String? = InvalidData.UNINITIALIZED.getString(),
    @SerializedName("msgInterna") val msgInternal: String? = InvalidData.UNINITIALIZED.getString(),
    @SerializedName("msgExterna") val msgExternal: String? = InvalidData.UNINITIALIZED.getString(),
    @SerializedName("inf") val user: JsonUserResponse? = JsonUserResponse()
)
