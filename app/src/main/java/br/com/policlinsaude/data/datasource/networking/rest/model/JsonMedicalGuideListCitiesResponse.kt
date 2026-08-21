package br.com.policlinsaude.data.datasource.networking.rest.model

import br.com.policlinsaude.data.helper.InvalidData
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

//data class JsonMedicalGuideListCitiesResponse(@SerializedName("cidades") val cities: List<JsonCitiesResponse>?  = mutableListOf())
//JsonCitiesResponse

data class JsonMedicalGuideListCitiesResponse (@SerializedName("CidadeNome") val cityName: String? = InvalidData.UNINITIALIZED.getString(),
                              @SerializedName("_CIDCOD") val cidCod: String? = InvalidData.UNINITIALIZED.getString(),
                              @SerializedName("tiposervico") val serviceType: List<JsonMedicalGuideServiceTypeResponse>? = mutableListOf())

