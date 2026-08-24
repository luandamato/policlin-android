package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData
import java.io.Serializable

data class PresentationCityOptions(var code: Int = InvalidData.UNINITIALIZED.getInt(),
                                   var description: String = InvalidData.UNINITIALIZED.getString()):
Serializable {

    override fun toString(): String {
        return description
    }
}