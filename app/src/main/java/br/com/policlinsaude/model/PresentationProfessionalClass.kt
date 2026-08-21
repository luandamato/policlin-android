package br.com.policlinsaude.model

import br.com.policlinsaude.domain.helper.InvalidData
import java.io.Serializable

data class PresentationProfessionalClass(var codeProfessionalClass: String = InvalidData.UNINITIALIZED.getString(),
                                         var descriptionProfessionalClass: String = InvalidData.UNINITIALIZED.getString()):


Serializable {

    override fun toString(): String {
        return descriptionProfessionalClass
    }
}
