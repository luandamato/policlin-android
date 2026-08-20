package br.com.data.datasource.networking.rest.model

import br.com.data.helper.InvalidData
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

//data class JsonMedicalGuideServiceTypeResponse(@SerializedName("tiposervico") val serviceType: List<JsonServiceType>?  = mutableListOf())
//JsonServiceType
data class JsonMedicalGuideServiceTypeResponse (@SerializedName("TIPOSERV") val serviceType: String? = InvalidData.UNINITIALIZED.getString(),
                                              @SerializedName("especialidade") val speciality: List<JsonMedicalGuideSpecialitiesResponse>? = mutableListOf())

