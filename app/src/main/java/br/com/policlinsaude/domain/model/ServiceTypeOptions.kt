package br.com.domain.model

import br.com.domain.helper.InvalidData

data class ServiceTypeOptions(var codeServiceTypeOptions: String = InvalidData.UNINITIALIZED.getString(),
                              var descriptionServiceTypeOptions: String = InvalidData.UNINITIALIZED.getString())