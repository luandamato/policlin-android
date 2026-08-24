package br.com.policlinsaude.domain.models

import br.com.policlinsaude.util.helpers.InvalidData

data class ProfessionalClassOptions(var codeProfessionalClassOptions: String = InvalidData.UNINITIALIZED.getString(),
                                    var descriptionProfessionalClassOptions: String = InvalidData.UNINITIALIZED.getString())