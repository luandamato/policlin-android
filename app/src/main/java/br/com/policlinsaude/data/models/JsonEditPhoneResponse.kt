package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData
import com.google.gson.annotations.SerializedName

/**
 * Created by lmiyagi on 05/04/18.
 */
data class JsonEditPhoneResponse(
    val codAcao: Int? = null,
    val msgInterna: String? = null,
    val msgExterna: String? = null
)