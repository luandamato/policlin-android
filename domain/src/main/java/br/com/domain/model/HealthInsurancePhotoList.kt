package br.com.domain.model

import br.com.domain.helper.InvalidData


data class HealthInsurancePhotoList( var listaimgFrente: List<HealthInsurancePhoto>? = mutableListOf(),
                                     var imgVerso:         String?  = InvalidData.UNINITIALIZED.getString(),
                                     var codAcao:          String?  = InvalidData.UNINITIALIZED.getString(),
                                     var msgInterna:       String?  = InvalidData.UNINITIALIZED.getString(),
                                     var msgExterna:       String?  = InvalidData.UNINITIALIZED.getString())
