package br.com.domain.usecase.requestvalues

import br.com.domain.model.Establishment
import br.com.domain.usecase.base.BaseRequestValues

/**
 * Created by lmiyagi on 3/27/18.
 */
class AddToFavoriteRV(val establishment: Establishment)
    : BaseRequestValues