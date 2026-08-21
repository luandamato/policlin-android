package br.com.policlinsaude.domain.usecase.requestvalues

import br.com.policlinsaude.domain.helper.InvalidData
import br.com.policlinsaude.domain.usecase.base.BaseRequestValues

data class RecoverPasswordRV(val register: String = InvalidData.UNINITIALIZED.getString(),
                             val order: String = InvalidData.UNINITIALIZED.getString(),
                             val email: String = InvalidData.UNINITIALIZED.getString())
    : BaseRequestValues