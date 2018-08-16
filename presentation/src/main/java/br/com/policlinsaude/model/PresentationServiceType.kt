package br.com.policlinsaude.model

import br.com.domain.helper.InvalidData
import java.io.Serializable

data class PresentationServiceType(var codeServiceType: String = InvalidData.UNINITIALIZED.getString(),
                                   var descriptionServiceType: String = InvalidData.UNINITIALIZED.getString()):


Serializable {

    override fun toString(): String {
        return descriptionServiceType
    }
}
