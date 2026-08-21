package br.com.policlinsaude.domain.usecase.requestvalues

import br.com.policlinsaude.domain.usecase.base.BaseRequestValues

/**
 * Created by lmiyagi on 3/28/18.
 */
data class UpdateAvatarRV(val imageString: String)
    : BaseRequestValues