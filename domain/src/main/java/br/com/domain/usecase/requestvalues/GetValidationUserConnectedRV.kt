package br.com.domain.usecase.requestvalues

import br.com.domain.usecase.base.BaseRequestValues

class GetValidationUserConnectedRV(
    val registration: String,
    val order: String
) : BaseRequestValues