package br.com.domain.usecase.requestvalues

import br.com.domain.model.Person
import br.com.domain.model.Plan
import br.com.domain.usecase.base.BaseRequestValues

data class CheckPlanRV(val person: Person,
                       val plan: Plan)
    : BaseRequestValues