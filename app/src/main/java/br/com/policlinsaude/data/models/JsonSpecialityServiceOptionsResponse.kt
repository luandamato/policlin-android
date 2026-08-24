package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData
import com.google.gson.annotations.SerializedName

data class JsonSpecialityServiceOptionsResponse(
        @SerializedName("lista") val specialityServiceOptions: List<JsonSpecialityServiceOptions>?
        = mutableListOf())

data class JsonSpecialityServiceOptions(@SerializedName("Codigo_Especialidade_Servicos") val code: Int?
                            = InvalidData.UNINITIALIZED.getInt(),
                           @SerializedName("Descricao_Especialidade_Servicos") val description: String?
                           = InvalidData.UNINITIALIZED.getString(),
                                        @SerializedName("Tipo_Especialidade_Servicos") val type: String?
                                        = InvalidData.UNINITIALIZED.getString())