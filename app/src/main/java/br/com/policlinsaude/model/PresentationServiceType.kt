package br.com.policlinsaude.model

import br.com.policlinsaude.domain.helper.InvalidData
import java.io.Serializable

data class PresentationServiceType(var codeServiceType: String = InvalidData.UNINITIALIZED.getString(),
                                   var descriptionServiceType: String = InvalidData.UNINITIALIZED.getString()):


Serializable {

    override fun toString(): String {
        return descriptionServiceType
    }
}
