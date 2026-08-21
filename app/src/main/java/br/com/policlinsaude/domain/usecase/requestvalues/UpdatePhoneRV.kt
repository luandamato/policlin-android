package br.com.policlinsaude.domain.usecase.requestvalues

import br.com.policlinsaude.domain.usecase.base.BaseRequestValues

data class UpdatePhoneRV(val codeArea: String,
                         val phone: String) : BaseRequestValues