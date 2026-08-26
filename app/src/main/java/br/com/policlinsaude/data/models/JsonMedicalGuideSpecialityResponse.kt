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
                      CidadeNome
                      _CIDCOD

                      tiposervico --> array
                                      TIPOSERV

                      especialidade --> array
                                    EspecialidadeNome
                                    _ESCOD
                                    _Ent_Tipo

                                    guiamedico --> array*/


//data class JsonMedicalGuideSpecialitiesResponse(@SerializedName("especialidade") val planOptions: List<JsonSpecialityResponse>?  = mutableListOf())
//JsonSpecialityResponse
data class JsonMedicalGuideSpecialitiesResponse (@SerializedName("EspecialidadeNome") val specialityName: String? = InvalidData.UNINITIALIZED.getString(),
                                              @SerializedName("_ESCOD") val specialityCode: String? = InvalidData.UNINITIALIZED.getString(),
                                              @SerializedName("_Ent_Tipo") val entType: String? = InvalidData.UNINITIALIZED.getString(),
                                              @SerializedName("guiamedico") val medicalGuide: List<JsonEstablishmentResponse>? = mutableListOf())
