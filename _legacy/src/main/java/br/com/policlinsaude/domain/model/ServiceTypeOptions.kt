package br.com.policlinsaude.domain.model

import br.com.policlinsaude.domain.helper.InvalidData

data class ServiceTypeOptions(var codeServiceTypeOptions: String = InvalidData.UNINITIALIZED.getString(),
                              var descriptionServiceTypeOptions: String = InvalidData.UNINITIALIZED.getString())