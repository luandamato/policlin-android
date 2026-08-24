package br.com.policlinsaude.domain.models

import br.com.policlinsaude.util.helpers.InvalidData

data class Qualification(var image: String = InvalidData.UNINITIALIZED.getString(),
                         var initial: String = InvalidData.UNINITIALIZED.getString(),
                         var description: String = InvalidData.UNINITIALIZED.getString())