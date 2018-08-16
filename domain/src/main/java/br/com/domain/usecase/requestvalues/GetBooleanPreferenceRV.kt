package br.com.domain.usecase.requestvalues

import br.com.domain.usecase.base.BaseRequestValues

/**
 * Created by lmiyagi on 3/26/18.
 */
class GetBooleanPreferenceRV(val key: String,
                             val defaultValue: Boolean = false)
    : BaseRequestValues