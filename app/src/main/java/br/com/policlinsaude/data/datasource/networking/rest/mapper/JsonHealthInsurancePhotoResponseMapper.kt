package br.com.data.datasource.networking.rest.mapper


import android.util.Log
import br.com.data.datasource.networking.rest.model.*
import br.com.data.helper.InvalidData
import br.com.domain.model.*


object JsonHealthInsurancePhotoResponseMapper {

    fun transform(jsonHealthInsurancePhotoResponse: JsonHealthInsurancePhotoResponse): HealthInsurancePhotoList {
        val healthInsurancePhotoList = HealthInsurancePhotoList()

            healthInsurancePhotoList.imgVerso = jsonHealthInsurancePhotoResponse.imgVerso
            healthInsurancePhotoList.codAcao = jsonHealthInsurancePhotoResponse.codAcao
            healthInsurancePhotoList.msgInterna = jsonHealthInsurancePhotoResponse.msgInternal
            healthInsurancePhotoList.msgInterna = jsonHealthInsurancePhotoResponse.msgExternal

            jsonHealthInsurancePhotoResponse.listImages?.let {
            healthInsurancePhotoList.listaimgFrente = transform(it.toTypedArray())
            }

             return healthInsurancePhotoList
    }

    //Andre ----

    fun transform(jsonHealthInsurancePhotoListResponse: Array<JsonHealthInsurancePhotoListResponse>): List<HealthInsurancePhoto> {
              return jsonHealthInsurancePhotoListResponse.map{ jsonPhoto ->
                HealthInsurancePhoto(imgFrente = jsonPhoto.imageFront ?: InvalidData.UNINITIALIZED.getString(),
                                     ordem     = jsonPhoto.ordem ?: InvalidData.UNINITIALIZED.getString(),
                                     titular   = jsonPhoto.titular ?: InvalidData.UNINITIALIZED.getString())
            }

        return mutableListOf()
    }




// Andre----


}