package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData
import br.com.policlinsaude.domain.models.MedicalGuideListCities

/**
 *
 * Andre em 03/06/2018
 */


data class PresentationMedicalGuideListSpecialitiesV4(var specialityName: String = InvalidData.UNINITIALIZED.getString(),
                                                      var specialityCod: String = InvalidData.UNINITIALIZED.getString(),
                                                      var ent_tipo: String = InvalidData.UNINITIALIZED.getString(),
                                                      var medicalGuide: List<PresentationEstablishment> = mutableListOf()
) {





}