package br.com.policlinsaude.data.models

import br.com.policlinsaude.util.helpers.InvalidData

data class PresentationProfessionalClass(var codeProfessionalClass: String = InvalidData.UNINITIALIZED.getString(),
                                         var descriptionProfessionalClass: String = InvalidData.UNINITIALIZED.getString()) {

    override fun toString(): String {
        return descriptionProfessionalClass
    }
}