package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData
import com.google.gson.annotations.SerializedName

data class JsonMedicalGuideOptionsResponse(
        @SerializedName("plano") val plan: JsonPlanOptionsResponse? = JsonPlanOptionsResponse(),
        @SerializedName("cidade") val city: JsonCityOptionsResponse? = JsonCityOptionsResponse(),
        @SerializedName("especialidadeServico") val specialityService: JsonSpecialityServiceOptionsResponse? = JsonSpecialityServiceOptionsResponse(),
        @SerializedName("classeProfissional") val professionalClass: JsonProfessionalClassResponse? = JsonProfessionalClassResponse(),
        @SerializedName("tipoServico") val serviceType: JsonServiceTypeResponse? = JsonServiceTypeResponse(),
        @SerializedName("tipoEstabelecimento") val establishmentType: JsonEstablishmentTypeResponse? = JsonEstablishmentTypeResponse(),
        @SerializedName("qualificacao") val qualification: JsonQualificationsForFilterResponse? = JsonQualificationsForFilterResponse(),
        @SerializedName("msgInterna") val msgInternal: String?       = InvalidData.UNINITIALIZED.getString(),
        @SerializedName("msgExterna") val msgExternal: String?       = InvalidData.UNINITIALIZED.getString())