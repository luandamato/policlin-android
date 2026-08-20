package br.com.domain.usecase.requestvalues

import br.com.domain.usecase.base.BaseRequestValues

/**
 * Created by lmiyagi on 3/21/18.
 */
data class GetPlansRV(val proUF: String,
                      val prsCod: String,
                      val proCls: String,
                      val proCod: String)
    : BaseRequestValues