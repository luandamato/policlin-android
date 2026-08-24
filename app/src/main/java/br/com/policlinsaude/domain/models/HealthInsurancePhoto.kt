package br.com.policlinsaude.domain.models

import br.com.policlinsaude.util.helpers.InvalidData


/*data class HealthInsurancePhoto(var code: Int = InvalidData.UNINITIALIZED.getInt(),
                                var description: String = InvalidData.UNINITIALIZED.getString())*/

data class HealthInsurancePhoto(var imgFrente:  String? = InvalidData.UNINITIALIZED.getString(),
                                var titular:    String? = InvalidData.UNINITIALIZED.getString(),
                                var ordem:      String? = InvalidData.UNINITIALIZED.getString())





