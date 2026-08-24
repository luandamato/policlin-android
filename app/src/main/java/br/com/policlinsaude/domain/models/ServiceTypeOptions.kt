package br.com.policlinsaude.domain.models

import br.com.policlinsaude.util.helpers.InvalidData

data class ServiceTypeOptions(var codeServiceTypeOptions: String = InvalidData.UNINITIALIZED.getString(),
                              var descriptionServiceTypeOptions: String = InvalidData.UNINITIALIZED.getString())