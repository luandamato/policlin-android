package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData
import br.com.policlinsaude.domain.models.MedicalGuideListCities

/**
 *
 * Andre em 03/06/2018
 */

data class PresentationMedicalGuideListCitiesV4(var cityName: String = InvalidData.UNINITIALIZED.getString(),
                                                var cityCod: String = InvalidData.UNINITIALIZED.getString(),
                                                var serviceType: List<PresentationMedicalGuideListServiceTypesV4> = mutableListOf()
) {





}