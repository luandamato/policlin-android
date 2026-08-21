package br.com.policlinsaude.domain.model

import br.com.policlinsaude.domain.helper.InvalidData

data class ProfessionalClassOptions(var codeProfessionalClassOptions: String = InvalidData.UNINITIALIZED.getString(),
                                    var descriptionProfessionalClassOptions: String = InvalidData.UNINITIALIZED.getString())