package br.com.policlinsaude.domain.model

import br.com.policlinsaude.domain.helper.InvalidData

data class PlanOptions(var codeGuide: Int = InvalidData.UNINITIALIZED.getInt(),
                var codePlan: Int = InvalidData.UNINITIALIZED.getInt(),
                var description: String = InvalidData.UNINITIALIZED.getString())