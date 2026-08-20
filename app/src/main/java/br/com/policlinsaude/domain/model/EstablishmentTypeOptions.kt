package br.com.domain.model

import br.com.domain.helper.InvalidData

data class EstablishmentTypeOptions(var codeEstablishmentTypeOptions: String = InvalidData.UNINITIALIZED.getString(),
                                    var descriptionEstablishmentTypeOptions: String = InvalidData.UNINITIALIZED.getString())



