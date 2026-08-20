package br.com.domain.usecase.requestvalues

import br.com.domain.usecase.base.BaseRequestValues

data class UpdatePhoneRV(val codeArea: String,
                         val phone: String) : BaseRequestValues