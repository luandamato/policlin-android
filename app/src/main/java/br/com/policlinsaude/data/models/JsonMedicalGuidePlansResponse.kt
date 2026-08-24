package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData
import com.google.gson.annotations.SerializedName

/**
 * Created by lmiyagi on 3/20/18.
 */
data class JsonMedicalGuidePlansResponse(
        @SerializedName("lista") val plans: List<JsonMedicalGuidePlanResponse>? = mutableListOf(),
        @SerializedName("codAcao") val actionCode: Int? = InvalidData.UNINITIALIZED.getInt(),
        @SerializedName("msgInterna") val msgInternal: String? = InvalidData.UNINITIALIZED.getString(),
        @SerializedName("msgExterna") val msgExternal: String? = InvalidData.UNINITIALIZED.getString())