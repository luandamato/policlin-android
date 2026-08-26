package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData

/**
 *
 * Andre em 03/06/2018
 */

data class PresentationMedicalGuideListServiceTypesV4(
    var serviceType: String = InvalidData.UNINITIALIZED.getString(),
    var specialities: List<PresentationMedicalGuideListSpecialitiesV4> = mutableListOf()
) {



}