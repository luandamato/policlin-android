package br.com.policlinsaude.domain.usecase.requestvalues

import br.com.policlinsaude.domain.model.Person
import br.com.policlinsaude.domain.model.Plan
import br.com.policlinsaude.domain.usecase.base.BaseRequestValues

data class RegisterPasswordRV(val person: Person = Person(),
                              val plan: Plan = Plan())
    : BaseRequestValues