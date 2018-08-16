package br.com.domain.usecase.requestvalues

import br.com.domain.usecase.base.BaseRequestValues

data class EditPasswordRV(val password: String)
    : BaseRequestValues