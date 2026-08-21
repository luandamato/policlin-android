package br.com.policlinsaude.domain.usecase.requestvalues

import br.com.policlinsaude.domain.usecase.base.BaseRequestValues

/**
 * Created by lmiyagi on 3/26/18.
 */
data class SetNotificationPreferenceRV(val value: Boolean = false)
    : BaseRequestValues