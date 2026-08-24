package br.com.policlinsaude.domain.models

import br.com.policlinsaude.util.helpers.InvalidData

data class EstablishmentTypeOptions(var codeEstablishmentTypeOptions: String = InvalidData.UNINITIALIZED.getString(),
                                    var descriptionEstablishmentTypeOptions: String = InvalidData.UNINITIALIZED.getString())



