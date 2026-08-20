package br.com.domain.usecase.requestvalues

import br.com.domain.helper.InvalidData
import br.com.domain.usecase.base.BaseRequestValues

data class RecoverPasswordRV(val register: String = InvalidData.UNINITIALIZED.getString(),
                             val order: String = InvalidData.UNINITIALIZED.getString(),
                             val email: String = InvalidData.UNINITIALIZED.getString())
    : BaseRequestValues