package br.com.policlinsaude.data.datasource.networking.rest.model

import br.com.policlinsaude.data.helper.InvalidData
import com.google.gson.annotations.SerializedName

/**
 * Created by lmiyagi on 05/04/18.
 */
data class JsonEditPhoneResponse(@SerializedName("msgInterna") val msgInternal: String?
                                 = InvalidData.UNINITIALIZED.getString(),
                                 @SerializedName("msgExterna") val msgExternal: String?
                                 = InvalidData.UNINITIALIZED.getString())