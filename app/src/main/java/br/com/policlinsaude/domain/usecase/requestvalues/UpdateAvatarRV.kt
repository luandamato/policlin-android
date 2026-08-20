package br.com.domain.usecase.requestvalues

import br.com.domain.usecase.base.BaseRequestValues

/**
 * Created by lmiyagi on 3/28/18.
 */
data class UpdateAvatarRV(val imageString: String)
    : BaseRequestValues