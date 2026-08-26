package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData

data class PresentationPlanOptions(var codeGuide: Int = InvalidData.UNINITIALIZED.getInt(),
                                   var codePlan: Int = InvalidData.UNINITIALIZED.getInt(),
                                   var description: String = InvalidData.UNINITIALIZED.getString())
    {

    override fun toString(): String {
        return description
    }
}