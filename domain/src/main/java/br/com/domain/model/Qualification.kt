package br.com.domain.model

import br.com.domain.helper.InvalidData

data class Qualification(var image: String = InvalidData.UNINITIALIZED.getString(),
                         var initial: String = InvalidData.UNINITIALIZED.getString(),
                         var description: String = InvalidData.UNINITIALIZED.getString())