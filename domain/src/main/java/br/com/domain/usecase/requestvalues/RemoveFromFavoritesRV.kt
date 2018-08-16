package br.com.domain.usecase.requestvalues

import br.com.domain.model.Establishment
import br.com.domain.usecase.base.BaseRequestValues

class RemoveFromFavoritesRV(val establishment: Establishment)
    : BaseRequestValues