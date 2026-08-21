package br.com.policlinsaude.data.datasource.networking.rest.model

import br.com.policlinsaude.data.helper.InvalidData
import com.google.gson.annotations.SerializedName

data class JsonServiceTypeResponse(@SerializedName("lista") val serviceTypeOptions: List<JsonServiceTypeOptions>?
                                   = mutableListOf())

data class JsonServiceTypeOptions(@SerializedName("Codigo_TipoServico") val codeServiceType: String?
                                                         = InvalidData.UNINITIALIZED.getString(),
                                        @SerializedName("Descricao_TipoServico") val descriptionServiceType: String?
                                                                   = InvalidData.UNINITIALIZED.getString())