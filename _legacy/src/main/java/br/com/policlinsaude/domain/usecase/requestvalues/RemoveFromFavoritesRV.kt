package br.com.policlinsaude.domain.usecase.requestvalues

import br.com.policlinsaude.domain.model.Establishment
import br.com.policlinsaude.domain.usecase.base.BaseRequestValues

class RemoveFromFavoritesRV(val establishment: Establishment)
    : BaseRequestValues