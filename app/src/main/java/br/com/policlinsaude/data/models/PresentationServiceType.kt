package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData
import java.io.Serializable

data class PresentationServiceType(var codeServiceType: String = InvalidData.UNINITIALIZED.getString(),
                                   var descriptionServiceType: String = InvalidData.UNINITIALIZED.getString()):


Serializable {

    override fun toString(): String {
        return descriptionServiceType
    }
}
