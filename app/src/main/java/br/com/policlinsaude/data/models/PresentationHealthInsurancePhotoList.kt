package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData



data class PresentationHealthInsurancePhotoList( var listaimgFrente: List<PresentationHealthInsurancePhoto>? = mutableListOf(),
                                     var imgVerso:         String?  = InvalidData.UNINITIALIZED.getString(),
                                     var codAcao:          String?  = InvalidData.UNINITIALIZED.getString(),
                                     var msgInterna:       String?  = InvalidData.UNINITIALIZED.getString(),
                                     var msgExterna:       String?  = InvalidData.UNINITIALIZED.getString()


) {





}