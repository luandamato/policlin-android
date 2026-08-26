package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData
import br.com.policlinsaude.domain.models.MedicalGuideListCities

/**
 *
 * Andre em 03/06/2018
 */

data class PresentationMedicalGuideListPlansV4(var planName: String = InvalidData.UNINITIALIZED.getString(),
                                               var planCod: String = InvalidData.UNINITIALIZED.getString(),
                                               var cities : List<PresentationMedicalGuideListCitiesV4> = mutableListOf()
) {





}