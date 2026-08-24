package br.com.policlinsaude.domain.usecase.requestvalues

import br.com.policlinsaude.domain.helper.InvalidData
import br.com.policlinsaude.domain.usecase.base.BaseRequestValues

data class DoLoginRV(
    val register: String = InvalidData.UNINITIALIZED.getString(),
    val order: String = InvalidData.UNINITIALIZED.getString(),
    val password: String = InvalidData.UNINITIALIZED.getString(),
    val firebaseToken: String = InvalidData.UNINITIALIZED.getString(),
    val osVersion: String = InvalidData.UNINITIALIZED.getString()
)
    : BaseRequestValues
