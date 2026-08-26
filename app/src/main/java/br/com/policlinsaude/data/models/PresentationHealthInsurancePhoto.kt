package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData
import br.com.policlinsaude.domain.models.MedicalGuideListCities

/**
 *
 * Andre em 03/06/2018
 */

data class PresentationHealthInsurancePhoto(var imgFrente:  String? = InvalidData.UNINITIALIZED.getString(),
                                            var titular:    String? = InvalidData.UNINITIALIZED.getString(),
                                            var ordem:      String? = InvalidData.UNINITIALIZED.getString()
) {





}