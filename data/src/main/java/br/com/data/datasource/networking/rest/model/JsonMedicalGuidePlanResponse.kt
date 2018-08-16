package br.com.data.datasource.networking.rest.model

import br.com.data.helper.InvalidData
import com.google.gson.annotations.SerializedName

/**
 * Created by lmiyagi on 3/20/18.
 */
data class JsonMedicalGuidePlanResponse(
        @SerializedName("desc") val description: String? = InvalidData.UNINITIALIZED.getString())