package br.com.domain.model

import br.com.domain.helper.InvalidData

data class PlanOptions(var codeGuide: Int = InvalidData.UNINITIALIZED.getInt(),
                var codePlan: Int = InvalidData.UNINITIALIZED.getInt(),
                var description: String = InvalidData.UNINITIALIZED.getString())