package br.com.policlinsaude.data.datasource.networking.rest.model

import br.com.policlinsaude.data.helper.InvalidData
import com.google.gson.annotations.SerializedName

/**
 * Criado por Andre
 */

data class JsonHealthInsurancePhotoListResponse(
        @SerializedName("imgFrente") val imageFront: String? = InvalidData.UNINITIALIZED.getString(),
        @SerializedName("titular") val titular: String?      = InvalidData.UNINITIALIZED.getString(),
        @SerializedName("ordem") val ordem: String?          = InvalidData.UNINITIALIZED.getString())


/**
 *  "listaimgFrente": [
 *                      {
 *                      imgFrente:
 *                      titular:
 *                      ordem:}
 *                    ],
 * imgVerso:
 * codAcao": 1,
 * msgInterna": "OK",
 * msgExterna": ""
 *
 *
 */
