package br.com.data.datasource.networking.rest.model

import br.com.data.helper.InvalidData
import com.google.gson.annotations.SerializedName


data class JsonHealthInsurancePhotoResponse(

    /*    @SerializedName("imgFrente") val imageFront: String? = InvalidData.UNINITIALIZED.getString(),
        @SerializedName("msgInterna") val msgInternal: String?
        = InvalidData.UNINITIALIZED.getString(),
        @SerializedName("msgExterna") val msgExternal: String?
        = InvalidData.UNINITIALIZED.getString())*/

        @SerializedName("listaimgFrente") val listImages: List<JsonHealthInsurancePhotoListResponse>? = mutableListOf(),
        @SerializedName("imgVerso")   val imgVerso: String? = InvalidData.UNINITIALIZED.getString(),
        @SerializedName("codAcao")    val codAcao: String? = InvalidData.UNINITIALIZED.getString(),
        @SerializedName("msgInterna") val msgInternal: String? = InvalidData.UNINITIALIZED.getString(),
        @SerializedName("msgExterna") val msgExternal: String? = InvalidData.UNINITIALIZED.getString())



/**
 * Atual:
 *
 *  "listaimgFrente": [
 *                      {
 *                      imgFrente:
 *                      titular:
 *                      ordenm:}
 *                    ],
 * imgVerso:
 * codAcao": 1,
 * msgInterna": "OK",
 * msgExterna": ""
 *
 *
 */
