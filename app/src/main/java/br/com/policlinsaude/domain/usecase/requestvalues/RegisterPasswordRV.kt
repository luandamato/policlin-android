package br.com.domain.usecase.requestvalues

import br.com.domain.model.Person
import br.com.domain.model.Plan
import br.com.domain.usecase.base.BaseRequestValues

data class RegisterPasswordRV(val person: Person = Person(),
                              val plan: Plan = Plan())
    : BaseRequestValues