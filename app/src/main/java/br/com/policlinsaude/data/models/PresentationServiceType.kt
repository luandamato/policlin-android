package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData

data class PresentationServiceType(var codeServiceType: String = InvalidData.UNINITIALIZED.getString(),
                                   var descriptionServiceType: String = InvalidData.UNINITIALIZED.getString()) {

    override fun toString(): String {
        return descriptionServiceType
    }
}