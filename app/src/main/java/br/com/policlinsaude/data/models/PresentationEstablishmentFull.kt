package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData

data class PresentationEstablishmentFull(var planNameV4: String = InvalidData.UNINITIALIZED.getString(),//Andre
                                         var cityV4: String = InvalidData.UNINITIALIZED.getString(),//Andre
                                         var serviceTypeV4: String = InvalidData.UNINITIALIZED.getString(),//Andre
                                         var especialityNameV4: String = InvalidData.UNINITIALIZED.getString(),//Andre
                                         var medicalGuide: List<PresentationEstablishment> = mutableListOf()
                                     ) {





}