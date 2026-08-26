package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData

data class PresentationCityOptions(var code: Int = InvalidData.UNINITIALIZED.getInt(),
                                   var description: String = InvalidData.UNINITIALIZED.getString()) {

    override fun toString(): String {
        return description
    }
}