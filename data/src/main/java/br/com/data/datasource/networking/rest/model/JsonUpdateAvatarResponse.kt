package br.com.data.datasource.networking.rest.model

import br.com.data.helper.InvalidData
import com.google.gson.annotations.SerializedName

/**
 * Created by lmiyagi on 3/28/18.
 */
data class JsonUpdateAvatarResponse(@SerializedName("msgInterna") val msgInternal: String?
                                    = InvalidData.UNINITIALIZED.getString(),
                                    @SerializedName("msgExterna") val msgExternal: String?
                                    = InvalidData.UNINITIALIZED.getString())