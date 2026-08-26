package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData
import com.google.gson.annotations.SerializedName

/**
 *
 * Andre em 31/05/2018
 */

/*root: plano -array

campos plano: PlanoNome
              _PlanoCod

              cidades -->array
  */

//data class JsonMedicalGuideListPlansResponse(@SerializedName("planos") val plan: List<JsonPlansResponse>?  = mutableListOf())

//JsonMedicalGuideListPlansResponse
//JsonPlansResponse

data class JsonMedicalGuideListPlansResponse(
                    @SerializedName("PlanoNome") val planName: String? = InvalidData.UNINITIALIZED.getString(),
                    @SerializedName("_PlanCod") val planCod: String? = InvalidData.UNINITIALIZED.getString(),
                    @SerializedName("cidades") val cities: List<JsonMedicalGuideListCitiesResponse>? = mutableListOf())
