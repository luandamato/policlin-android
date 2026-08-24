package br.com.policlinsaude.domain.model

import br.com.policlinsaude.domain.helper.InvalidData

data class Qualification(var image: String = InvalidData.UNINITIALIZED.getString(),
                         var initial: String = InvalidData.UNINITIALIZED.getString(),
                         var description: String = InvalidData.UNINITIALIZED.getString())