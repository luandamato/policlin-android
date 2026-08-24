package br.com.policlinsaude.data.datasource.networking.rest.model

import br.com.policlinsaude.data.helper.InvalidData
import com.google.gson.annotations.SerializedName

data class JsonProfessionalClassResponse(@SerializedName("lista") val professionalClassOptions: List<JsonProfessionalClassOptions>?
                                   = mutableListOf())

data class JsonProfessionalClassOptions(@SerializedName("Codigo_ClasseProfissional") val codeProfessionalClass: String?
                                                         = InvalidData.UNINITIALIZED.getString(),
                                        @SerializedName("Descricao_ClasseProfissional") val descriptionProfessionalClass: String?
                                                                   = InvalidData.UNINITIALIZED.getString())