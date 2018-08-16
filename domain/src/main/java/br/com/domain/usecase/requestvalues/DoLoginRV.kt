package br.com.domain.usecase.requestvalues

import br.com.domain.helper.InvalidData
import br.com.domain.usecase.base.BaseRequestValues

data class DoLoginRV(val register: String = InvalidData.UNINITIALIZED.getString(),
                     val order: String = InvalidData.UNINITIALIZED.getString(),
                                   val password: String = InvalidData.UNINITIALIZED.getString())
    : BaseRequestValues
