package br.com.policlinsaude.domain.usecase.requestvalues

import br.com.policlinsaude.domain.usecase.base.BaseRequestValues

class GetValidationUserConnectedRV(
    val registration: String,
    val order: String
) : BaseRequestValues