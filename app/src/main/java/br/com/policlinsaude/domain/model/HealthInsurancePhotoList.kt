package br.com.policlinsaude.domain.model

import br.com.policlinsaude.domain.helper.InvalidData


data class HealthInsurancePhotoList( var listaimgFrente: List<HealthInsurancePhoto>? = mutableListOf(),
                                     var imgVerso:         String?  = InvalidData.UNINITIALIZED.getString(),
                                     var codAcao:          String?  = InvalidData.UNINITIALIZED.getString(),
                                     var msgInterna:       String?  = InvalidData.UNINITIALIZED.getString(),
                                     var msgExterna:       String?  = InvalidData.UNINITIALIZED.getString())
