package br.com.policlinsaude.domain.models

import br.com.policlinsaude.util.helpers.InvalidData

data class PlanOptions(var codeGuide: Int = InvalidData.UNINITIALIZED.getInt(),
                var codePlan: Int = InvalidData.UNINITIALIZED.getInt(),
                var description: String = InvalidData.UNINITIALIZED.getString())