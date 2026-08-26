package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData
import com.google.gson.annotations.SerializedName

data class JsonEstablishmentTypeResponse(@SerializedName("lista") val establishmentTypeOptions: List<JsonEstablishmentTypeOptions>?
                                   = mutableListOf())

data class JsonEstablishmentTypeOptions(@SerializedName("Codigo_TipoEstabelecimento") val codeEstablishmentType: String?
                                                         = InvalidData.UNINITIALIZED.getString(),
                                        @SerializedName("Descricao_TipoEstabelecimento") val descriptionEstablishmentType: String?
                                                                   = InvalidData.UNINITIALIZED.getString())

