package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData
import com.google.gson.annotations.SerializedName

data class JsonEditPasswordResponse(
    val codAcao: Int? = null,
    val msgInterna: String? = null,
    val msgExterna: String? = null
)

