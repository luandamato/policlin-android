package br.com.domain.model

import br.com.domain.helper.InvalidData

data class ProfessionalClassOptions(var codeProfessionalClassOptions: String = InvalidData.UNINITIALIZED.getString(),
                                    var descriptionProfessionalClassOptions: String = InvalidData.UNINITIALIZED.getString())