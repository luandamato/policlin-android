package br.com.policlinsaude.domain.usecase.requestvalues

import br.com.policlinsaude.domain.model.Establishment
import br.com.policlinsaude.domain.usecase.base.BaseRequestValues

/**
 * Created by lmiyagi on 3/27/18.
 */
class AddToFavoriteRV(val establishment: Establishment)
    : BaseRequestValues