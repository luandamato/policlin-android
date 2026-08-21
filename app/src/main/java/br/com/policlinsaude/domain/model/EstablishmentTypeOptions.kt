package br.com.policlinsaude.domain.model

import br.com.policlinsaude.domain.helper.InvalidData

data class EstablishmentTypeOptions(var codeEstablishmentTypeOptions: String = InvalidData.UNINITIALIZED.getString(),
                                    var descriptionEstablishmentTypeOptions: String = InvalidData.UNINITIALIZED.getString())



