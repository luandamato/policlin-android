package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData
import com.google.gson.annotations.SerializedName

data class JsonMedicalGuideListResponse(
     //   @SerializedName("lista") val establishments: List<JsonEstablishmentResponse>? = mutableListOf(),
       //Andre mudança filtro v4
        @SerializedName("planos") val plans: List<JsonMedicalGuideListPlansResponse>? = mutableListOf(),

        @SerializedName("legenda") val qualifications: List<JsonQualificationResponse>? = mutableListOf(),
        @SerializedName("msgInterna") val msgInternal: String?
                             = InvalidData.UNINITIALIZED.getString(),
        @SerializedName("msgExterna") val msgExternal: String?
                             = InvalidData.UNINITIALIZED.getString())