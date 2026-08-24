package br.com.policlinsaude.domain.usecase.requestvalues

import br.com.policlinsaude.domain.usecase.base.BaseRequestValues

data class EditPasswordRV(val password: String)
    : BaseRequestValues