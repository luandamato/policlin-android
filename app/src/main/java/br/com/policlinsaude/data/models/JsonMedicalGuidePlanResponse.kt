package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData
import com.google.gson.annotations.SerializedName

/**
 * Created by lmiyagi on 3/20/18.
 */
data class JsonMedicalGuidePlanResponse(
        @SerializedName("desc") val description: String? = InvalidData.UNINITIALIZED.getString())